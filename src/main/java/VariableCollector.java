import baseVisitors.VoidScopeVisitor;
import syntaxtree.*;
import utils.Scope;
import visitor.GJVoidDepthFirst;

import java.util.ArrayList;
import java.util.List;

/**
 * Collect mathematical variables for solving constraints.
 * The variable doesn't refer to variables in programming.
 */
public class VariableCollector extends VoidScopeVisitor<Location>
{
	ArrayList<FlowSensitiveVariable> variables = new ArrayList<>();

	@Override
	public void visitScope(MainClass n, Location argu)
	{
		n.f14.accept(this, null);
		n.f15.accept(this, null);
	}

	@Override
	public void visitScope(MethodDeclaration n, Location argu)
	{
		n.f7.accept(this, null);
		n.f8.accept(this, null);

		//return statement
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

	@Override
	public void visit(VarDeclaration n, Location argu)
	{
		if (NullableCollector.isNullable(n.f0) == false)
			return;

//		assert NullableCollector.nullables.containsKey(n.f1);

		VariableIn vIn = new VariableIn(new NullableIdentifierDefinition(n.f1, getClassName(), getMethodName(), false), new Location(n));
		variables.add(vIn);

		VariableOut vOut = new VariableOut(new NullableIdentifierDefinition(n.f1, getClassName(), getMethodName(), false), new Location(n));
		variables.add(vOut);
	}

	@Override
	public void visit(Statement n, Location argu)
	{
		List<NullableIdentifierDefinition> nullables = NullableCollector.getNullableIdentifierInScope(new Scope(getClassName(), getMethodName()));

		for (NullableIdentifierDefinition nullable : nullables)
		{
			variables.add(new VariableIn(nullable, argu));
			variables.add(new VariableOut(nullable, argu));
		}

		super.visit(n, new Location(n));
	}

	@Override
	public void visit(Expression n, Location argu)
	{
		assert argu != null;

		VariableRes vRes = new VariableRes(n, argu);
		variables.add(vRes);

		super.visit(n, argu);
	}
}
