import baseVisitors.VoidScopeVisitor;
import nullPointerAnalysis.VariableIn;
import nullPointerAnalysis.VariableOut;
import nullPointerAnalysis.VariableRes;
import syntaxtree.*;
import utils.FlowSensitiveVariable;
import utils.Location;
import nullPointerAnalysis.NullableIdentifierDefinition;
import utils.Scope;

import java.util.ArrayList;
import java.util.List;

/**
 * Collect mathematical variables for solving constraints.
 * The variable doesn't refer to variables in programming.
 */
public class VariableCollector extends VoidScopeVisitor<Location>
{
	ArrayList<FlowSensitiveVariable> variables = new ArrayList<>();

	List<NullableIdentifierDefinition> nullablesInScope;

	@Override
	public void visitScope(MainClass n, Location argu)
	{
//		n.f14.accept(this, null);

		nullablesInScope = NullableCollector.getNullableIdentifiersInScope(new Scope(getClassName(), getMethodName()));
		n.f15.accept(this, null);
	}

	@Override
	public void visitScope(MethodDeclaration n, Location argu)
	{
//		n.f7.accept(this, null);

		nullablesInScope = NullableCollector.getNullableIdentifiersInScope(new Scope(getClassName(), getMethodName()));
		n.f8.accept(this, null);

		//return statement
		Location returnLocation = new Location(n.f9);
		for (NullableIdentifierDefinition nullable : nullablesInScope)
		{
			variables.add(new VariableIn(nullable, returnLocation));
			variables.add(new VariableOut(nullable, returnLocation));
		}

		n.f10.accept(this, new Location(n.f9));
	}

	@Override
	public void visitScope(ClassDeclaration n, Location argu)
	{
		n.f4.accept(this, null);
	}

	@Override
	protected void visitScope(ClassExtendsDeclaration n, Location argu)
	{
		n.f6.accept(this, null);
	}

//	@Override
//	public void visit(VarDeclaration n, Location argu)
//	{
//		if (NullableCollector.isNullable(n.f0) == false)
//			return;
//
////		assert NullableCollector.nullables.containsKey(n.f1);
//
//		VariableIn vIn = new VariableIn(new NullableIdentifierDefinition(n.f1, getClassName(), getMethodName(), false), new Location(n));
//		variables.add(vIn);
//
//		VariableOut vOut = new VariableOut(new NullableIdentifierDefinition(n.f1, getClassName(), getMethodName(), false), new Location(n));
//		variables.add(vOut);
//	}

	@Override
	public void visit(Statement n, Location argu)
	{
//		List<utils.NullableIdentifierDefinition> nullables = NullableCollector.getNullableIdentifierInScope(new Scope(getClassName(), getMethodName()));

		Location location = new Location(n);
		for (NullableIdentifierDefinition nullable : nullablesInScope)
		{
			variables.add(new VariableIn(nullable, location));
			variables.add(new VariableOut(nullable, location));
		}

		super.visit(n, location);
	}

	@Override
	public void visit(Expression n, Location argu)
	{
		assert argu != null;

		VariableRes vRes = new VariableRes(n, argu);
		variables.add(vRes);

		super.visit(n, argu);
	}

	@Override
	public void visit(PrimaryExpression n, Location argu)
	{
		if (n.f0.choice instanceof Identifier)
			variables.add(new VariableRes(n, argu));
		else if (n.f0.choice instanceof NotExpression)
			variables.add(new VariableRes(n, argu));
		else if (n.f0.choice instanceof BracketExpression)
			variables.add(new VariableRes(n, argu));

		super.visit(n, argu);
	}
}
