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

public class Pair8Test
{
	@Test
	public void test() throws ParseException, FileNotFoundException
	{
		FileInputStream stream = new FileInputStream("testcases/hw2/pair8.java");
		try {new MiniJavaParser(stream);} catch (Throwable e) {MiniJavaParser.ReInit(stream);}
		Goal goal = MiniJavaParser.Goal();

		ProgramStructureCollector.init(goal);
		ClassHierarchyAnalysis.init(goal);
		Assert.assertTrue("Pair8 may throw null pointer exception, but we didn't detect it.", Solver.checkNullPointer(goal, new PrintStream(OutputStream.nullOutputStream())));
	}
}
