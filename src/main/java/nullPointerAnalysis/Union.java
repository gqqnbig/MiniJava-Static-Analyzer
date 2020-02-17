package nullPointerAnalysis;

import math.FunctionUnion;

import java.util.ArrayList;
import java.util.List;

public class Union implements FunctionUnion<AnalysisResult>, AnalysisResult
{
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
