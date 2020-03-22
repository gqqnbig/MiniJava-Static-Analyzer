import numericalAnalysis.EqualityRelationship;
import numericalAnalysis.LiteralInterval;
import numericalAnalysis.VariableRes;
import org.junit.Assert;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Helper
{
	public static void assertVariableRes(List<EqualityRelationship> solution, String input, int lineNumber, int callSiteLine, LiteralInterval expectedValue)
	{
		Optional<EqualityRelationship> match = solution.stream().filter(r -> {
			if (r.left instanceof VariableRes)
			{
				VariableRes vOut = (VariableRes) r.left;
				return (vOut.getInput().startsWith(input + "@") && vOut.getStatement().getLine() == lineNumber && vOut.getCallSite().getLine() == callSiteLine);
			}
			return false;
		}).findAny();

		if (match.isPresent() == false)
			Assert.fail(String.format("res[%s, L%s, L%s] is not in solution. What we have are:\n" + solution.stream().map(Object::toString).collect(Collectors.joining("\n")), input, lineNumber, callSiteLine));

		Assert.assertEquals(String.format("Value of res[%s, L%s, L%s] is incorrect.", input, lineNumber, callSiteLine), expectedValue, match.get().right);
	}
}
