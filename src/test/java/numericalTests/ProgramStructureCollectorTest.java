import numericalAnalysis.ProgramStructureCollector;
import org.junit.Assert;
import org.junit.Test;
import syntaxtree.Goal;
import utils.Location;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Set;

public class ProgramStructureCollectorTest
{
	@Test
	public void testCallSites() throws FileNotFoundException, ParseException
	{
		FileInputStream stream = new FileInputStream("testcases/hw3/Pair4.java");
		try {MiniJavaParser.ReInit(stream);} catch (Throwable e) {new MiniJavaParser(stream);}
		Goal goal = MiniJavaParser.Goal();

		ProgramStructureCollector.init(goal);

		Set<Location> callSites = ProgramStructureCollector.getCallsites("Test", "q", 1);
		Assert.assertNotNull("Unable to find callsite of Test.q().", callSites);

	}
}
