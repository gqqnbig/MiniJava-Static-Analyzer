import math.Variable;
import nullPointerAnalysis.*;
import org.junit.Assert;
import org.junit.Test;
import syntaxtree.Goal;
import typeAnalysis.ClassHierarchyAnalysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VariablesTest
{
	@Test
	public void testVariableNumbers() throws ParseException, FileNotFoundException
	{
		FileInputStream stream = new FileInputStream("testcases/hw2/pair5/NullCheck.java");
		try {new MiniJavaParser(stream);} catch (Throwable e) {MiniJavaParser.ReInit(stream);}
		Goal goal = MiniJavaParser.Goal();

		ProgramStructureCollector.init(goal);
		ClassHierarchyAnalysis.init(goal);
		VariableCollector variableCollector = new VariableCollector();
		goal.accept(variableCollector, null);

//		Assert.assertEquals(21, variableCollector.variables.stream().filter(v -> {
//			Object input = v.getInput();
//			if (input instanceof ObjectIdentifierDefinition)
//				return (Objects.equals(((ObjectIdentifierDefinition) input).Method, "main") && ((ObjectIdentifierDefinition) input).getIsParameter()) == false;
//			else
//				return true;
//		}).count());


		ConstraintCollector constraintCollector = new ConstraintCollector();
		goal.accept(constraintCollector, null);

		//don't know how to deal with "new B()" as expression or as allocation expression.
//		Assert.assertEquals("Expect only one res[new B(), n] in the program.",
//				1, variableCollector.variables.stream().filter(v -> v instanceof VariableRes && ((VariableRes)v).getInput().contains("new B()")).count());

		for (EqualityRelationship r : constraintCollector.constraints)
		{
			if (r.left instanceof VariableIn || r.left instanceof VariableOut || r.left instanceof VariableRes)
				Assert.assertTrue(r.left + " is not a previously captured variable.", variableCollector.variables.contains(r.left));
			if (r.right instanceof VariableIn || r.right instanceof VariableOut || r.right instanceof VariableRes)
				Assert.assertTrue(r.right + " is not a previously captured variable.", variableCollector.variables.contains(r.right));


			Assert.assertTrue(r + " is invalid", r.left.equals(r.right) == false);

		}
	}

	@Test
	public void testPair8() throws ParseException, FileNotFoundException
	{
		Path currentRelativePath = Paths.get("testcases/hw2/pair8.java");
		String s = currentRelativePath.toAbsolutePath().toString();
		File f=new File(s);
		System.out.println(f.exists());



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
