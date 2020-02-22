package nullPointerAnalysis;

import math.Literal;

public class NotNullLiteral implements Literal<AnalysisResult>, AnalysisResult
{
	@Override
	public String toString()
	{
		return "No NullPointerException";
	}
}
