package numericalAnalysis;

import math.Literal;

import java.util.Objects;

public class LiteralInterval implements Literal<Interval>, Interval
{
	public static final LiteralInterval NONE = new LiteralInterval();

	public long lowerBound;//long to store 2147483648, -minInt
	public long upperBound;

	public LiteralInterval(long lowerBound, long upperBound)
	{
		assert lowerBound <= upperBound : String.format("%1$d <= %2$d failed.", lowerBound, upperBound);

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

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LiteralInterval that = (LiteralInterval) o;
		return lowerBound == that.lowerBound &&
				upperBound == that.upperBound;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(lowerBound, upperBound);
	}
}
