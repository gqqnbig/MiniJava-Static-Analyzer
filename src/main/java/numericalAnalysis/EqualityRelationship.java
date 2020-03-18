package numericalAnalysis;

public class EqualityRelationship extends math.EqualityRelationship<Interval>
{
	public String comment;

	public EqualityRelationship()
	{
	}

	public EqualityRelationship(Interval left, Interval right)
	{
		this.left = left;
		this.right = right;
	}

	public EqualityRelationship(Interval left, Interval right, String comment)
	{
		this.left = left;
		this.right = right;
		this.comment = comment;
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
