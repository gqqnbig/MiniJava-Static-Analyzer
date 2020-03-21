package numericalAnalysis;

import baseVisitors.ExpressionToStringVisitor;
import math.EqualityRelationship;
import math.EquationSolver;
import math.Literal;
import syntaxtree.*;
import utils.Location;

import java.util.Collection;

public class VariableRes extends ConstraintVariable<String>
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


	private VariableRes(Node expressionChoise, Location statement, Location callSite)
	{

		super(expressionChoise.accept(new ExpressionToStringVisitor(), null) + "@" + Integer.toHexString(expressionChoise.hashCode()), statement, callSite);
		assert (expressionChoise instanceof Expression == false);
		assert (expressionChoise instanceof PrimaryExpression == false);

		expression = expressionChoise;
	}

	/**
	 * actually stores expression.f0.choice
	 *
	 * @param statement
	 */
	public VariableRes(Expression expression, Location statement, Location callSite)
	{
		this(diveInto(expression), statement, callSite);
	}

	/**
	 * actually stores expression.f0.choice
	 *
	 * @param statement
	 */
	public VariableRes(PrimaryExpression expression, Location statement, Location callSite)
	{
		this(diveInto(expression), statement, callSite);
	}

//	public VariableRes(BracketExpression n, Location statement)
//	{
//		this((Node) n, statement);
//	}

	public VariableRes(AllocationExpression n, Location statement, Location callSite)
	{
		this((Node) n, statement, callSite);
	}

	public VariableRes(MessageSend n, Location statement, Location callSite)
	{
		this((Node) n, statement, callSite);
	}

	public VariableRes(IntegerLiteral n, Location statement, Location callSite)
	{
		this((Node) n, statement, callSite);
	}

	public VariableRes(PlusExpression n, Location statement, Location callSite)
	{
		this((Node) n, statement, callSite);
	}

	public VariableRes(MinusExpression n, Location statement, Location callSite)
	{
		this((Node) n, statement, callSite);
	}



	@Override
	public String getFunctionName()
	{
		return "res";
	}

	@Override
	public <ER extends EqualityRelationship<Interval>> Literal<Interval> reduce(Collection<ER> constraints, EquationSolver<Interval> solver)
	{
		return null;
	}

	public Node getExpression()
	{
		return expression;
	}
}
