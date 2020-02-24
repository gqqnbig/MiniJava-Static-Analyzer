package typeAnalysis;

import baseVisitors.AllocationVisitor;
import syntaxtree.Goal;
import syntaxtree.Identifier;
import syntaxtree.PrimaryExpression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class RapidTypeAnalysis extends ClassHierarchyAnalysis
{
	public void init(Goal goal)
	{
		this.goal = goal;
		if (ProgramStructureCollector.classMethodMapping == null || ProgramStructureCollector.classMethodMapping.size() == 0)
		{
			ProgramStructureCollector cmv = new ProgramStructureCollector();
			goal.accept(cmv);
		}

		AllocationVisitor allocationVisitor = new AllocationVisitor();
		goal.accept(allocationVisitor);

		classMethodMapping = ProgramStructureCollector.classMethodMapping;
		superClassHierarchy = ProgramStructureCollector.superClassHierarchy;

		ArrayList<String> keys = new ArrayList<>(classMethodMapping.keySet());
		for (String className : keys)
		{
			if (allocationVisitor.usedClasses.contains(className) == false)
				classMethodMapping.remove(className);
		}
		keys = new ArrayList<>(superClassHierarchy.keySet());
		for (String className : keys)
		{
			if (allocationVisitor.usedClasses.contains(className) == false)
				superClassHierarchy.remove(className);
		}
//		CHAVisitor chaVisitor = new CHAVisitor();
//		goal.accept(chaVisitor, "");
//		List<String> rootlist = chaVisitor.rootlist;
//		HashMap<String, HashSet<String>> cha = chaVisitor.parent2child;
		methodAvailableInClassMapping = c2mTOm2c(classMethodMapping);
//		MessageSendVisitor msv = new MessageSendVisitor();
//		msv.setMethodName2class(m2c);
//		goal.accept(msv, "");
	}
}