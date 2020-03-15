package numericalAnalysis;

import utils.Location;
import utils.ObjectIdentifierDefinition;

public class VariableIn extends ConstraintVariable<IntIdentifierDefinition>
{

	public VariableIn(IntIdentifierDefinition input, Location statement, Location callSite)
	{
		super(input, statement, callSite);
	}


	@Override
	public String getFunctionName()
	{
		return "in";
	}
}
