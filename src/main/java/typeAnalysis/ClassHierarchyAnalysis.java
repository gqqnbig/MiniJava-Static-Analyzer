package typeAnalysis;

import syntaxtree.Goal;
import syntaxtree.Identifier;

import java.util.*;

public class ClassHierarchyAnalysis
{
	private static Goal goal;
	private static HashMap<String, HashSet<String>> classMethodMapping;
	public static HashMap<String, String> superClassHierarchy;
	/**
	 * given a method name, return the classes where the given method can be called.
	 */
	private static HashMap<String, HashSet<String>> methodAvailableInClassMapping;

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
	public static Collection<String> getPossibleTypes(Identifier receiver, String methodName)
	{
		return methodAvailableInClassMapping.get(methodName);
	}

	static HashMap<String, HashSet<String>> c2mTOm2c(HashMap<String, HashSet<String>> c2m)
	{
		HashMap<String, HashSet<String>> m2c = new HashMap<String, HashSet<String>>();
		for (String thisClass : c2m.keySet())
		{
			String searchingClass = thisClass;
			while (searchingClass != null)
			{
				for (String method : c2m.get(searchingClass))
				{
					if (!m2c.containsKey(method))
					{
						m2c.put(method, new HashSet<String>());
					}
					m2c.get(method).add(thisClass);
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
