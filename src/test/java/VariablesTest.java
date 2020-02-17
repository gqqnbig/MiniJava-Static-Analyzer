import math.Variable;
import nullPointerAnalysis.EqualityRelationship;
import nullPointerAnalysis.ProgramStructureCollector;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Assert;
import org.junit.Test;
import syntaxtree.Goal;
import typeAnalysis.ClassHierarchyAnalysis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class VariablesTest
{
	@Test
	public void testVariableNumbers() throws ParseException, FileNotFoundException
	{
		FileInputStream stream = new FileInputStream("testcases/hw2/pair5/NullCheck.java");
		new MiniJavaParser(stream);
		Goal goal = MiniJavaParser.Goal();

		ProgramStructureCollector.init(goal);
		ClassHierarchyAnalysis.init(goal);
		VariableCollector variableCollector = new VariableCollector();
		goal.accept(variableCollector, null);

		Assert.assertEquals(21, variableCollector.variables.size());


		ConstraintCollector constraintCollector = new ConstraintCollector();
		goal.accept(constraintCollector, null);
		for (EqualityRelationship r : constraintCollector.constraints)
		{
			if (r.left instanceof Variable)
				Assert.assertTrue(r.left + " is not a previously captured variable.", variableCollector.variables.contains(r.left));
			if (r.right instanceof Variable)
				Assert.assertTrue(r.right + " is not a previously captured variable.", variableCollector.variables.contains(r.right));


			Assert.assertTrue(r + " is invalid", r.left.equals(r.right) == false);

		}
	}

	@Test
	public void testPair8() throws ParseException, FileNotFoundException
	{
		FileInputStream stream = new FileInputStream("testcases/hw2/pair8.java");
		MiniJavaParser.ReInit(stream);
//		new MiniJavaParser(stream);
		Goal goal = MiniJavaParser.Goal();

		ProgramStructureCollector.init(goal);
		ClassHierarchyAnalysis.init(goal);
		VariableCollector variableCollector = new VariableCollector();
		goal.accept(variableCollector, null);

//		Assert.assertEquals(21, variableCollector.variables.size());


		ConstraintCollector constraintCollector = new ConstraintCollector();
		goal.accept(constraintCollector, null);
		for (EqualityRelationship r : constraintCollector.constraints)
		{
			//This test case has Union operation, which is not a variable.
			if (r.left instanceof Variable)
			{
				Assert.assertNotNull("Variable input cannot be null.", ((Variable) r.left).getInput());
//				Assert.assertTrue(r.left + " is not a previously captured variable.", variableCollector.variables.contains(r.left));
			}
			if (r.right instanceof Variable)
			{
				Assert.assertNotNull("Variable input cannot be null.", ((Variable) r.right).getInput());
//				Assert.assertTrue(r.right + " is not a previously captured variable.", variableCollector.variables.contains(r.right));
			}


			Assert.assertTrue(r + " is invalid", r.left.equals(r.right) == false);

		}
	}
}
