package nullPointerAnalysis;

import baseVisitors.ExpressionToStringVisitor;
import syntaxtree.Expression;
import syntaxtree.PrimaryExpression;
import utils.FlowSensitiveVariable;
import utils.Location;

public class VariableRes extends FlowSensitiveNullPointerAnalysisVariable<String>
{
	private Expression expression;


	public VariableRes(Expression expression, Location statement)
	{
		super(expression.accept(new ExpressionToStringVisitor(), null) + "@" + Integer.toHexString(expression.hashCode()), statement);
	}

	public VariableRes(PrimaryExpression expression, Location statement)
	{
		super(expression.accept(new ExpressionToStringVisitor(), null) + "@" + Integer.toHexString(expression.hashCode()), statement);
	}


	@Override
	public String getFunctionName()
	{
		return "res";
	}

}
