package nullPointerAnalysis;

import baseVisitors.ArrayLengthVisitor;
import baseVisitors.ArrayLookupVisitor;
import baseVisitors.MessageSendCollector;
import math.Literal;
import math.Variable;
import syntaxtree.*;
import typeAnalysis.TypeService;
import utils.FlowSensitiveVariable;
import utils.Options;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

public class Solver
{
	public static TypeService typeService;

	/**
	 * if not found, return null.
	 *
	 * @param v
	 * @param constraints
	 * @return
	 */
	public static Literal<AnalysisResult> findLiteral(AnalysisResult v, Collection<EqualityRelationship> constraints)
	{
		Literal<AnalysisResult> literal = null;
		for (EqualityRelationship r : constraints)
		{
			if (r.left.equals(v) && r.right instanceof Literal<?>)
			{
				literal = UnionFunction.union(literal, (Literal<AnalysisResult>) r.right);
			}
		}
		return literal;
	}


	/**
	 * return true if the program may throw null pointer exception.
	 *
	 * @param goal
	 * @param debugOut
	 * @return
	 */
	public static boolean checkNullPointer(Goal goal, PrintStream debugOut)
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

		return checkNullPointerException(goal, solutions);
	}

	public static boolean checkNullPointerException(Goal goal, List<EqualityRelationship> solutions)
	{
		MessageSendCollector messageSendCollector = new MessageSendCollector();
		goal.accept(messageSendCollector);

		solutions = solutions.stream().filter(r -> r.left instanceof VariableRes).collect(Collectors.toList());
		for (MessageSend ms : messageSendCollector.messageSends)
		{
			if (solutions.stream().anyMatch(r -> ((VariableRes) r.left).getExpression() == ms.f0.f0.choice && r.right == PossibleNullLiteral.instance))
			{
				return true;
			}
		}

		ArrayLookupVisitor arrayLookupVisitor = new ArrayLookupVisitor();
		goal.accept(arrayLookupVisitor);
		for (ArrayLookup al : arrayLookupVisitor.arrayLookups)
		{
			Node divedNode = VariableRes.diveInto(al.f0);
			if (solutions.stream().anyMatch(r -> ((VariableRes) r.left).getExpression() == divedNode && r.right == PossibleNullLiteral.instance))
			{
				return true;
			}
		}

		ArrayLengthVisitor arrayLengthVisitor = new ArrayLengthVisitor();
		goal.accept(arrayLengthVisitor);

		for (ArrayLength al : arrayLengthVisitor.arrayLengths)
		{
			Node divedNode = VariableRes.diveInto(al.f0);
			if (solutions.stream().anyMatch(r -> ((VariableRes) r.left).getExpression() == divedNode && r.right == PossibleNullLiteral.instance))
			{
				return true;
			}
		}
		return false;
	}

	private static void clearUpSingleUnion(Collection<EqualityRelationship> constraints)
	{
		for (EqualityRelationship r : constraints)
		{
			if (r.right instanceof UnionFunction && ((UnionFunction) r.right).getInput().size() < 2)
			{
				List<AnalysisResult> input = ((UnionFunction) r.right).getInput();
				assert input.size() != 0;
				r.right = input.get(0);
			}
		}
	}

	/**
	 * Return literal solution of all variables, ie. VariableIn, VariableOut, VariableRes.
	 *
	 * @param constraints
	 * @return
	 */
	public static ArrayList<EqualityRelationship> solve(List<EqualityRelationship> constraints)
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

				Literal<AnalysisResult> rightL = null;
				if (r.right instanceof FlowSensitiveNullPointerAnalysisVariable)
					rightL = ((FlowSensitiveNullPointerAnalysisVariable) r.right).getReturnValue(constraints);
				else if (r.right instanceof UnionFunction)
					rightL = ((UnionFunction) r.right).getReturnValue(constraints);

				Literal<AnalysisResult> leftL = null;
				if (r.left instanceof FlowSensitiveNullPointerAnalysisVariable)
					leftL = ((FlowSensitiveNullPointerAnalysisVariable) r.left).getReturnValue(constraints);
				else if (r.left instanceof UnionFunction)
					leftL = ((UnionFunction) r.left).getReturnValue(constraints);

				if (leftL == null && rightL != null)
				{
					EqualityRelationship newEquality = new EqualityRelationship(r.left, (AnalysisResult) rightL);
					hasChange = workingset.add(newEquality) || hasChange;
				}
				else if (leftL != null && rightL == null)
				{
					EqualityRelationship newEquality = new EqualityRelationship(r.right, (AnalysisResult) leftL);
					hasChange = workingset.add(newEquality) || hasChange;
				}
				else if (leftL != null && rightL != null)
				{
					Literal<AnalysisResult> l = UnionFunction.union(leftL, rightL);
					hasChange = workingset.add(new EqualityRelationship(r.left, (AnalysisResult) l)) || hasChange;
					hasChange = workingset.add(new EqualityRelationship(r.right, (AnalysisResult) l)) || hasChange;
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

		if(Options.isDebug)
			solutions.sort(new Comparator<EqualityRelationship>()
			{
				@Override
				public int compare(EqualityRelationship o1, EqualityRelationship o2)
				{
					return ((FlowSensitiveVariable)o1.left).getStatement().compareTo(((FlowSensitiveVariable)o2.left).getStatement());
				}
			});

		return solutions;

	}
}
