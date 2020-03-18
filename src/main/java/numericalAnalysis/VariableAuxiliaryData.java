package numericalAnalysis;

import utils.Location;

public class VariableAuxiliaryData
{
	public Location statement;
	public Location callSite;

	public VariableAuxiliaryData(Location statement, Location callSite)
	{
		this.statement = statement;
		this.callSite = callSite;
	}
}
