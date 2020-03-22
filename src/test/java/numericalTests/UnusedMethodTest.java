import baseVisitors.AllocationVisitor;
import numericalAnalysis.*;
import org.junit.Assert;
import org.junit.Test;
import syntaxtree.Goal;
import typeAnalysis.ClassHierarchyAnalysis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;

public class UnusedMethodTest
{
	@Test
	public void test() throws FileNotFoundException, ParseException
	{
		FileInputStream stream = new FileInputStream("testcases/hw3/UnusedMethodTest.java");
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
		solver.debugOut = new PrintStream(OutputStream.nullOutputStream());
		var solutions = solver.solve(goal, constraintCollector.constraints);

	}
}
