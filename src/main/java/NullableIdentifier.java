import syntaxtree.Identifier;
import utils.Scope;

public class NullableIdentifier
{
	Scope scope;
	String identifierName;

	public NullableIdentifier(Identifier f1)
	{
		identifierName=f1.f0.toString();
	}

	@Override
	public String toString()
	{
		return identifierName+" as defined in "+ scope.getClass();
	}
}
