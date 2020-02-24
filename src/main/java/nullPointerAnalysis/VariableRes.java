package nullPointerAnalysis;

import baseVisitors.ExpressionToStringVisitor;
import syntaxtree.*;
import utils.FlowSensitiveVariable;
import utils.Location;

public class VariableRes extends FlowSensitiveNullPointerAnalysisVariable<String>
{
	public static Node diveInto(Expression expression)
	{
		Node n = expression.f0.choice;
		while (true)
		{
			if (n instanceof PrimaryExpression)
			{
				n = diveInto((PrimaryExpression) n);
			}
			else
				return n;
		}
	}

	public static Node diveInto(PrimaryExpression expression)
	{
		Node n = expression.f0.choice;

		while (true)
		{
			if (n instanceof BracketExpression)
			{
				n = diveInto(((BracketExpression) n).f1);
			}
			else
				return n;
		}
	}

	private Node expression;


	private VariableRes(Node expressionChoise, Location statement)
	{

		super(expressionChoise.accept(new ExpressionToStringVisitor(), null) + "@" + Integer.toHexString(expressionChoise.hashCode()), statement);
		assert (expressionChoise instanceof Expression == false);
		assert (expressionChoise instanceof PrimaryExpression == false);

		expression = expressionChoise;
	}

	/**
	 * actually stores expression.f0.choice
	 *
	 * @param statement
	 */
	public VariableRes(Expression expression, Location statement)
	{
		this(diveInto(expression), statement);
	}

	/**
	 * actually stores expression.f0.choice
	 *
	 * @param statement
	 */
	public VariableRes(PrimaryExpression expression, Location statement)
	{
		this(diveInto(expression), statement);
	}

//	public VariableRes(BracketExpression n, Location statement)
//	{
//		this((Node) n, statement);
//	}

	public VariableRes(AllocationExpression n, Location statement)
	{
		this((Node) n, statement);
	}

	public VariableRes(MessageSend n, Location statement)
	{
		this((Node) n, statement);
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
