package utils;

import utils.Variable;

public interface FlowSensitiveVariable<TInput> extends Variable<TInput>
{
	Location getStatement();
}
