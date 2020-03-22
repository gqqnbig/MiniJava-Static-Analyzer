import baseVisitors.AllocationVisitor;
import numericalAnalysis.*;
import org.junit.Assert;
import org.junit.Test;
import syntaxtree.Goal;
import typeAnalysis.ClassHierarchyAnalysis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;

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

	@Test
	public void testCallSitesColumn() throws FileNotFoundException, ParseException
	{
		FileInputStream stream = new FileInputStream("testcases/hw3/CallSiteColumnTest.java");
		try {MiniJavaParser.ReInit(stream);} catch (Throwable e) {new MiniJavaParser(stream);}
		Goal goal = MiniJavaParser.Goal();

		ProgramStructureCollector.init(goal);
		ClassHierarchyAnalysis.init(goal);
		//Initialize AllocationVisitor.usedClasses so that we can skip analyzing ununsed classes.
		goal.accept(new AllocationVisitor());

		WrittenFieldsCollector writtenFieldsCollector = new WrittenFieldsCollector();
		writtenFieldsCollector.visit(goal, null);
		ConstraintCollector constraintCollector = new ConstraintCollector(writtenFieldsCollector.writtenFields);
		goal.accept(constraintCollector, null);

		Solver solver = new Solver();
		solver.clearUpSingleUnion(constraintCollector.constraints);

		HashSet<VariableRes> variables = new HashSet<>();
		for (EqualityRelationship r : constraintCollector.constraints)
		{
			if (r.right instanceof VariableRes && ((VariableRes) r.right).getInput().startsWith("this@"))
				variables.add((VariableRes) r.right);
		}
		Assert.assertEquals("res[this, L14, ?] should have two callsites.", 2, variables.size());

	}


}
