package nullPointerAnalysis;

import math.FunctionUnion;
import math.Literal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class UnionFunction implements FunctionUnion<AnalysisResult>, AnalysisResult
{
	public static Literal<AnalysisResult> union(Literal<AnalysisResult> a, Literal<AnalysisResult> b)
	{
		if (a == PossibleNullLiteral.instance || b == PossibleNullLiteral.instance)
			return PossibleNullLiteral.instance;
		else
			return a;
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

	@Override
	public String toString()
	{
		if (inputArray.size() == 0)
			return "∅";

		StringBuilder sb = new StringBuilder();
		for (var element : inputArray)
			sb.append(element).append(" ∪ ");

		sb.delete(sb.length() - 2, sb.length());
		return sb.toString();
	}
}
