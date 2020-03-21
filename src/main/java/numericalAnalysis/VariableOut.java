package numericalAnalysis;

import math.EqualityRelationship;
import math.EquationSolver;
import math.Literal;
import utils.Location;

import java.util.Collection;

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


	@Override
	public <ER extends EqualityRelationship<Interval>> Literal<Interval> reduce(Collection<ER> constraints, EquationSolver<Interval> solver)
	{
		return null;
	}

}
