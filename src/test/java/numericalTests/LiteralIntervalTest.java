import baseVisitors.AllocationVisitor;
import math.Literal;
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
		LiteralInterval i = (LiteralInterval) MultiplyInterval.multiply(new LiteralInterval(0, 0), new LiteralInterval(2, 2));
		Assert.assertEquals(0, i.lowerBound);
		Assert.assertEquals(0, i.upperBound);

		i = (LiteralInterval) MultiplyInterval.multiply(new LiteralInterval(5, 5), new LiteralInterval(2, 2));
		Assert.assertEquals(10, i.lowerBound);
		Assert.assertEquals(10, i.upperBound);
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

		Assert.assertTrue("res[x * 2, L27, L20] = [10, 10] is missing.",
				solution.stream().anyMatch(r -> {
					if (r.left instanceof VariableRes)
					{
						VariableRes vRes = (VariableRes) r.left;
						if (vRes.getInput().startsWith("x * 2@") && vRes.getCallSite().getLine() == 20)
						{
							LiteralInterval value = (LiteralInterval) r.right;
							return value.lowerBound == 10 && value.upperBound == 10;
						}
					}
					return false;
				}));

		Assert.assertTrue("res[(x * 2) - 5, L27, L20] = [-5, -5] is missing.",
				solution.stream().anyMatch(r -> {
					if (r.left instanceof VariableRes)
					{
						VariableRes vRes = (VariableRes) r.left;
						if (vRes.getInput().startsWith("(x * 2) - 5@") && vRes.getCallSite().getLine() == 20)
						{
							LiteralInterval value = (LiteralInterval) r.right;
							return value.lowerBound == 5 && value.upperBound == 5;
						}
					}
					return false;
				}));
	}
}
