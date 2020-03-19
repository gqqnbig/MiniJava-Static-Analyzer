package numericalAnalysis;

import math.Literal;

public class LiteralInterval implements Literal<Interval>, Interval
{
	public static final LiteralInterval NONE = new LiteralInterval();

	public int lowerBound;
	public int upperBound;

	public LiteralInterval(int lowerBound, int upperBound)
	{
		assert lowerBound <= upperBound;

		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	/**
	 * to construct a NONE instance.
	 */
	private LiteralInterval()
	{
		this.lowerBound = Integer.MAX_VALUE;
		this.upperBound = Integer.MIN_VALUE;
	}

	@Override
	public String toString()
	{
		if (lowerBound <= upperBound)
			return "[" +
					(lowerBound == Integer.MIN_VALUE ? "MIN" : String.valueOf(lowerBound)) +
					", " +
					(upperBound == Integer.MAX_VALUE ? "MAX" : String.valueOf(upperBound)) +
					"]";
		else
			return "NONE";
	}
}
