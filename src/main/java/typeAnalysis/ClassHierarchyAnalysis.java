package typeAnalysis;

import syntaxtree.Goal;
import syntaxtree.Identifier;

import java.util.*;

public class ClassHierarchyAnalysis
{
	private static Goal goal;
	private static HashMap<String, HashSet<Tuple<String, Integer>>> classMethodMapping;
	public static HashMap<String, String> superClassHierarchy;
	/**
	 * given a method name, return the classes where the given method can be called.
	 */
	private static HashMap<Tuple<String, Integer>, HashSet<String>> methodAvailableInClassMapping;

	public static void init(Goal goal)
	{
		ClassHierarchyAnalysis.goal = goal;
		if (ProgramStructureCollector.classMethodMapping.size() == 0)
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
//
//	public static HashMap<String, HashSet<String>> c2mTOm2c(HashMap<String, HashSet<String>> c2m,
//															List<String> rootlist, HashMap<String, HashSet<String>> cha)
//	{
//		HashMap<String, HashSet<String>> m2c = new HashMap<String, HashSet<String>>();
//		for (String rootclass : rootlist)
//		{
//			for (String method : c2m.get(rootclass))
//			{
//				if (!m2c.containsKey(method))
//				{
//					m2c.put(method, new HashSet<String>());
//				}
//				m2c.get(method).add(rootclass);
//			}
//			dfs(c2m, rootclass, cha, m2c);
//		}
//		return m2c;
//	}
//
//	public static void dfs(HashMap<String, HashSet<String>> c2m,
//						   String parent, HashMap<String, HashSet<String>> cha, HashMap<String, HashSet<String>> m2c)
//	{
//		if (cha.containsKey(parent))
//		{
//			for (String child : cha.get(parent))
//			{
//				// complete class to method
//				for (String method : c2m.get(parent))
//				{
//					c2m.get(child).add(method);
//				}
//				// method to class
//				for (String method : c2m.get(child))
//				{
//					if (!m2c.containsKey(method))
//					{
//						m2c.put(method, new HashSet<String>());
//					}
//					m2c.get(method).add(child);
//				}
//				dfs(c2m, child, cha, m2c);
//			}
//		}
//	}
}

class Tuple<T1, T2>
{
	public T1 item1;
	public T2 item2;

	public Tuple(T1 item1, T2 item2)
	{
		this.item1 = item1;
		this.item2 = item2;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Tuple<?, ?> tuple = (Tuple<?, ?>) o;
		return Objects.equals(item1, tuple.item1) &&
				Objects.equals(item2, tuple.item2);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(item1, item2);
	}
}
