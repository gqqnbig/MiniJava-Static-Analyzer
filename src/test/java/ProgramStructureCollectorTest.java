import nullPointerAnalysis.ProgramStructureCollector;
import org.junit.Assert;
import org.junit.Test;
import syntaxtree.Goal;
import utils.Location;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ProgramStructureCollectorTest
{
	@Test
	public void testGetFirstStatement() throws FileNotFoundException, ParseException
	{
		FileInputStream stream = new FileInputStream("testcases/hw2/pair3.java");
		try {MiniJavaParser.ReInit(stream);} catch (Throwable e) {new MiniJavaParser(stream);}
		Goal goal = MiniJavaParser.Goal();

		ProgramStructureCollector.init(goal);

		var location = ProgramStructureCollector.getFirstStatement("Pair3", "main");
		Assert.assertNotNull("Unable to find first statement of Pair3.main", location);
		Assert.assertEquals(8, location.getLine());


		location = ProgramStructureCollector.getFirstStatement("X", "m");
		Assert.assertNotNull(location);
		Assert.assertEquals(16, location.getLine());

		location = ProgramStructureCollector.getFirstStatement("X", "op");
		Assert.assertNotNull(location);
		Assert.assertEquals(22, location.getLine());


		location = ProgramStructureCollector.getFirstStatement("Y", "m");
		Assert.assertNotNull(location);
		Assert.assertEquals(29, location.getLine());
	}

	@Test
	public void testIf() throws ParseException, FileNotFoundException
	{
		FileInputStream stream = new FileInputStream("testcases/hw2/IfTest.java");
		try {MiniJavaParser.ReInit(stream);} catch (Throwable e) {new MiniJavaParser(stream);}
		Goal goal = MiniJavaParser.Goal();

		ProgramStructureCollector.init(goal);

		Location s = ProgramStructureCollector.getFirstStatement("Program", "main");
		Assert.assertEquals(6, s.getLine());


		var successors = ProgramStructureCollector.getSuccessors("Program", "main", s);
		Assert.assertEquals("Successor of n=1 is the if block.", 1, successors.size());
		Assert.assertEquals(7, successors.get(0).getLine());

		successors = ProgramStructureCollector.getSuccessors("Program", "main", successors.get(0));
		Assert.assertEquals("If statement has 2 successors.", 2, successors.size());

		Assert.assertTrue("One successor of n==1 is line 6.", successors.stream().anyMatch(l -> l.getLine() == 9));
		Assert.assertTrue("One successor of n==1 is line 9.", successors.stream().anyMatch(l -> l.getLine() == 13));

		Assert.assertEquals(15, ProgramStructureCollector.getSuccessors("Program", "main", successors.get(0)).get(0).getLine());
		Assert.assertEquals(15, ProgramStructureCollector.getSuccessors("Program", "main", successors.get(1)).get(0).getLine());
	}


	@Test
	public void testWhile() throws ParseException, FileNotFoundException
	{
		FileInputStream stream = new FileInputStream("testcases/hw2/WhileTest.java");
		try {MiniJavaParser.ReInit(stream);} catch (Throwable e) {new MiniJavaParser(stream);}
		Goal goal = MiniJavaParser.Goal();

		ProgramStructureCollector.init(goal);

		Location s = ProgramStructureCollector.getFirstStatement("WhileTest", "main");
		Assert.assertEquals(6, s.getLine());


		var successors = ProgramStructureCollector.getSuccessors("WhileTest", "main", s);
		Assert.assertEquals("Successor of n=1 is the while block.", 1, successors.size());
		Assert.assertEquals(7, successors.get(0).getLine());

		successors = ProgramStructureCollector.getSuccessors("WhileTest", "main", successors.get(0));
		Assert.assertEquals("while statement has 2 successors.", 2, successors.size());

		s = successors.stream().filter(l -> l.getLine() == 9).findAny().orElse(null);
		Assert.assertNotNull("One successor of while statement is line 9, but we get" + successors.toString() + ".", s);
		Assert.assertTrue("One successor of while statement is line 12.", successors.stream().anyMatch(l -> l.getLine() == 12));

		s = ProgramStructureCollector.getSuccessors("WhileTest", "main", s).get(0);
		Assert.assertEquals("The successor of line 9 is line 10.", 10, s.getLine());
		Assert.assertEquals("The last statement of while loop goes back to the while statement.", 7, ProgramStructureCollector.getSuccessors("WhileTest", "main", s).get(0).getLine());
	}
}
