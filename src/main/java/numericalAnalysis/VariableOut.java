package numericalAnalysis;

import utils.Location;

public class VariableOut extends ConstraintVariable<IntIdentifierDefinition>
{

	public VariableOut(IntIdentifierDefinition input, Location statement, Location callSite)
	{
		super(input, statement,  callSite);
	}


	@Override
	public String getFunctionName()
	{
		return "out";
	}

}
