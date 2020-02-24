package nullPointerAnalysis;

import math.Literal;
import utils.Options;

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
		if (Options.shortform)
			return "M";
		else
			return "May throw exception";
	}
}
