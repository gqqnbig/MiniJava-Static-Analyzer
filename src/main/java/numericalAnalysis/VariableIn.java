package numericalAnalysis;

import utils.Location;
import utils.ObjectIdentifierDefinition;

public class VariableIn extends ConstraintVariable<ObjectIdentifierDefinition>
{

	public VariableIn(ObjectIdentifierDefinition input, Location statement, Location callSite)
	{
		super(input, statement, callSite);
	}


	@Override
	public String getFunctionName()
	{
		return "in";
	}
}
