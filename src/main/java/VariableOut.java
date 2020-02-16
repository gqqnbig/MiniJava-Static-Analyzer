import syntaxtree.Identifier;

public class VariableOut implements FlowSensitiveVariable
{
	private final Identifier identifier;
	private Location statement;

	public VariableOut(Identifier identifier, Location statement)
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
		return "out[" + identifier.f0.toString() + ", " + statement + "]";
	}
}
