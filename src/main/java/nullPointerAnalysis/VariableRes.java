package nullPointerAnalysis;

import baseVisitors.ExpressionToStringVisitor;
import syntaxtree.Expression;
import syntaxtree.PrimaryExpression;
import utils.FlowSensitiveVariable;
import utils.Location;

public class VariableRes implements FlowSensitiveVariable<Expression, AnalysisResult>
{
	private Expression expression;
	private Location statement;

	private String expressionStr;

	public VariableRes(Expression expression, Location statement)
	{
//		this.expression = expression;

		expressionStr = expression.accept(new ExpressionToStringVisitor(), null) + "@" + Integer.toHexString(expression.hashCode());
		this.statement = statement;
	}

	public VariableRes(PrimaryExpression expression, Location statement)
	{
		expressionStr = expression.accept(new ExpressionToStringVisitor(), null) + "@" + Integer.toHexString(expression.hashCode());
		this.statement = statement;
	}


	@Override
	public Location getStatement()
	{
		return statement;
	}

	@Override
	public String toString()
	{
		return String.format("res[%s, %s]", expressionStr, statement);
	}

	@Override
	public String getFunctionName()
	{
		return "res";
	}

	@Override
	public Expression getInput()
	{
		return expression;
	}
}
