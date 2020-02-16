package utils;

import math.Variable;
import nullPointerAnalysis.AnalysisResult;

public interface FlowSensitiveVariable<TInput, TOutput extends AnalysisResult> extends Variable<TInput, TOutput>
{
	Location getStatement();
}
