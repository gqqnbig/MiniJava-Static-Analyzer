package nullPointerAnalysis;

import baseVisitors.ExpressionToStringVisitor;
import syntaxtree.AllocationExpression;
import syntaxtree.Expression;
import syntaxtree.Node;
import syntaxtree.PrimaryExpression;
import utils.FlowSensitiveVariable;
import utils.Location;

public class VariableRes extends FlowSensitiveNullPointerAnalysisVariable<String>
{
	private Node expression;


	protected VariableRes(Node n, Location statement)
	{
		super(n.accept(new ExpressionToStringVisitor(), null) + "@" + Integer.toHexString(n.hashCode()), statement);
	}

	public VariableRes(Expression expression, Location statement)
	{
		this((Node) expression, statement);
	}

	public VariableRes(PrimaryExpression expression, Location statement)
	{
		this((Node) expression, statement);
	}

	public VariableRes(AllocationExpression n, Location statement)
	{
		this((Node) n, statement);
		expression=n;
	}


	@Override
	public String getFunctionName()
	{
		return "res";
	}

}
