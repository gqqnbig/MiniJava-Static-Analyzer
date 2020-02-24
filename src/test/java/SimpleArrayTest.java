import baseVisitors.AllocationVisitor;
import nullPointerAnalysis.ProgramStructureCollector;
import nullPointerAnalysis.Solver;
import org.junit.Assert;
import org.junit.Test;
import syntaxtree.Goal;
import typeAnalysis.ClassHierarchyAnalysis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;

public class SimpleArrayTest
{
	@Test
	public void test() throws FileNotFoundException, ParseException
	{
		FileInputStream stream = new FileInputStream("testcases/hw2/SimpleArrayTest.java");
		try {new MiniJavaParser(stream);} catch (Throwable e) {MiniJavaParser.ReInit(stream);}
		Goal goal = MiniJavaParser.Goal();

		ProgramStructureCollector.init(goal);
		ClassHierarchyAnalysis.init(goal);
		goal.accept(new AllocationVisitor());

		Solver.debugOut = new PrintStream(OutputStream.nullOutputStream());
		Assert.assertTrue("SimpleArrayTest may throw null pointer exception, but we didn't detect it.", Solver.checkNullPointer(goal));

	}
}
