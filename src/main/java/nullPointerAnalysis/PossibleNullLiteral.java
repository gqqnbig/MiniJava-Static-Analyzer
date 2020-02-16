package nullPointerAnalysis;

import math.Literal;

public class PossibleNullLiteral implements Literal<AnalysisResult>, AnalysisResult
{
	@Override
	public String toString()
	{
		return "don't know";
	}
}
