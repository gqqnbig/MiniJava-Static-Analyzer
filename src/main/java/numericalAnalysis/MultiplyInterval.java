package numericalAnalysis;

import math.EqualityRelationship;
import math.EquationSolver;
import math.Literal;
import math.Variable;
import utils.Tuple;

import java.util.Collection;

public class MultiplyInterval implements Variable<Tuple<Interval, Interval>, Interval>, Interval
{
	public static Literal<Interval> multiply(LiteralInterval a, LiteralInterval b)
	{
		if (a.lowerBound > 0 && b.lowerBound > 0 ||
				a.upperBound < 0 && b.upperBound < 0)
			return new LiteralInterval(a.lowerBound * b.lowerBound, a.upperBound * b.upperBound);
		else if (a.isIn(0) || b.isIn(0))
			return new LiteralInterval(Math.min(a.lowerBound * b.upperBound, a.upperBound * b.lowerBound), Math.max(a.lowerBound * b.lowerBound, a.upperBound * b.upperBound));
		else
			return new LiteralInterval(a.upperBound * b.upperBound, a.lowerBound * b.lowerBound);
	}

	private final Tuple<Interval, Interval> input;
	public Interval x;
	public Interval y;

	public MultiplyInterval(Interval x, Interval y)
	{
		input = new Tuple<>(x, y);
		this.x = x;
		this.y = y;
	}

	@Override
	public String getFunctionName()
	{
		return "MultiplyInterval";
	}

	@Override
	public Tuple<Interval, Interval> getInput()
	{
		return input;
	}

	@Override
	public <ER extends EqualityRelationship<Interval>> Literal<Interval> reduce(Collection<ER> constraints, EquationSolver<Interval> solver)
	{
		LiteralInterval lx = (LiteralInterval) solver.findLiteral(x, constraints);
		if (lx == null)
			return null;

		LiteralInterval ly = (LiteralInterval) solver.findLiteral(y, constraints);
		if (ly == null)
			return null;

		return multiply(lx, ly);
	}

	@Override
	public String toString()
	{
		return x.toString() + " (-) " + y.toString();
	}
}