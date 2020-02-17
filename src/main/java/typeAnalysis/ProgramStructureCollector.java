package typeAnalysis;

import baseVisitors.ScopeVisitor;
import syntaxtree.ClassDeclaration;
import syntaxtree.ClassExtendsDeclaration;
import syntaxtree.MainClass;
import syntaxtree.MethodDeclaration;
import visitor.GJDepthFirst;

import java.util.HashMap;
import java.util.HashSet;

//// typdefs
//class ClassSet extends HashSet<String>
//{
//}

//class Mapping extends HashMap<String, ClassSet>
//{
//}

// R = [ method -> {classname} ] 
// A = classname 
// 
// R: 
// At each MethodDeclaration add the current A argument to the set for that 
// method name. All other nodes should merge and return 
// 
// A: 
// At each ClassDeclaration pass in the name as A. All other node types should 
// hand down what was given

/**
 * Collect classMethodMapping, superClassHierarchy
 */
public class ProgramStructureCollector extends ScopeVisitor<Object>
{
	protected static HashMap<String, HashSet<String>> classMethodMapping = new HashMap<String, HashSet<String>>();

	/*
	map from subclass to its superclass
	 */
	protected static HashMap<String, String> superClassHierarchy = new HashMap<>();

	@Override
	public Object visitScope(MainClass n)
	{
		HashSet<String> methods = new HashSet<>();
		methods.add("main");
		classMethodMapping.put(getClassName(), methods);

		return null;
	}

	@Override
	public Object visitScope(MethodDeclaration n)
	{
		classMethodMapping.get(getClassName()).add(getMethodName());
		return null;
	}

	@Override
	public Object visitScope(ClassDeclaration n)
	{
		assert classMethodMapping.containsKey(n.f1.f0.toString()) == false;
		classMethodMapping.put(n.f1.f0.toString(), new HashSet<>());

		n.f4.accept(this);
		return null;
	}

	@Override
	protected Object visitScope(ClassExtendsDeclaration n)
	{
		superClassHierarchy.put(n.f1.f0.toString(), n.f3.f0.toString());

		assert classMethodMapping.containsKey(n.f1.f0.toString()) == false;
		classMethodMapping.put(n.f1.f0.toString(), new HashSet<>());

		n.f6.accept(this);
		return null;
	}
}