package nullPointerAnalysis;

import utils.Location;

public class VariableIn extends FlowSensitiveNullPointerAnalysisVariable<ObjectIdentifierDefinition>
{

	public VariableIn(ObjectIdentifierDefinition input, Location statement)
	{
		super(input, statement);
	}


	@Override
	public String getFunctionName()
	{
		return "in";
	}
}
