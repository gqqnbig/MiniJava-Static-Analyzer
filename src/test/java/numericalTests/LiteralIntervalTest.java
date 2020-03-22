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

public class LiteralIntervalTest
{
	//	@SuppressWarnings("NumericOverflow")
//	@Test
//	public void testWrap()
//	{
//		Assert.assertEquals(Integer.MIN_VALUE - 1, LiteralInterval.wrap((long) Integer.MIN_VALUE - 1));
//
//		Assert.assertEquals(Integer.MIN_VALUE * 2, LiteralInterval.wrap((long) Integer.MIN_VALUE * 2));
//
//		Assert.assertEquals(Integer.MIN_VALUE - Integer.MAX_VALUE, LiteralInterval.wrap((long) Integer.MIN_VALUE - Integer.MAX_VALUE));
//
//		Assert.assertEquals(Integer.MAX_VALUE + 1, LiteralInterval.wrap((long) Integer.MAX_VALUE + 1));
//
//		Assert.assertEquals(Integer.MAX_VALUE * 2, LiteralInterval.wrap((long) Integer.MAX_VALUE *2 ));
//	}
	@Test
	public void testIntervalMultiply()
	{
		assertMultiply(new LiteralInterval(0), new LiteralInterval(2),new LiteralInterval(0));

		assertMultiply(new LiteralInterval(5), new LiteralInterval(2), new LiteralInterval(10));

		assertMultiply(new LiteralInterval(1), new LiteralInterval(-1), new LiteralInterval(-1));
	}

	private void assertMultiply(LiteralInterval a, LiteralInterval b, LiteralInterval expected)
	{
		LiteralInterval i;
		i = (LiteralInterval) MultiplyInterval.multiply(a, b);
		Assert.assertEquals(String.format("Value of %s (*) %s is incorrect.", a, b), expected, i);
	}

	@Test
	public void testIntervalMinus()
	{
		LiteralInterval i = (LiteralInterval) MinusInterval.minus(new LiteralInterval(Integer.MIN_VALUE, Integer.MAX_VALUE), new LiteralInterval(1, 1));
		Assert.assertEquals(Integer.MIN_VALUE, i.lowerBound);
		Assert.assertEquals(Integer.MAX_VALUE, i.upperBound);
	}

	@Test
	public void testLargeInterval()
	{
		long l = (long) Integer.MIN_VALUE * 2;
		long u = l + 2;
		LiteralInterval interval = new LiteralInterval(l, u);
		for (long i = l; i <= u; i++)
			Assert.assertTrue(interval.isIn((int) i));


		l = (long) Integer.MIN_VALUE - 1;
		u = (long) Integer.MIN_VALUE + 1;
		interval = new LiteralInterval(l, u);
		for (long i = l; i <= u; i++)
			Assert.assertTrue(interval.isIn((int) i));


		l = (long) Integer.MIN_VALUE - 1;
		u = (long) Integer.MAX_VALUE + 1;
		interval = new LiteralInterval(l, u);
		for (int i = 0; i <= 10; i++)
			Assert.assertTrue(interval.isIn((int) (l + i)));
		for (int i = 0; i <= 10; i++)
			Assert.assertTrue(interval.isIn((int) (u - i)));
		for (int i = 0; i <= 10; i++)
			Assert.assertTrue(interval.isIn(i));

	}

	@Test
	public void testPair6() throws FileNotFoundException, ParseException
	{
		FileInputStream stream = new FileInputStream("testcases/hw3/Pair6.java");
		try {MiniJavaParser.ReInit(stream);} catch (Throwable e) {new MiniJavaParser(stream);}
		Goal goal = MiniJavaParser.Goal();

		ProgramStructureCollector.init(goal);
		ClassHierarchyAnalysis.init(goal);
		//Initialize AllocationVisitor.usedClasses so that we can skip analyzing ununsed classes.
		goal.accept(new AllocationVisitor());

		Solver solver = new Solver();
		solver.debugOut = new PrintStream(OutputStream.nullOutputStream());

		WrittenFieldsCollector writtenFieldsCollector = new WrittenFieldsCollector();
		writtenFieldsCollector.visit(goal, null);

		ConstraintCollector constraintCollector = new ConstraintCollector(writtenFieldsCollector.writtenFields);
		goal.accept(constraintCollector, null);
		List<EqualityRelationship> solution = solver.solve(goal, constraintCollector.constraints);

		Helper.assertVariableRes(solution,"x * 2",27,20,new LiteralInterval(10));
		Helper.assertVariableRes(solution,"(x * 2) - 5",27,20,new LiteralInterval(5));
	}


	@Test
	public void testPair3() throws FileNotFoundException, ParseException
	{
		FileInputStream stream = new FileInputStream("testcases/hw3/Pair3.java");
		try {MiniJavaParser.ReInit(stream);} catch (Throwable e) {new MiniJavaParser(stream);}
		Goal goal = MiniJavaParser.Goal();

		ProgramStructureCollector.init(goal);
		ClassHierarchyAnalysis.init(goal);
		//Initialize AllocationVisitor.usedClasses so that we can skip analyzing ununsed classes.
		goal.accept(new AllocationVisitor());

		Solver solver = new Solver();
		solver.debugOut = new PrintStream(OutputStream.nullOutputStream());

		WrittenFieldsCollector writtenFieldsCollector = new WrittenFieldsCollector();
		writtenFieldsCollector.visit(goal, null);

		ConstraintCollector constraintCollector = new ConstraintCollector(writtenFieldsCollector.writtenFields);
		goal.accept(constraintCollector, null);
		List<EqualityRelationship> solution = solver.solve(goal, constraintCollector.constraints);

		Helper.assertVariableRes(solution,"c * b",25,8,new LiteralInterval(-1));
		Helper.assertVariableRes(solution, "c", 26, 8, new LiteralInterval(-1));

		Helper.assertVariableRes(solution, "a + c", 26, 8, new LiteralInterval(0));
		Optional<EqualityRelationship> match;

		match = solution.stream().filter(r -> {
			if (r.left instanceof VariableOut)
			{
				VariableOut vOut = (VariableOut) r.left;
				return (vOut.getInput().getIdentifier().equals("d") && vOut.getStatement().getLine() == 26 && vOut.getCallSite().getLine() == 8);
			}
			return false;
		}).findAny();
		Assert.assertTrue("out[A.A.d, L26, L8] is not in solution.", match.isPresent());
		Assert.assertEquals("Value of out[A.A.d, L26, L8] is incorrect.", new LiteralInterval(0, 0), match.get().right);


		Helper.assertVariableRes(solution, "ret + d", 30, 8, new LiteralInterval(-1));

	}

}
