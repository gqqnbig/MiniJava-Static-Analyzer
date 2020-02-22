package nullPointerAnalysis;

import math.Literal;

public class PossibleNullLiteral implements Literal<AnalysisResult>, AnalysisResult
{
	public static final PossibleNullLiteral instance = getInstance();

	private static PossibleNullLiteral getInstance()
	{
		return new PossibleNullLiteral();
	}

	private PossibleNullLiteral() {}


	@Override
	public String toString()
	{
		return "don't know";
	}
}
