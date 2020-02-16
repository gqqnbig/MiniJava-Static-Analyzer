import syntaxtree.Identifier;

public class VariableIn implements FlowSensitiveVariable
{
	private final Identifier identifier;
	private Location statement;

	public VariableIn(Identifier identifier, Location statement)
	{
		this.identifier = identifier;
		this.statement = statement;
	}


	@Override
	public Location getStatement()
	{
		return statement;
	}

	public Identifier getIdentifier()
	{
		return identifier;
	}

	@Override
	public String toString()
	{
		return "in[" + identifier.f0.toString() + ", " + statement + "]";
	}
}
