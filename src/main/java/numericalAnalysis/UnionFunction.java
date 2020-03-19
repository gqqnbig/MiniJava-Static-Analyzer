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
		if (a != null && b != null)
			return new LiteralInterval(Math.min(((LiteralInterval) a).lowerBound, ((LiteralInterval) b).lowerBound), Math.max(((LiteralInterval) a).upperBound, ((LiteralInterval) b).upperBound));
		else if (a != null)
			return a;
		else
			return b;
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


	public Literal<Interval> getReturnValue(Collection<EqualityRelationship> constraints, Solver solver)
	{
		Literal<Interval> result = null;
		for (Interval element : inputArray)
		{
			Literal<Interval> l = solver.findLiteral(element, constraints);
			if (l == null)
				return null;
			if (result == null)
				result = l;
			else
				result = union(result, l);
		}

		return result;
	}

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
//
//	@Override
//	public Literal<Interval> unionHelper(Literal<Interval> a, Literal<Interval> b)
//	{
//		return UnionFunction.union(a,b);
//	}
}
