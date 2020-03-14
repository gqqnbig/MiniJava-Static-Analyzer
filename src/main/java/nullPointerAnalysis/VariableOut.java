package nullPointerAnalysis;

import utils.Location;
import utils.ObjectIdentifierDefinition;

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
