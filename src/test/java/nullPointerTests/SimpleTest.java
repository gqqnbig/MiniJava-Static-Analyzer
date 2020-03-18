package nullPointerTests;

import baseVisitors.AllocationVisitor;
import baseVisitors.MessageSendCollector;
import nullPointerAnalysis.*;
import org.junit.Assert;
import org.junit.Test;
import syntaxtree.Goal;
import syntaxtree.MessageSend;
import typeAnalysis.ClassHierarchyAnalysis;
import nullPointerAnalysis.Solver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleTest
{
	@Test
	public void test() throws FileNotFoundException, ParseException
	{
		FileInputStream stream = new FileInputStream("testcases/hw2/NullCheckSimple.java");
		try {MiniJavaParser.ReInit(stream);} catch (Throwable e) {new MiniJavaParser(stream);}
		Goal goal = MiniJavaParser.Goal();

		ProgramStructureCollector.init(goal);
		ClassHierarchyAnalysis.init(goal);
		goal.accept(new AllocationVisitor());

		VariableCollector variableCollector = new VariableCollector();
		goal.accept(variableCollector, null);

		ConstraintCollector constraintCollector = new ConstraintCollector();
		goal.accept(constraintCollector, null);


		for (EqualityRelationship r : constraintCollector.constraints)
		{
			if (r.left instanceof VariableIn || r.left instanceof VariableOut || r.left instanceof VariableRes)
				Assert.assertTrue(r.left + " is not a previously captured variable.", variableCollector.variables.contains(r.left));
			if (r.right instanceof VariableIn || r.right instanceof VariableOut || r.right instanceof VariableRes)
				Assert.assertTrue(r.right + " is not a previously captured variable.", variableCollector.variables.contains(r.right));


			Assert.assertNotEquals(r + " is invalid", r.left, r.right);
		}

		Assert.assertTrue("res[f,L10]=in[x,L10] is missing.",
				constraintCollector.constraints.stream().anyMatch(c -> c.left instanceof VariableRes && ((VariableRes) c.left).getInput().startsWith("f@")));

		Assert.assertTrue("in[f, L10]=Don't know is missing",
				constraintCollector.constraints.stream().anyMatch(c -> c.left instanceof VariableIn && ((VariableIn) c.left).getInput().getIdentifier().equals("f") && ((VariableIn) c.left).getStatement().getLine() == 10 &&
						c.right == PossibleNullLiteral.instance));


		List<EqualityRelationship> solutions = Solver.solve(constraintCollector.constraints);

		solutions = solutions.stream().filter(r -> r.left instanceof VariableRes).collect(Collectors.toList());
		Assert.assertTrue("f.id() throws null pointer exception.", Solver.checkNullPointerException(goal, solutions));
	}
}
