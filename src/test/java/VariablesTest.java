import org.junit.Assert;
import org.junit.Test;
import syntaxtree.Goal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class VariablesTest
{
	@Test
	public void testVariableNumbers() throws ParseException, FileNotFoundException
	{
		FileInputStream stream = new FileInputStream("testcases/hw2/pair5/NullCheck.java");
		new MiniJavaParser(stream);
		Goal goal = MiniJavaParser.Goal();

		NullableCollector.init(goal);
		VariableCollector variableCollector = new VariableCollector();
		goal.accept(variableCollector, null);

		Assert.assertEquals(21, variableCollector.variables.size());
	}
}
