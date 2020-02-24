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

public class Pair9Test
{
	@Test
	public void test() throws ParseException, FileNotFoundException
	{
		FileInputStream stream = new FileInputStream("testcases/hw2/Pair9.java");
		try {MiniJavaParser.ReInit(stream);} catch (Throwable e) {new MiniJavaParser(stream);}
		Goal goal = MiniJavaParser.Goal();

		ProgramStructureCollector.init(goal);

		Solver.typeService=new ClassHierarchyAnalysis();
		Solver.typeService.init(goal);
		PrintStream debugOut = new PrintStream(OutputStream.nullOutputStream());
		ConstraintCollector constraintCollector = new ConstraintCollector();
		goal.accept(constraintCollector, null);

		List<EqualityRelationship> solutions = Solver.solve(constraintCollector.constraints);
		solutions = solutions.stream().filter(r -> r.left instanceof VariableRes).collect(Collectors.toList());

		Assert.assertFalse("Pair9.java doesn't throw null pointer exception.", Solver.checkNullPointerException(goal, solutions));

	}
}
