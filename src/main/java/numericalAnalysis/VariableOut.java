package numericalAnalysis;

import utils.Location;
import utils.ObjectIdentifierDefinition;

public class VariableOut extends ConstraintVariable<ObjectIdentifierDefinition>
{

	public VariableOut(ObjectIdentifierDefinition input, Location statement, Location callSite)
	{
		super(input, statement,  callSite);
	}


	@Override
	public String getFunctionName()
	{
		return "out";
	}

}
