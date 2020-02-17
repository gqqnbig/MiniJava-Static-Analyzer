package nullPointerAnalysis;

public class EqualityRelationship extends math.EqualityRelationship<AnalysisResult>
{
	public EqualityRelationship()
	{
	}

	public EqualityRelationship(AnalysisResult left, AnalysisResult right)
	{
		this.left = left;
		this.right = right;
	}

	@Override
	public String toString()
	{
		return left.toString() + " = " + right.toString();
	}
}
