package numericalAnalysis;

import math.FunctionUnion;
import math.Literal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UnionFunction implements FunctionUnion<Interval>, Interval
{
	public static Literal<Interval> union(Literal<Interval> a, Literal<Interval> b)
	{
		if (a instanceof LiteralInterval && b instanceof LiteralInterval)
			return new LiteralInterval(Math.min(((LiteralInterval) a).lowerBound, ((LiteralInterval) b).lowerBound), Math.max(((LiteralInterval) a).upperBound, ((LiteralInterval) b).upperBound));
		else
			throw new UnsupportedOperationException();
	}

	private List<Interval> inputArray = new ArrayList<>();

	@Override
	public String getFunctionName()
	{
		return "Union";
	}

	@Override
	public List<Interval> getInput()
	{
		return inputArray;
	}


//	public Literal<Interval> getReturnValue(Collection<EqualityRelationship> constraints)
//	{
//		if (inputArray.stream().anyMatch(e -> Solver.findLiteral(e, constraints) == PossibleNullLiteral.instance))
//			return PossibleNullLiteral.instance;
//
//		return null;
//	}

	@Override
	public String toString()
	{
		if (inputArray.size() == 0)
			return "∅";

		StringBuilder sb = new StringBuilder();
		for (Interval element : inputArray)
			sb.append(element).append(" ∪ ");

		sb.delete(sb.length() - 2, sb.length());
		return sb.toString();
	}
}
