package nullPointerAnalysis;

import math.Literal;

public class PossibleNullLiteral implements Literal<Domain>
{
	@Override
	public String toString()
	{
		return "don't know";
	}
}
