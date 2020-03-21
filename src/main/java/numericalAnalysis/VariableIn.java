package numericalAnalysis;

import math.EqualityRelationship;
import math.EquationSolver;
import math.Literal;
import utils.Location;
import utils.ObjectIdentifierDefinition;

import java.util.Collection;

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


	@Override
	public <ER extends EqualityRelationship<Interval>> Literal<Interval> reduce(Collection<ER> constraints, EquationSolver<Interval> solver)
	{
		return null;
	}
}
