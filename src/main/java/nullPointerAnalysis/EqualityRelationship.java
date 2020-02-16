package nullPointerAnalysis;

public class EqualityRelationship extends math.EqualityRelationship<NullableIdentifierDefinition, Domain>
{
	@Override
	public String toString()
	{
		return left.toString() + " = " + right.toString();
	}
}
