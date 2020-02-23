package nullPointerAnalysis;

import baseVisitors.ParameterCollector;
import syntaxtree.*;
import utils.Location;
import utils.Scope;

import java.util.*;
import java.util.stream.Collectors;

public class ProgramStructureCollector extends typeAnalysis.ProgramStructureCollector
{
	static ArrayList<ObjectIdentifierDefinition> objects;
	//	static HashMap<Tuple, Location> lastStatementData;
//	static HashMap<Tuple, Location> firstStatementData;
	static HashMap<Tuple, ArrayList<Location>> statementOrderData;
	static HashMap<Tuple, Expression> returnExpressions;
//	static HashMap<Tuple, ObjectIdentifierDefinition> methodParameterInfos;

//region static methods

	public static boolean isNullable(Type type)
	{
		if (type.f0.choice instanceof BooleanType)
			return false;
		if (type.f0.choice instanceof IntegerType)
			return false;

		assert type.f0.choice instanceof Identifier || type.f0.choice instanceof ArrayType;
		return true;
	}

	public static void init(Goal goal)
	{
		goal.accept(new ProgramStructureCollector());
	}


	private static List<ObjectIdentifierDefinition> getNullableFieldsDefinedInClass(String className)
	{
		List<ObjectIdentifierDefinition> scopeNullables = new ArrayList<>();
		for (ObjectIdentifierDefinition entry : objects)
		{
			if (entry.Class.equals(className) && entry.Method == null)
				scopeNullables.add(entry);
		}
		return scopeNullables;
	}

	public static List<ObjectIdentifierDefinition> getNullableIdentifiersInScope(Scope scope)
	{
		List<ObjectIdentifierDefinition> scopeNullables = new ArrayList<>();
		for (ObjectIdentifierDefinition entry : objects)
		{
			if (entry.Class.equals(scope.Class) &&
					(entry.Method == null || entry.Method.equals(scope.Method))) //fields are available in a method.
				scopeNullables.add(entry);
		}

		String className = typeAnalysis.ClassHierarchyAnalysis.superClassHierarchy.get(scope.Class);
		while (className != null)
		{
			scopeNullables.addAll(getNullableFieldsDefinedInClass(className));
			className = superClassHierarchy.get(className);
		}

		return scopeNullables;
	}

	/**
	 * Get definition of a nullable identifier from the identifier usage.
	 * <p>
	 * If the identifier is not nullable, eg. int, return null.
	 *
	 * @param identifier
	 * @return
	 */
	public static ObjectIdentifierDefinition getDefinition(Identifier identifier, Scope scope)
	{
		ObjectIdentifierDefinition fieldDefinition = null;
		for (ObjectIdentifierDefinition d : getNullableIdentifiersInScope(scope))
		{
			if (d.getIdentifier().equals(identifier.f0.toString()))
			{
				if (d.Method == null)
				{
					assert fieldDefinition == null;
					fieldDefinition = d;
				}
				else
					return d;
			}
		}
		return fieldDefinition;
	}


	public static Location getFirstStatement(String className, String methodName)
	{
//		return null;
		return statementOrderData.get(new Tuple(className, methodName)).get(0);
	}

	public static Location getLastStatement(String className, String methodName)
	{
		ArrayList<Location> list = statementOrderData.get(new Tuple(className, methodName));
		return list.get(list.size() - 1);
	}

	/**
	 * @param className
	 * @param methodName
	 * @param parameterIndex
	 * @return null if the parameter is not of type object.
	 */
	public static ObjectIdentifierDefinition getParameter(String className, String methodName, int parameterIndex)
	{
		Optional<ObjectIdentifierDefinition> parameter = objects.stream().filter(o -> o.parameterIndex == parameterIndex && o.Class.equals(className) && Objects.equals(o.Method, methodName)).findAny();
		// parameter may be null if the parameter is not object, eg. int.
		return parameter.orElse(null);
	}
	//endregion

	protected ProgramStructureCollector()
	{
		objects = new ArrayList<>();
		statementOrderData = new HashMap<>();
		returnExpressions = new HashMap<>();
	}

	public static List<Location> getSuccessors(String className, String methodName, Location location)
	{
		assert location != null : "Parameter location cannot be null.";
		ArrayList<Location> list = statementOrderData.get(new Tuple(className, methodName));
		int i = java.util.Collections.binarySearch(list, location);
		assert i >= 0;
		return list.subList(i + 1, list.size());
	}

	public static Expression getReturnExpression(String className, String methodName)
	{
		return returnExpressions.get(new Tuple(className, methodName));
	}


	@Override
	public Object visitScope(MainClass n)
	{
		super.visitScope(n);
		objects.add(new ObjectIdentifierDefinition(n.f11, getClassName()));

		n.f14.accept(this);

		Tuple key = new Tuple(getClassName(), getMethodName());
		statementOrderData.put(key, new ArrayList<>());
//		firstStatement = null;
		n.f15.accept(this);
//		if (lastStatement != null)
//		{
//			key.item1 = getClassName();
//			key.item2 = getMethodName();
//			lastStatementData.put(key, lastStatement);
//		}
//		if (firstStatement != null)
//			firstStatementData.put(key, firstStatement);
//
//		lastStatement = null;

		return null;
	}

	@Override
	public Object visitScope(MethodDeclaration n)
	{
		super.visitScope(n);

		ParameterCollector parameterCollector = new ParameterCollector();
		n.f4.accept(parameterCollector);
		ArrayList<FormalParameter> parameters = parameterCollector.parameters;
		for (int i = 0; i < parameters.size(); i++)
		{
			FormalParameter p = parameters.get(i);
			if (isNullable(p.f0))
				objects.add(new ObjectIdentifierDefinition(p.f1, getClassName(), getMethodName(), i));
		}


		n.f7.accept(this);

//		firstStatement = null;
		Tuple key = new Tuple(getClassName(), getMethodName());
		statementOrderData.put(key, new ArrayList<>());
		n.f8.accept(this);


		Location returnStatement = new Location(n.f9);
		statementOrderData.get(key).add(returnStatement);
		returnExpressions.put(key, n.f10);
//		Tuple key = new Tuple();
//		key.item1 = getClassName();
//		key.item2 = getMethodName();
//		lastStatementData.put(key, lastStatement);
//
//		if (firstStatement == null)
//			firstStatement = lastStatement;
//		firstStatementData.put(key, firstStatement);
//
//		lastStatement = null;
		return null;
	}

	@Override
	public Object visitScope(ClassDeclaration n)
	{
		assert classMethodMapping.containsKey(n.f1.f0.toString()) == false;
		classMethodMapping.put(n.f1.f0.toString(), new HashSet<>());


		n.f3.accept(this);
		n.f4.accept(this);
		return null;
	}

	@Override
	protected Object visitScope(ClassExtendsDeclaration n)
	{
		superClassHierarchy.put(n.f1.f0.toString(), n.f3.f0.toString());

		assert classMethodMapping.containsKey(n.f1.f0.toString()) == false;
		classMethodMapping.put(n.f1.f0.toString(), new HashSet<>());


		n.f5.accept(this);
		n.f6.accept(this);
		return null;
	}

	@Override
	public Object visit(VarDeclaration n)
	{
		if (isNullable(n.f0))
			objects.add(new ObjectIdentifierDefinition(n.f1, getClassName(), getMethodName(), -1));
		return null;
	}


	@Override
	public Object visit(Statement n)
	{
//		lastStatement = new Location(n);
//		if (firstStatement == null)
//			firstStatement = lastStatement;
		statementOrderData.get(new Tuple(getClassName(), getMethodName())).add(new Location(n));

		return null;
//		return super.visit(n);
	}
}

class Tuple
{
	public String item1;
	public String item2;


	public Tuple()
	{
	}

	public Tuple(String item1, String item2)
	{
		this.item1 = item1;
		this.item2 = item2;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Tuple tuple = (Tuple) o;
		return Objects.equals(item1, tuple.item1) &&
				Objects.equals(item2, tuple.item2);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(item1, item2);
	}
}