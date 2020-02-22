package nullPointerAnalysis;

import math.Literal;

public class NotNullLiteral implements Literal<AnalysisResult>, AnalysisResult
{
	public static final NotNullLiteral instance = getInstance();

	private static NotNullLiteral getInstance()
	{
		return new NotNullLiteral();
	}

	private NotNullLiteral() {}

	@Override
	public String toString()
	{
		return "No NullPointerException";
	}
}
