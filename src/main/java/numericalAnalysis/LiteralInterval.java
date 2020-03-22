package numericalAnalysis;

import math.Literal;

import java.util.Objects;

public class LiteralInterval implements Literal<Interval>, Interval
{
	public static final LiteralInterval NONE = new LiteralInterval();

	/**
	 * wrap an overflow or underflow integer.
	 *
	 * @param n
	 * @return
	 */
	public static int wrap(long n)
	{
		long intRange = ((long) Integer.MAX_VALUE - Integer.MIN_VALUE);

		if (n < Integer.MIN_VALUE)
		{
			long r = (Integer.MIN_VALUE - n) % intRange;
			if (r == 0)
				return Integer.MIN_VALUE;
			else
				return (int) (Integer.MAX_VALUE - (r - 1));
		}
		if (n > Integer.MAX_VALUE)
		{
			long r = (n - Integer.MAX_VALUE) % intRange;
			if (r == 0)
				return Integer.MAX_VALUE;
			else
				return (int) (Integer.MIN_VALUE + r - 1);
		}
		else
			return (int) n;
	}

	public long lowerBound;//long to store 2147483648, -minInt
	public long upperBound;

	public LiteralInterval(long lowerBound, long upperBound)
	{
		assert lowerBound <= upperBound : String.format("%1$d <= %2$d failed.", lowerBound, upperBound);

		this.upperBound = (int) upperBound;
		this.lowerBound = (int) lowerBound;

		if (this.upperBound < this.lowerBound)
		{
			this.lowerBound = Integer.MIN_VALUE;
			this.upperBound = Integer.MAX_VALUE;
		}

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

	public boolean isIn(Integer n)
	{
		return lowerBound <= n && n <= upperBound;
	}
}
