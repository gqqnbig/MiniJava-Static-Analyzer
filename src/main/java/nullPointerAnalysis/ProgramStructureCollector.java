package nullPointerAnalysis;

import syntaxtree.*;
import utils.Location;
import utils.Scope;

import java.util.*;

public class ProgramStructureCollector extends typeAnalysis.ProgramStructureCollector
{
	static ArrayList<NullableIdentifierDefinition> nullables = new ArrayList<>();
	static HashMap<Tuple, Location> lastStatementData = new HashMap<>();

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


	private static List<NullableIdentifierDefinition> getNullableFieldsDefinedInClass(String className)
	{
		List<NullableIdentifierDefinition> scopeNullables = new ArrayList<>();
		for (NullableIdentifierDefinition entry : nullables)
		{
			if (entry.Class.equals(className) && entry.Method == null)
				scopeNullables.add(entry);
		}
		return scopeNullables;
	}

	public static List<NullableIdentifierDefinition> getNullableIdentifiersInScope(Scope scope)
	{
		List<NullableIdentifierDefinition> scopeNullables = new ArrayList<>();
		for (NullableIdentifierDefinition entry : nullables)
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
	public static NullableIdentifierDefinition getDefinition(Identifier identifier, Scope scope)
	{
		NullableIdentifierDefinition fieldDefinition = null;
		for (var d : getNullableIdentifiersInScope(scope))
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


	Location lastStatement;

	public static Location getLastStatement(String className, String methodName)
	{
		Tuple key = new Tuple();
		key.item1 = className;
		key.item2 = methodName;

		return lastStatementData.get(key);
	}


	@Override
	public Object visitScope(MainClass n)
	{
		super.visitScope(n);
		n.f14.accept(this);
		n.f15.accept(this);
		if (lastStatement != null)
		{
			Tuple key = new Tuple();
			key.item1 = getClassName();
			key.item2 = getMethodName();
			lastStatementData.put(key, lastStatement);
		}
		lastStatement = null;

		return null;
	}

	@Override
	public Object visitScope(MethodDeclaration n)
	{
		super.visitScope(n);

		n.f4.accept(this);
		n.f7.accept(this);


		lastStatement = new Location(n.f9);
		Tuple key = new Tuple();
		key.item1 = getClassName();
		key.item2 = getMethodName();
		lastStatementData.put(key, lastStatement);

		lastStatement = null;
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
			nullables.add(new NullableIdentifierDefinition(n.f1, getClassName(), getMethodName(), false));
		return null;
	}

	@Override
	public Object visit(FormalParameter n)
	{
		if (isNullable(n.f0))
			nullables.add(new NullableIdentifierDefinition(n.f1, getClassName(), getMethodName(), true));
		return null;
	}

	@Override
	public Object visit(Statement n)
	{
		lastStatement = new Location(n);
		return super.visit(n);
	}
}

class Tuple
{
	public String item1;
	public String item2;

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