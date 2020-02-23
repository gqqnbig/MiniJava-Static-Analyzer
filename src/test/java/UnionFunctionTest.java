import nullPointerAnalysis.NotNullLiteral;
import nullPointerAnalysis.PossibleNullLiteral;
import nullPointerAnalysis.UnionFunction;
import org.junit.Assert;
import org.junit.Test;

public class UnionFunctionTest
{
	@Test
	public void testUnion()
	{
		Assert.assertSame(NotNullLiteral.instance, UnionFunction.union(NotNullLiteral.instance, NotNullLiteral.instance));
		Assert.assertSame(PossibleNullLiteral.instance, UnionFunction.union(NotNullLiteral.instance, PossibleNullLiteral.instance));
		Assert.assertSame(PossibleNullLiteral.instance, UnionFunction.union(PossibleNullLiteral.instance, NotNullLiteral.instance));
		Assert.assertSame(PossibleNullLiteral.instance, UnionFunction.union(PossibleNullLiteral.instance, PossibleNullLiteral.instance));
	}
}
