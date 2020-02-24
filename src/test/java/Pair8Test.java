import baseVisitors.ArrayLookupVisitor;
import baseVisitors.MessageSendCollector;
import nullPointerAnalysis.*;
import org.junit.Assert;
import org.junit.Test;
import syntaxtree.ArrayLookup;
import syntaxtree.Goal;
import syntaxtree.MessageSend;
import typeAnalysis.ClassHierarchyAnalysis;
import typeAnalysis.RapidTypeAnalysis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Pair8Test
{
	@Test
	public void test() throws ParseException, FileNotFoundException
	{
		FileInputStream stream = new FileInputStream("testcases/hw2/pair8.java");
		try {MiniJavaParser.ReInit(stream);} catch (Throwable e) {new MiniJavaParser(stream);}
		Goal goal = MiniJavaParser.Goal();

		ProgramStructureCollector.init(goal);

		Solver.typeService=new ClassHierarchyAnalysis();
		Solver.typeService.init(goal);
		boolean condition = false;
		PrintStream debugOut = new PrintStream(OutputStream.nullOutputStream());
		ConstraintCollector constraintCollector = new ConstraintCollector();
		goal.accept(constraintCollector, null);

		List<EqualityRelationship> solutions = Solver.solve(constraintCollector.constraints);
		solutions = solutions.stream().filter(r -> r.left instanceof VariableRes).collect(Collectors.toList());

		Assert.assertTrue("res[nullField, L19] = M is missing from the solutions.",
				solutions.stream().anyMatch(c -> ((VariableRes) c.left).getInput().startsWith("nullField@") && c.right == PossibleNullLiteral.instance));

		Assert.assertTrue("Pair8 may throw null pointer exception, but we didn't detect it.", Solver.checkNullPointerException(goal, solutions));
	}
}
