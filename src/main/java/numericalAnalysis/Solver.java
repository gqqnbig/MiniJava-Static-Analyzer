package numericalAnalysis;

import syntaxtree.Goal;

import java.io.PrintStream;

public class Solver
{
	public PrintStream debugOut;

	public boolean alwaysGreaterThan0(Goal goal)
	{
		ConstraintCollector constraintCollector = new ConstraintCollector();
		goal.accept(constraintCollector, null);
		debugOut.println("\nConstraints:");
		for (EqualityRelationship r : constraintCollector.constraints)
		{
			debugOut.println(r);
		}

		return true;
	}
}
