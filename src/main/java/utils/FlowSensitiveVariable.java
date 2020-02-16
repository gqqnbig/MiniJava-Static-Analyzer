package utils;

import math.Variable;

public interface FlowSensitiveVariable<TInput,TOutput> extends Variable<TInput,TOutput>
{
	Location getStatement();
}
