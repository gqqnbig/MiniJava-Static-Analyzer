package numericalAnalysis;

import math.EquationSolver;
import math.Literal;
import math.Variable;
import syntaxtree.Goal;
import utils.FlowSensitiveVariable;
import utils.Options;

import java.io.PrintStream;
import java.util.*;

public class Solver extends EquationSolver<Interval>
{
	public PrintStream debugOut;

	public List<EqualityRelationship> solve(Goal goal)
	{
		ConstraintCollector constraintCollector = new ConstraintCollector();
		goal.accept(constraintCollector, null);
		debugOut.println("\nConstraints:");
		for (EqualityRelationship r : constraintCollector.constraints)
		{
			debugOut.println(r);
		}


		List<EqualityRelationship> solutions = solve(constraintCollector.constraints);
		debugOut.println("\nSolutions:");
		for (EqualityRelationship r : solutions)
		{
			debugOut.println(r);
		}

		return solutions;
	}


	/**
	 * Return literal solution of all variables, ie. VariableIn, VariableOut, VariableRes.
	 *
	 * @param constraints
	 * @return
	 */
	ArrayList<EqualityRelationship> solve(List<EqualityRelationship> constraints)
	{
		//Clear up single union
		clearUpSingleUnion(constraints);
		HashSet<EqualityRelationship> workingset = new HashSet<>(constraints);

		boolean hasChange;
		do
		{
			hasChange = false;

			constraints = new ArrayList<>(workingset);
			for (int i = 0; i < constraints.size(); i++)
			{
				EqualityRelationship r = constraints.get(i);
				if (r.right instanceof Literal)
					continue;

				Literal<Interval> rightL = getReturnValue(constraints, r.right);
				Literal<Interval> leftL = getReturnValue(constraints, r.left);

				if (leftL == null && rightL != null)
				{
					EqualityRelationship newEquality = new EqualityRelationship(r.left, (Interval) rightL);
					hasChange = workingset.add(newEquality) || hasChange;
				}
				else if (leftL != null && rightL == null)
				{
					EqualityRelationship newEquality = new EqualityRelationship(r.right, (Interval) leftL);
					hasChange = workingset.add(newEquality) || hasChange;
				}
				else if (leftL != null && rightL != null)
				{
					Literal<Interval> l = UnionFunction.union(leftL, rightL);
					hasChange = workingset.add(new EqualityRelationship(r.left, (Interval) l)) || hasChange;
					hasChange = workingset.add(new EqualityRelationship(r.right, (Interval) l)) || hasChange;
				}
			}
		}
		while (hasChange);


		ArrayList<EqualityRelationship> solutions = new ArrayList<>();
		for (EqualityRelationship r : workingset)
		{
			if (r.left instanceof VariableRes && r.right instanceof Literal)
				solutions.add(r);
			if (r.left instanceof VariableOut && r.right instanceof Literal)
				solutions.add(r);
			if (r.left instanceof VariableIn && r.right instanceof Literal)
				solutions.add(r);
		}

		if (Options.isDebug)
			solutions.sort(new Comparator<EqualityRelationship>()
			{
				@Override
				public int compare(EqualityRelationship o1, EqualityRelationship o2)
				{
					return ((FlowSensitiveVariable) o1.left).getStatement().compareTo(((FlowSensitiveVariable) o2.left).getStatement());
				}
			});

		return solutions;

	}

	private Literal<Interval> getReturnValue(List<EqualityRelationship> constraints, Interval interval)
	{
		if(interval instanceof Variable)
			 return ((Variable) interval).reduce(constraints,this);
		else
			return null;
	}

	@Override
	protected Literal<Interval> union(Literal<Interval> a, Literal<Interval> b)
	{
		return UnionFunction.union(a, b);
	}
}
