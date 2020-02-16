import baseVisitors.VoidScopeVisitor;
import nullPointerAnalysis.*;
import syntaxtree.*;
import utils.Location;
import nullPointerAnalysis.PossibleNullLiteral;
import utils.Scope;

import java.util.ArrayList;
import java.util.List;

public class ConstraintCollector extends VoidScopeVisitor<Location>
{
	private boolean isFirstStatement;

	List<NullableIdentifierDefinition> nullablesInScope;

	List<EqualityRelationship> constraints = new ArrayList<>();

	@Override
	public void visitScope(MainClass n, Location argu)
	{
		nullablesInScope = NullableCollector.getNullableIdentifierInScope(new Scope(getClassName(), getMethodName()));
		isFirstStatement = true;
		n.f15.accept(this, null);

		assert n.f15.present() == false || isFirstStatement == false : "If method body has statements, isFirstStatement must already be turned off.";
		isFirstStatement = false;
	}

	@Override
	public void visitScope(MethodDeclaration n, Location argu)
	{
//		n.f7.accept(this, null);

		nullablesInScope = NullableCollector.getNullableIdentifierInScope(new Scope(getClassName(), getMethodName()));
		isFirstStatement = true;
		n.f8.accept(this, null);
		assert n.f8.present() == false || isFirstStatement == false : "If method body has statements, isFirstStatement must already be turned off.";

		if (isFirstStatement)
		{
			//return statement
			Location returnLocation = new Location(n.f9);
			for (NullableIdentifierDefinition nullable : nullablesInScope)
			{
				EqualityRelationship r = new EqualityRelationship();
				r.left = new VariableIn(nullable, returnLocation);
				r.right = new PossibleNullLiteral();
				constraints.add(r);
			}
		}
		isFirstStatement = false;
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
		if (isFirstStatement)
		{

			for (NullableIdentifierDefinition nullable : nullablesInScope)
			{
				EqualityRelationship r = new EqualityRelationship();
				r.left = new VariableIn(nullable, location);
				r.right = new PossibleNullLiteral();
				constraints.add(r);
			}
		}
		isFirstStatement = false;


		super.visit(n, location);
	}

//	@Override
//	public void visit(Expression n, Location argu)
//	{
//		assert argu != null;
//
//		VariableRes vRes = new VariableRes(n, argu);
//		variables.add(vRes);
//
//		super.visit(n, argu);
//	}
//
//	@Override
//	public void visit(PrimaryExpression n, Location argu)
//	{
//		if (n.f0.choice instanceof Identifier)
//			variables.add(new VariableRes(n, argu));
//		else if (n.f0.choice instanceof NotExpression)
//			variables.add(new VariableRes(n, argu));
//		else if (n.f0.choice instanceof BracketExpression)
//			variables.add(new VariableRes(n, argu));
//
//		super.visit(n, argu);
//	}
}
