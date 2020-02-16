import syntaxtree.*;
import visitor.GJVoidDepthFirst;

import java.util.ArrayList;

/**
 * Collect mathematical variables for solving constraints.
 * The variable doesn't refer to variables in programming.
 */
public class VariableCollector extends GJVoidDepthFirst<Location>
{
	ArrayList<FlowSensitiveVariable> variables = new ArrayList<>();

	@Override
	public void visit(MethodDeclaration n, Location argu)
	{
		n.f7.accept(this, argu);
		n.f8.accept(this, argu);

		n.f10.accept(this, new Location(n.f9));
	}

	@Override
	public void visit(VarDeclaration n, Location argu)
	{
		if (NullableCollector.isNullable(n.f0) == false)
			return;

//		assert NullableCollector.nullables.containsKey(n.f1);

		VariableIn vIn = new VariableIn(n.f1, new Location(n));
		variables.add(vIn);

		VariableOut vOut = new VariableOut(n.f1, new Location(n));
		variables.add(vOut);
	}

	@Override
	public void visit(Statement n, Location argu)
	{


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
