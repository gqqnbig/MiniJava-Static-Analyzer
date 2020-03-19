package numericalAnalysis;

import math.Variable;
import utils.Tuple;

public class PlusInterval implements Variable<Tuple<Interval, Interval>, Interval>, Interval
{
	private final Tuple<Interval, Interval> input;
	public Interval x;
	public Interval y;

	public PlusInterval(Interval x, Interval y)
	{
		input = new Tuple<>(x, y);
		this.x = x;
		this.y = y;
	}

	@Override
	public String getFunctionName()
	{
		return "PlusInterval";
	}

	@Override
	public Tuple<Interval, Interval> getInput()
	{
		return input;
	}

	@Override
	public String toString()
	{
		return x.toString() + " (+) " + y.toString();
	}
}
