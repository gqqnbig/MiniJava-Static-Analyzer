import baseVisitors.ArrayLookupVisitor;
import baseVisitors.MessageSendCollector;
import nullPointerAnalysis.*;
import org.junit.Assert;
import org.junit.Test;
import syntaxtree.ArrayLookup;
import syntaxtree.Goal;
import syntaxtree.MessageSend;
import typeAnalysis.ClassHierarchyAnalysis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class MethodChainTest
{
	@Test
	public void test() throws FileNotFoundException, ParseException
	{
		FileInputStream stream = new FileInputStream("testcases/hw2/MethodChainTest.java");
		try {MiniJavaParser.ReInit(stream);} catch (Throwable e) {new MiniJavaParser(stream);}
		Goal goal = MiniJavaParser.Goal();

		ProgramStructureCollector.init(goal);
		ClassHierarchyAnalysis.init(goal);
		PrintStream debugOut = new PrintStream(OutputStream.nullOutputStream());
		ConstraintCollector constraintCollector = new ConstraintCollector();
		goal.accept(constraintCollector, null);

		ArrayList<EqualityRelationship> solutions = Solver.solve(constraintCollector.constraints);

		Assert.assertTrue("res[field, L52] = M is missing.",
				solutions.stream().anyMatch(r -> r.left instanceof VariableRes && ((VariableRes) r.left).getInput().startsWith("field@") && ((VariableRes) r.left).getStatement().getLine() == 52
						&& r.right == PossibleNullLiteral.instance));

		Assert.assertTrue("MethodChainTest.java may throw null pointer exception.", Solver.checkNullPointerException(goal, solutions));
	}
}
