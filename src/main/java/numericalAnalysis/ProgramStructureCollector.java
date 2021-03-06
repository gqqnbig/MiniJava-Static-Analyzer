package numericalAnalysis;

import baseVisitors.ArgumentsCollector;
import baseVisitors.ParameterCollector;
import syntaxtree.*;
import typeAnalysis.MethodSignature;
import utils.Location;
import utils.Scope;
import utils.Tuple;

import java.util.*;

public class ProgramStructureCollector extends typeAnalysis.ProgramStructureCollector
{
	static ArrayList<IntIdentifierDefinition> integers;
	static HashMap<Tuple, ArrayList<JumpInfo>> statementOrderData;
	static HashMap<Tuple, Expression> returnExpressions;
	static HashMap<MethodSignature, Set<Location>> callsites;

//region static methods

	public static boolean isInteger(Type type)
	{
		return type.f0.choice instanceof IntegerType;
	}

	public static void init(Goal goal)
	{
		goal.accept(new ProgramStructureCollector());
	}


	private static List<IntIdentifierDefinition> getNullableFieldsDefinedInClass(String className)
	{
		List<IntIdentifierDefinition> scopeNullables = new ArrayList<>();
		for (IntIdentifierDefinition entry : integers)
		{
			if (entry.Class.equals(className) && entry.Method == null)
				scopeNullables.add(entry);
		}
		return scopeNullables;
	}

	public static List<IntIdentifierDefinition> getVariableIdentifiersInScope(Scope scope)
	{
		List<IntIdentifierDefinition> scopeNullables = new ArrayList<>();
		for (IntIdentifierDefinition entry : integers)
		{
			if (entry.Class.equals(scope.Class) && Objects.equals(entry.Method, scope.Method))
				scopeNullables.add(entry);
		}
		return scopeNullables;
	}

	public static List<IntIdentifierDefinition> getFieldIdentifiersInScope(Scope scope)
	{
		List<IntIdentifierDefinition> scopeNullables = new ArrayList<>();
		for (IntIdentifierDefinition entry : integers)
		{
			if (entry.Class.equals(scope.Class) && entry.Method == null) //fields are available in a method.
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

	public static List<IntIdentifierDefinition> getIntegerIdentifiersInScope(Scope scope)
	{
		List<IntIdentifierDefinition> scopeNullables = new ArrayList<>();
		for (IntIdentifierDefinition entry : integers)
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
	 * Get definition of an identifier from the identifier usage.
	 *
	 * @param identifier
	 * @return
	 */
	public static IntIdentifierDefinition getDefinition(Identifier identifier, Scope scope)
	{
		IntIdentifierDefinition fieldDefinition = null;
		for (IntIdentifierDefinition d : getIntegerIdentifiersInScope(scope))
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
		return statementOrderData.get(new Tuple(className, methodName)).get(0).location;
	}

	public static Location getLastStatement(String className, String methodName)
	{
		ArrayList<JumpInfo> list = statementOrderData.get(new Tuple(className, methodName));
		return list.get(list.size() - 1).location;
	}

	/**
	 * @param className
	 * @param methodName
	 * @param parameterIndex
	 * @return null if the parameter is not of type object.
	 */
	public static IntIdentifierDefinition getParameter(String className, String methodName, int parameterIndex)
	{
		Optional<IntIdentifierDefinition> parameter = integers.stream().filter(o -> o.parameterIndex == parameterIndex && o.Class.equals(className) && Objects.equals(o.Method, methodName)).findAny();
		// parameter may be null if the parameter is not object, eg. int.
		return parameter.orElse(null);
	}
	//endregion

	protected ProgramStructureCollector()
	{
		integers = new ArrayList<>();
		statementOrderData = new HashMap<>();
		returnExpressions = new HashMap<>();
		callsites = new HashMap<>();
	}

	public static List<Location> getSuccessors(String className, String methodName, Location location)
	{
		assert location != null : "Parameter location cannot be null.";
		ArrayList<JumpInfo> list = statementOrderData.get(new Tuple(className, methodName));
		int i = Collections.binarySearch(list, location, new Comparator<Object>()
		{
			@Override
			public int compare(Object o1, Object o2)
			{
				Location l1 = null;
				if (o1 instanceof JumpInfo)
					l1 = ((JumpInfo) o1).location;
				else if (o1 instanceof Location)
					l1 = (Location) o1;

				Location l2 = null;
				if (o2 instanceof JumpInfo)
					l2 = ((JumpInfo) o2).location;
				else if (o2 instanceof Location)
					l2 = (Location) o2;

				assert l1 != null;
				assert l2 != null;
				return l1.compareTo(l2);
			}
		});
		assert i >= 0;

		ArrayList<Location> result = new ArrayList<>();
		JumpInfo item = list.get(i);
		if (item.noNext == false && i + 1 < list.size())
			result.add(list.get(i + 1).location);

		if (item.additionalJump >= 0 && item.additionalJump < list.size())
			result.add(list.get(item.additionalJump).location);

		return result;
	}

	public static Expression getReturnExpression(String className, String methodName)
	{
		return returnExpressions.get(new Tuple(className, methodName));
	}

	/**
	 * find the locations that call this method.
	 *
	 * @return
	 */
	public static Set<Location> getCallsites(String className, String methodName, int parameterCount)
	{
		return callsites.get(new MethodSignature(methodName, parameterCount));
	}


	@Override
	public Object visitScope(MainClass n)
	{
		super.visitScope(n);

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
			if (isInteger(p.f0))
				integers.add(new IntIdentifierDefinition(p.f1, getClassName(), getMethodName(), i));
		}


		n.f7.accept(this);

//		firstStatement = null;
		Tuple key = new Tuple(getClassName(), getMethodName());
		statementOrderData.put(key, new ArrayList<>());
		n.f8.accept(this);


		Location returnStatement = new Location(n.f9);
		statementOrderData.get(key).add(new JumpInfo(returnStatement));
		returnExpressions.put(key, n.f10);

		//return statements may have message sends.
		visit(n.f10);

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
		if (isInteger(n.f0))
			integers.add(new IntIdentifierDefinition(n.f1, getClassName(), getMethodName(), -1));
		return null;
	}


	@Override
	public Object visit(Statement n)
	{
		if (n.f0.choice instanceof Block)
			return n.f0.accept(this);


		JumpInfo jump = new JumpInfo(new Location(n));
		ArrayList<JumpInfo> data = statementOrderData.get(new Tuple(getClassName(), getMethodName()));
		int jumpIndex = data.size();
		data.add(jump);

		if (n.f0.choice instanceof IfStatement)
		{
			IfStatement ifStatement = ((IfStatement) n.f0.choice);
			ifStatement.f4.accept(this);
			JumpInfo lastIfTrueJump = data.get(data.size() - 1);
			lastIfTrueJump.noNext = true;

			jump.additionalJump = data.size();
			ifStatement.f6.accept(this);

			lastIfTrueJump.additionalJump = data.size();
		}
		else if (n.f0.choice instanceof WhileStatement)
		{
			WhileStatement whileStatement = (WhileStatement) n.f0.choice;
			whileStatement.f4.accept(this);
			jump.additionalJump = data.size();

			assert jump != data.get(data.size() - 1);
			data.get(data.size() - 1).noNext = true;
			data.get(data.size() - 1).additionalJump = jumpIndex;
		}

		return super.visit(n);
	}

	@Override
	public Object visit(MessageSend n)
	{
		ArgumentsCollector p = new ArgumentsCollector();
		n.f4.accept(p);
		MethodSignature s = new MethodSignature(n.f2.f0.toString(), p.arguments.size());

		Location l = new Location(n.f2);
		Set<Location> list = callsites.computeIfAbsent(s, k -> new HashSet<>());
		list.add(l);

		return super.visit(n);
	}
}

//class Tuple
//{
//	public String item1;
//	public String item2;
//
//
//	public Tuple()
//	{
//	}
//
//	public Tuple(String item1, String item2)
//	{
//		this.item1 = item1;
//		this.item2 = item2;
//	}
//
//	@Override
//	public boolean equals(Object o)
//	{
//		if (this == o) return true;
//		if (o == null || getClass() != o.getClass()) return false;
//		Tuple tuple = (Tuple) o;
//		return Objects.equals(item1, tuple.item1) &&
//				Objects.equals(item2, tuple.item2);
//	}
//
//	@Override
//	public int hashCode()
//	{
//		return Objects.hash(item1, item2);
//	}
//}

class JumpInfo
{
	public Location location;
	public boolean noNext = false;
	public int additionalJump = -1;

	public JumpInfo(Location location)
	{
		this.location = location;
	}
}