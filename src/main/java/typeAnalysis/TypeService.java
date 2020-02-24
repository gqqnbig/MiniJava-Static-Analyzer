package typeAnalysis;

import syntaxtree.Goal;
import syntaxtree.Identifier;
import syntaxtree.PrimaryExpression;

import java.util.Collection;
import java.util.HashMap;

public interface TypeService
{
	HashMap<String, String> getSuperClassHierarchy();

	void init(Goal goal);

	Collection<String> getPossibleTypes(PrimaryExpression receiver, String methodName, int parameterCount);

	Collection<String> getPossibleTypes(Identifier receiver, String methodName, int parameterCount);


}
