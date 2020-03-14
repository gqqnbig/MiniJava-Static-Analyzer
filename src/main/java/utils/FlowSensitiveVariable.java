package utils;

import math.Domain;
import math.Variable;

public interface FlowSensitiveVariable<TInput, TOutput extends Domain> extends Variable<TInput, TOutput>
{
	Location getStatement();
}
