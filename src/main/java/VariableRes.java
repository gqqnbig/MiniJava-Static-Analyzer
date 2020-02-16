import syntaxtree.Expression;
import syntaxtree.Identifier;

public class VariableRes implements FlowSensitiveVariable
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

	public Expression getExpression()
	{
		return expression;
	}

	@Override
	public String toString()
	{
		return String.format("res[%s, %s]", expression, statement);
	}
}
