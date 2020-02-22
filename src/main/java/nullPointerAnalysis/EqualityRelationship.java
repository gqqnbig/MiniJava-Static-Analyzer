package nullPointerAnalysis;

public class EqualityRelationship extends math.EqualityRelationship<AnalysisResult>
{
	public String comment;

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
		String s;
		if (comment == null)
			s = "";
		else
			s = String.format("%3s, ", comment);
		return s + left.toString() + " = " + right.toString();
	}


}
