package nullPointerAnalysis;

import utils.Location;

public class VariableOut extends FlowSensitiveNullPointerAnalysisVariable<ObjectIdentifierDefinition>
{

	public VariableOut(ObjectIdentifierDefinition input, Location statement)
	{
		super(input, statement);
	}


	@Override
	public String getFunctionName()
	{
		return "out";
	}

}
