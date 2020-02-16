package nullPointerAnalysis;

import utils.FlowSensitiveVariable;
import utils.Location;
import math.Variable;

public abstract class FlowSensitiveNullPointerAnalysisVariable implements Variable<NullableIdentifierDefinition, AnalysisResult>, FlowSensitiveVariable<NullableIdentifierDefinition, AnalysisResult>, AnalysisResult
{
	protected Location statement;
	NullableIdentifierDefinition input;

	public FlowSensitiveNullPointerAnalysisVariable(NullableIdentifierDefinition input, Location statement)
	{
		this.input = input;
		this.statement = statement;
	}

	@Override
	public final Location getStatement()
	{
		return statement;
	}

	@Override
	public abstract String getFunctionName();

	@Override
	public final NullableIdentifierDefinition getInput()
	{
		return input;
	}

	@Override
	public String toString()
	{
		return String.format("%s[%s, %s]", getFunctionName(), input, getStatement());
	}
}


