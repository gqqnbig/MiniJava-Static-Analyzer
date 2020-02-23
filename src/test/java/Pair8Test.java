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
		ClassHierarchyAnalysis.init(goal);
		boolean condition = false;
		PrintStream debugOut = new PrintStream(OutputStream.nullOutputStream());
		ConstraintCollector constraintCollector = new ConstraintCollector();
		goal.accept(constraintCollector, null);

		//Clear up single union
		Solver.clearUpSingleUnion(constraintCollector.constraints);

		List<EqualityRelationship> solutions = Solver.solve(constraintCollector.constraints);
		solutions = solutions.stream().filter(r -> r.left instanceof VariableRes).collect(Collectors.toList());

		Assert.assertTrue("res[nullField, L19] = M is missing",
				solutions.stream().anyMatch(c -> ((VariableRes) c.left).getInput().startsWith("nullField@") && c.right == PossibleNullLiteral.instance));


		MessageSendCollector messageSendCollector = new MessageSendCollector();
		goal.accept(messageSendCollector);
		for (MessageSend ms : messageSendCollector.messageSends)
		{
			if (solutions.stream().anyMatch(r -> ((VariableRes) r.left).getExpression() == ms.f0 && r.right == PossibleNullLiteral.instance))
			{
				condition = true;
				break;
			}
		}
		if (!condition)
		{
			ArrayLookupVisitor arrayLookupVisitor = new ArrayLookupVisitor();
			goal.accept(arrayLookupVisitor);
			for (ArrayLookup al : arrayLookupVisitor.arrayLookups)
			{
				if (solutions.stream().anyMatch(r -> ((VariableRes) r.left).getExpression() == al.f0 && r.right == PossibleNullLiteral.instance))
				{
					condition = true;
					break;
				}
			}

		}

		Assert.assertTrue("Pair8 may throw null pointer exception, but we didn't detect it.", condition);
	}
}
