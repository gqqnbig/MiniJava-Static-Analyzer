import baseVisitors.AllocationVisitor;
import numericalAnalysis.*;
import org.junit.Assert;
import org.junit.Test;
import syntaxtree.Goal;
import syntaxtree.IntegerLiteral;
import typeAnalysis.ClassHierarchyAnalysis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ConstraintTest
{
	@Test
	public void testC11() throws ParseException, FileNotFoundException
	{
		FileInputStream stream = new FileInputStream("testcases/hw3/CallsiteTest.java");
		try {MiniJavaParser.ReInit(stream);} catch (Throwable e) {new MiniJavaParser(stream);}
		Goal goal = MiniJavaParser.Goal();

		ProgramStructureCollector.init(goal);
		ClassHierarchyAnalysis.init(goal);
		//Initialize AllocationVisitor.usedClasses so that we can skip analyzing ununsed classes.
		goal.accept(new AllocationVisitor());

		ConstraintCollector constraintCollector = new ConstraintCollector();
		goal.accept(constraintCollector, null);

		var c11 = constraintCollector.constraints.stream().filter(r -> "C11".equals(r.comment) && ((UnionFunction) r.right).getInput().stream().anyMatch(res -> ((VariableRes) res).getInput().startsWith("null@"))).findFirst();
		if (c11.isPresent())
			Assert.fail("The right hand side of C11 should not have null, but we find " + c11.get().toString());

		Assert.assertTrue("C12 res[1, n, cs] = [1, 1] is missing.", constraintCollector.constraints.stream().anyMatch(r -> "C12".equals(r.comment) && ((LiteralInterval) r.right).lowerBound == 1 && ((LiteralInterval) r.right).upperBound == 1));

	}
}
