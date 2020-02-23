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

	protected Literal<AnalysisResult> findLiteral(AnalysisResult v, Collection<EqualityRelationship> constraints)
	{
		Literal<AnalysisResult> literal = NotNullLiteral.instance;
		for (EqualityRelationship r : constraints)
		{
			if (r.left.equals(v) && r.right instanceof Literal<?>)
			{
				literal = union(literal, (Literal<AnalysisResult>) r.right);
			}
		}
		return literal;
	}

	public AnalysisResult getReturnValue(Collection<EqualityRelationship> constraints)
	{
		if (inputArray.stream().anyMatch(e -> findLiteral(e, constraints) == PossibleNullLiteral.instance))
			return PossibleNullLiteral.instance;

		return this;
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
