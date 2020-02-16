import syntaxtree.Expression;
import syntaxtree.Identifier;

public class VariableRes implements FlowSensitiveVariable<Expression>
{
	private final Expression expression;
	private Location statement;

	public VariableRes(Expression expression, Location statement)
	{
		this.expression = expression;
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
		return String.format("res[%s, %s]", expression, statement);
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
