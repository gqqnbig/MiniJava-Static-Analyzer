import math.Literal;
import numericalAnalysis.LiteralInterval;
import numericalAnalysis.MinusInterval;
import org.junit.Assert;
import org.junit.Test;

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
	public void testIntervalOperation()
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
}
