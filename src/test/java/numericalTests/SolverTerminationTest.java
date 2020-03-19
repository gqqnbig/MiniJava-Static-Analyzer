import baseVisitors.AllocationVisitor;
import numericalAnalysis.ProgramStructureCollector;
import numericalAnalysis.Solver;
import org.junit.Test;
import syntaxtree.Goal;
import typeAnalysis.ClassHierarchyAnalysis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;

public class SolverTerminationTest
{
	@Test(timeout = 500)
	public void testBasic() throws FileNotFoundException, ParseException
	{
		FileInputStream stream = new FileInputStream("testcases/hw3/BasicSolverTest.java");
		try {MiniJavaParser.ReInit(stream);} catch (Throwable e) {new MiniJavaParser(stream);}
		Goal goal = MiniJavaParser.Goal();

		ProgramStructureCollector.init(goal);
		ClassHierarchyAnalysis.init(goal);
		//Initialize AllocationVisitor.usedClasses so that we can skip analyzing ununsed classes.
		goal.accept(new AllocationVisitor());

		Solver solver = new Solver();
		solver.debugOut = new PrintStream(OutputStream.nullOutputStream());
		solver.solve(goal);
	}
}
