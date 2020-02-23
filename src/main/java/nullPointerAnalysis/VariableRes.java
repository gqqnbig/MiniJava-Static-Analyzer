package nullPointerAnalysis;

import baseVisitors.ExpressionToStringVisitor;
import syntaxtree.*;
import utils.FlowSensitiveVariable;
import utils.Location;

public class VariableRes extends FlowSensitiveNullPointerAnalysisVariable<String>
{
	private Node expression;


	private VariableRes(Node expressionChoise, Location statement)
	{
		super(expressionChoise.accept(new ExpressionToStringVisitor(), null) + "@" + Integer.toHexString(expressionChoise.hashCode()), statement);
		expression = expressionChoise;
	}

	public VariableRes(Expression expression, Location statement)
	{
		this(expression.f0.choice, statement);
		this.expression = expression.f0.choice;
	}

	public VariableRes(PrimaryExpression expression, Location statement)
	{
		this((Node) expression, statement);
		this.expression = expression;
	}

	public VariableRes(AllocationExpression n, Location statement)
	{
		this((Node) n, statement);
		expression = n;
	}

	public VariableRes(MessageSend n, Location statement)
	{
		this((Node) n, statement);
		expression = n;
	}


	@Override
	public String getFunctionName()
	{
		return "res";
	}

	public Node getExpression()
	{
		return expression;
	}
}
