package typeAnalysis;

import syntaxtree.Goal;
import syntaxtree.Identifier;
import syntaxtree.PrimaryExpression;
import utils.Tuple;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class ClassHierarchyAnalysis
{
	private static HashMap<String, HashSet<Tuple<String, Integer>>> classMethodMapping;
	public static HashMap<String, String> superClassHierarchy;
	/**
	 * given a method name, return the classes where the given method can be called.
	 */
	private static HashMap<Tuple<String, Integer>, HashSet<String>> methodAvailableInClassMapping;

	public static void init(Goal goal)
	{
		if (ProgramStructureCollector.classMethodMapping == null || ProgramStructureCollector.classMethodMapping.size() == 0)
		{
			ProgramStructureCollector cmv = new ProgramStructureCollector();
			goal.accept(cmv);
		}
		classMethodMapping = ProgramStructureCollector.classMethodMapping;
		superClassHierarchy = ProgramStructureCollector.superClassHierarchy;
//		CHAVisitor chaVisitor = new CHAVisitor();
//		goal.accept(chaVisitor, "");
//		List<String> rootlist = chaVisitor.rootlist;
//		HashMap<String, HashSet<String>> cha = chaVisitor.parent2child;
		methodAvailableInClassMapping = c2mTOm2c(classMethodMapping);
//		MessageSendVisitor msv = new MessageSendVisitor();
//		msv.setMethodName2class(m2c);
//		goal.accept(msv, "");
	}

	/**
	 * Get possible types of an identifier.
	 *
	 * @param
	 * @return
	 */
	public static Collection<String> getPossibleTypes(PrimaryExpression receiver, String methodName, int parameterCount)
	{
		return methodAvailableInClassMapping.get(new Tuple<>(methodName, parameterCount));
	}

	/**
	 * Get possible types of an identifier.
	 *
	 * @param
	 * @return
	 */
	public static Collection<String> getPossibleTypes(Identifier receiver, String methodName, int parameterCount)
	{
		return methodAvailableInClassMapping.get(new Tuple<>(methodName, parameterCount));
	}

	static HashMap<Tuple<String, Integer>, HashSet<String>> c2mTOm2c(HashMap<String, HashSet<Tuple<String, Integer>>> c2m)
	{
		HashMap<Tuple<String, Integer>, HashSet<String>> m2c = new HashMap<>();
		for (String thisClass : c2m.keySet())
		{
			String searchingClass = thisClass;
			while (searchingClass != null)
			{
				for (Tuple<String, Integer> methodInfo : c2m.get(searchingClass))
				{
					if (!m2c.containsKey(methodInfo))
					{
						m2c.put(methodInfo, new HashSet<String>());
					}
					m2c.get(methodInfo).add(thisClass);
				}
				searchingClass = superClassHierarchy.get(searchingClass);
			}
		}
		return m2c;
	}
}

