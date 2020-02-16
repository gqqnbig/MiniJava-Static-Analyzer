package nullPointerAnalysis;

public class EqualityRelationship extends math.EqualityRelationship<AnalysisResult>
{
	@Override
	public String toString()
	{
		return left.toString() + " = " + right.toString();
	}
}
