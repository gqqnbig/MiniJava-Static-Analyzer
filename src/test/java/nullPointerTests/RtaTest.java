import org.junit.Assert;
import org.junit.Test;
import syntaxtree.Goal;
import syntaxtree.PrimaryExpression;
import typeAnalysis.ClassHierarchyAnalysis;
import typeAnalysis.ProgramStructureCollector;
//import typeAnalysis.RapidTypeAnalysis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

//public class RtaTest
//{
//	@Test
//	public void test() throws FileNotFoundException, ParseException
//	{
//		FileInputStream stream = new FileInputStream("testcases/hw2/RtaTest.java");
//		try {MiniJavaParser.ReInit(stream);} catch (Exception e) {new MiniJavaParser(stream);}
//		Goal goal = MiniJavaParser.Goal();
//		ProgramStructureCollector.init(goal);
//		var rta = new RapidTypeAnalysis();
//		rta.init(goal);
//
//		var types = rta.getPossibleTypes((PrimaryExpression) null, "m", 2);
//		Assert.assertTrue(types.contains("E"));
//		Assert.assertEquals(1, types.size());
//	}
//}
