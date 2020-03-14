package utils;

import math.Domain;
import math.Variable;

public interface ContextSensitiveVariable<TInput, TOutput extends Domain> extends Variable<TInput, TOutput>
{
	Location getCallSite();
}
