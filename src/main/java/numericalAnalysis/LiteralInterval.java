package numericalAnalysis;

public class LiteralInterval implements Interval
{

	public int lowerBound;
	public int upperBound;

	public LiteralInterval(int lowerBound, int upperBound)
	{
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}


	@Override
	public String toString()
	{
		return "[" +
				(lowerBound == Integer.MIN_VALUE ? "MIN" : String.valueOf(lowerBound)) +
				", " +
				(upperBound == Integer.MAX_VALUE ? "MAX" : String.valueOf(upperBound)) +
				"]";
	}
}
