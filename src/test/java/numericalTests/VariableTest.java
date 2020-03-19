import baseVisitors.AllocationVisitor;
import numericalAnalysis.ProgramStructureCollector;
import org.junit.Assert;
import org.junit.Test;
import syntaxtree.Goal;
import typeAnalysis.ClassHierarchyAnalysis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class VariableTest
{
	@Test
	public void testCallSites() throws FileNotFoundException, ParseException
	{
		FileInputStream stream = new FileInputStream("testcases/hw3/CallsiteTest.java");
		try {MiniJavaParser.ReInit(stream);} catch (Throwable e) {new MiniJavaParser(stream);}
		Goal goal = MiniJavaParser.Goal();

		ProgramStructureCollector.init(goal);
		ClassHierarchyAnalysis.init(goal);
		//Initialize AllocationVisitor.usedClasses so that we can skip analyzing ununsed classes.
		goal.accept(new AllocationVisitor());

		NumericalAssertionChecker.VariableCollector variableCollector = new NumericalAssertionChecker.VariableCollector();
		goal.accept(variableCollector, null);

		Assert.assertTrue("res[1@4b952a2d, L17, L6] is missing", variableCollector.variables.stream().anyMatch(v -> v.getStatement().getLine() == 17 && v.getCallSite().getLine() == 6));

	}
}
