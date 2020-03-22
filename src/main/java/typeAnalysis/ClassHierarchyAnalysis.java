package typeAnalysis;

import syntaxtree.Goal;
import syntaxtree.PrimaryExpression;
import syntaxtree.ThisExpression;

import java.util.*;
import java.util.stream.Collectors;

public class ClassHierarchyAnalysis
{
	private static HashMap<String, HashSet<MethodSignature>> classMethodMapping;
	public static HashMap<String, String> superClassHierarchy;
	/**
	 * given a method name, return the classes where the given method can be called.
	 */
	private static HashMap<MethodSignature, HashSet<String>> methodAvailableInClassMapping;

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
	 * @param className
	 * @return
	 */
	public static Collection<String> getPossibleTypes(PrimaryExpression receiver, String className, String methodName, int parameterCount)
	{
		if (receiver.f0.choice instanceof ThisExpression)
		{
			//Type of `this` is this class and its derivations.
			Collection<String> data2 = new ArrayList<>();
			data2.add(className);

			Queue<String> classes = new LinkedList<>();
			classes.add(className);

			while (classes.size() > 0)
			{
				String c = classes.poll();
				Collection<String> types = superClassHierarchy.entrySet().stream().filter(s -> s.getValue().equals(c)).map(Map.Entry::getKey).collect(Collectors.toList());
				data2.addAll(types);
				classes.addAll(types);
			}
			return data2;
		}
		else
			return methodAvailableInClassMapping.get(new MethodSignature(methodName, parameterCount));
	}

//	/**
//	 * Get possible types of an identifier.
//	 *
//	 * @param
//	 * @return
//	 */
//	public static Collection<String> getPossibleTypes(Identifier receiver, String methodName, int parameterCount)
//	{
//		return methodAvailableInClassMapping.get(new Tuple<>(methodName, parameterCount));
//	}

	static HashMap<MethodSignature, HashSet<String>> c2mTOm2c(HashMap<String, HashSet<MethodSignature>> c2m)
	{
		HashMap<MethodSignature, HashSet<String>> m2c = new HashMap<>();
		for (String thisClass : c2m.keySet())
		{
			String searchingClass = thisClass;
			while (searchingClass != null)
			{
				for (MethodSignature methodInfo : c2m.get(searchingClass))
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

