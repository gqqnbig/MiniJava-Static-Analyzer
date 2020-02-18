import org.junit.Assert;
import org.junit.Test;
import syntaxtree.Goal;
import typeAnalysis.ClassHierarchyAnalysis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ChaTest
{
	@Test
	public void testParameterCountMatching() throws FileNotFoundException, ParseException
	{
		FileInputStream stream = new FileInputStream("testcases/hw2/ParameterCountTest.java");
		try {new MiniJavaParser(stream);} catch (Exception e) {MiniJavaParser.ReInit(stream);}
		Goal goal = MiniJavaParser.Goal();
		ClassHierarchyAnalysis.init(goal);

		var types = ClassHierarchyAnalysis.getPossibleTypes(null, "get", 2);
		Assert.assertTrue(types.contains("A"));
		Assert.assertEquals(1, types.size());
	}
}
