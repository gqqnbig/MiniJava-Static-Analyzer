import nullPointerAnalysis.ProgramStructureCollector;
import org.junit.Assert;
import org.junit.Test;
import syntaxtree.Goal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
}
