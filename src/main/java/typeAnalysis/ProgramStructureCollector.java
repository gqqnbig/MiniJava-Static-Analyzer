package typeAnalysis;

import baseVisitors.ParameterCollector;
import baseVisitors.ScopeVisitor;
import syntaxtree.ClassDeclaration;
import syntaxtree.ClassExtendsDeclaration;
import syntaxtree.MainClass;
import syntaxtree.MethodDeclaration;
import utils.Tuple;

import java.util.HashMap;
import java.util.HashSet;


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
	public static HashMap<String, HashSet<MethodSignature>> classMethodMapping;

	/**
	map from subclass to its superclass
	 **/
	public static HashMap<String, String> superClassHierarchy;

	public static void init()
	{


	}

	public ProgramStructureCollector()
	{
		classMethodMapping = new HashMap<>();
		superClassHierarchy = new HashMap<>();
	}


	@Override
	public Object visitScope(MainClass n)
	{
		HashSet<MethodSignature> methods = new HashSet<>();
		methods.add(new MethodSignature("main", 1));
		classMethodMapping.put(getClassName(), methods);

		return null;
	}

	@Override
	public Object visitScope(MethodDeclaration n)
	{
		ParameterCollector parameterCollector = new ParameterCollector();
		n.f4.accept(parameterCollector);
		classMethodMapping.get(getClassName()).add(new MethodSignature(getMethodName(), parameterCollector.parameters.size()));
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