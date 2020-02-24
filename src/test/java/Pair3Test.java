import nullPointerAnalysis.*;
import org.junit.Assert;
import org.junit.Test;
import syntaxtree.Goal;
import typeAnalysis.ClassHierarchyAnalysis;
import typeAnalysis.RapidTypeAnalysis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Pair3Test
{
	@Test
	public void test() throws FileNotFoundException, ParseException
	{
		FileInputStream stream = new FileInputStream("testcases/hw2/pair3.java");
		try {new MiniJavaParser(stream);} catch (Throwable e) {MiniJavaParser.ReInit(stream);}
		Goal goal = MiniJavaParser.Goal();


		ProgramStructureCollector.init(goal);
		Solver.typeService=new ClassHierarchyAnalysis();
		Solver.typeService.init(goal);
		VariableCollector variableCollector = new VariableCollector();
		goal.accept(variableCollector, null);

		ConstraintCollector constraintCollector = new ConstraintCollector();
		goal.accept(constraintCollector, null);

		Assert.assertFalse("Pair3.main doesn't have any local variable.",
				constraintCollector.constraints.stream().anyMatch(c -> c.left instanceof VariableIn &&
						((VariableIn) c.left).getStatement().getLine() == 8 &&
						((VariableIn) c.left).getInput().getIsParameter() == false));

		Assert.assertTrue("res[new X(), n] is missing",
				constraintCollector.constraints.stream().anyMatch(c -> c.left instanceof VariableRes && ((VariableRes) c.left).getInput().contains("new X()")));

		Assert.assertTrue("res[x, n]=in[x,n] is missing",
				constraintCollector.constraints.stream().anyMatch(c -> c.left instanceof VariableRes && ((VariableRes) c.left).getStatement().getLine() == 16 &&
						c.right instanceof VariableIn && ((VariableIn) c.right).getStatement().getLine() == 16 && ((VariableIn) c.right).getInput().getIdentifier().equals("x")));
	}
}
