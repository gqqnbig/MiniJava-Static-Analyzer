import baseVisitors.AllocationVisitor;
import numericalAnalysis.*;
import org.junit.Assert;
import org.junit.Test;
import syntaxtree.Goal;
import typeAnalysis.ClassHierarchyAnalysis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Optional;

public class ConstraintTest
{
	@Test
	public void testC11() throws ParseException, FileNotFoundException
	{
		FileInputStream stream = new FileInputStream("testcases/hw3/CallsiteTest.java");
		try {MiniJavaParser.ReInit(stream);} catch (Throwable e) {new MiniJavaParser(stream);}
		Goal goal = MiniJavaParser.Goal();

		ProgramStructureCollector.init(goal);
		ClassHierarchyAnalysis.init(goal);
		//Initialize AllocationVisitor.usedClasses so that we can skip analyzing ununsed classes.
		goal.accept(new AllocationVisitor());

		WrittenFieldsCollector writtenFieldsCollector = new WrittenFieldsCollector();
		writtenFieldsCollector.visit(goal, null);
		ConstraintCollector constraintCollector = new ConstraintCollector(writtenFieldsCollector.writtenFields);
		goal.accept(constraintCollector, null);

		var c11 = constraintCollector.constraints.stream().filter(r -> "C11".equals(r.comment) && ((UnionFunction) r.right).getInput().stream().anyMatch(res -> ((VariableRes) res).getInput().startsWith("null@"))).findFirst();
		if (c11.isPresent())
			Assert.fail("The right hand side of C11 should not have null, but we find " + c11.get().toString());

		Assert.assertTrue("C12 res[1, n, cs] = [1, 1] is missing.", constraintCollector.constraints.stream().anyMatch(r -> "C12".equals(r.comment) && ((LiteralInterval) r.right).lowerBound == 1 && ((LiteralInterval) r.right).upperBound == 1));
	}

	@Test
	public void testC1() throws FileNotFoundException, ParseException
	{
		FileInputStream stream = new FileInputStream("testcases/hw3/SingleUseFieldTest.java");
		try {MiniJavaParser.ReInit(stream);} catch (Throwable e) {new MiniJavaParser(stream);}
		Goal goal = MiniJavaParser.Goal();

		ProgramStructureCollector.init(goal);
		ClassHierarchyAnalysis.init(goal);
		//Initialize AllocationVisitor.usedClasses so that we can skip analyzing ununsed classes.
		goal.accept(new AllocationVisitor());

		WrittenFieldsCollector writtenFieldsCollector = new WrittenFieldsCollector();
		writtenFieldsCollector.visit(goal, null);
		ConstraintCollector constraintCollector = new ConstraintCollector(writtenFieldsCollector.writtenFields);
		goal.accept(constraintCollector, null);

		Assert.assertTrue("in[A.f, L15, L5] is missing.", constraintCollector.constraints.stream().anyMatch(r -> {
			if (r.left instanceof VariableIn)
			{
				VariableIn vIn = (VariableIn) r.left;
				return vIn.getStatement().getLine() == 15 && vIn.getInput().getIdentifier().equals("f");
			}
			else
				return false;
		}));

		Solver solver = new Solver();
		solver.debugOut = new PrintStream(OutputStream.nullOutputStream());
		var solutions = solver.solve(goal, constraintCollector.constraints);

		Assert.assertTrue("Solution of res[f, L15, L5] is missing", solutions.stream().anyMatch(r -> r.left instanceof VariableRes && ((VariableRes) r.left).getInput().startsWith("f@")));

		Helper.assertVariableRes(solutions, "new A().id()", 5, -1, new LiteralInterval(-1));
	}

	@Test
	public void testC11Column() throws FileNotFoundException, ParseException
	{
		FileInputStream stream = new FileInputStream("testcases/hw3/SingleUseFieldTest.java");
		try {MiniJavaParser.ReInit(stream);} catch (Throwable e) {new MiniJavaParser(stream);}
		Goal goal = MiniJavaParser.Goal();

		ProgramStructureCollector.init(goal);
		ClassHierarchyAnalysis.init(goal);
		//Initialize AllocationVisitor.usedClasses so that we can skip analyzing ununsed classes.
		goal.accept(new AllocationVisitor());


		WrittenFieldsCollector writtenFieldsCollector = new WrittenFieldsCollector();
		writtenFieldsCollector.visit(goal, null);

		ConstraintCollector constraintCollector = new ConstraintCollector(writtenFieldsCollector.writtenFields);
		goal.accept(constraintCollector, null);

		Solver solver = new Solver();
		solver.clearUpSingleUnion(constraintCollector.constraints);
		Assert.assertTrue("res[new A().id(), L5C17, unknown] = res[f - 1, L15C17, L5C44] is missing.",
				constraintCollector.constraints.stream().anyMatch(r -> {
					if ("C11".equals(r.comment) && r.left instanceof VariableRes && r.right instanceof VariableRes)
					{
						return ((VariableRes) r.left).getInput().startsWith("new A().id()@") && ((VariableRes) r.right).getInput().startsWith("f - 1@") && ((VariableRes) r.right).getCallSite().getColumn() == 44;
					}
					return false;
				}));

	}
}
