package nullPointerAnalysis;

import math.Literal;
import utils.Options;

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
		if (Options.shortform)
			return "N";
		else
			return "No NullPointerException";
	}
}
