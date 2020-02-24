import baseVisitors.AllocationVisitor;
import baseVisitors.VoidScopeVisitor;
import nullPointerAnalysis.*;
import syntaxtree.*;
import utils.FlowSensitiveVariable;
import utils.Location;
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

	List<ObjectIdentifierDefinition> nullablesInScope;

	@Override
	public void visitScope(MainClass n, Location argu)
	{
//		n.f14.accept(this, null);

		nullablesInScope = ProgramStructureCollector.getNullableIdentifiersInScope(new Scope(getClassName(), getMethodName()));
		n.f15.accept(this, null);
	}

	@Override
	public void visitScope(MethodDeclaration n, Location argu)
	{
//		n.f7.accept(this, null);

		nullablesInScope = ProgramStructureCollector.getNullableIdentifiersInScope(new Scope(getClassName(), getMethodName()));
		n.f8.accept(this, null);

		//return statement
		Location returnLocation = new Location(n.f9);
		for (ObjectIdentifierDefinition nullable : nullablesInScope)
		{
			variables.add(new VariableIn(nullable, returnLocation));
			variables.add(new VariableOut(nullable, returnLocation));
		}

		n.f10.accept(this, new Location(n.f9));
	}

	@Override
	public void visitScope(ClassDeclaration n, Location argu)
	{
		if (AllocationVisitor.usedClasses.contains(getClassName()))
			n.f4.accept(this, null);
	}

	@Override
	protected void visitScope(ClassExtendsDeclaration n, Location argu)
	{
		if (AllocationVisitor.usedClasses.contains(getClassName()))
			n.f6.accept(this, null);
	}


	@Override
	public void visit(MessageSend n, Location argu)
	{
		variables.add(new VariableRes(n, argu));
		super.visit(n, argu);
	}

	@Override
	public void visit(Statement n, Location argu)
	{
//		List<utils.ObjectIdentifierDefinition> nullables = NullableCollector.getNullableIdentifierInScope(new Scope(getClassName(), getMethodName()));

		Location location = new Location(n);
		for (ObjectIdentifierDefinition nullable : nullablesInScope)
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

		//if (n.f0.choice instanceof AllocationExpression == false)
		//{
		VariableRes vRes = new VariableRes(n, argu);
		variables.add(vRes);
		//}
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

	@Override
	public void visit(AllocationExpression n, Location argu)
	{
		variables.add(new VariableRes(n, argu));
	}
}
