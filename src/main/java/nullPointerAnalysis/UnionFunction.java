package nullPointerAnalysis;

import math.FunctionUnion;
import math.Literal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UnionFunction implements FunctionUnion<AnalysisResult>, AnalysisResult
{
	public static Literal<AnalysisResult> union(Literal<AnalysisResult> a, Literal<AnalysisResult> b)
	{
		if (a == PossibleNullLiteral.instance || b == PossibleNullLiteral.instance)
			return PossibleNullLiteral.instance;
		else if (a != null)
			return a;
		else
			return b;
	}

	private List<AnalysisResult> inputArray = new ArrayList<>();

	@Override
	public String getFunctionName()
	{
		return "Union";
	}

	@Override
	public List<AnalysisResult> getInput()
	{
		return inputArray;
	}


	public Literal<AnalysisResult> getReturnValue(Collection<EqualityRelationship> constraints)
	{
		if (inputArray.stream().anyMatch(e -> Solver.findLiteral(e, constraints) == PossibleNullLiteral.instance))
			return PossibleNullLiteral.instance;

		return null;
	}

	@Override
	public String toString()
	{
		if (inputArray.size() == 0)
			return "∅";

		StringBuilder sb = new StringBuilder();
		for (AnalysisResult element : inputArray)
			sb.append(element).append(" ∪ ");

		sb.delete(sb.length() - 2, sb.length());
		return sb.toString();
	}
}
