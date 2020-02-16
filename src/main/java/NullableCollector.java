import baseVisitors.ScopeVisitor;
import syntaxtree.*;
import utils.Scope;

import java.util.ArrayList;
import java.util.List;

public class NullableCollector extends ScopeVisitor<Object>
{
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
		goal.accept(new NullableCollector());
	}


//	static HashMap<NullableIdentifier, Scope> nullables = new HashMap<>();

	static ArrayList<NullableIdentifierDefinition> nullables = new ArrayList<>();

	public static List<NullableIdentifierDefinition> getNullableIdentifierInScope(Scope scope)
	{
		List<NullableIdentifierDefinition> scopeNullables = new ArrayList<>();
		for (NullableIdentifierDefinition entry : nullables)
		{
			if(entry.Class.equals(scope.Class) && entry.Method.equals(scope.Method))
				scopeNullables.add(entry);
		}
		return scopeNullables;
	}

//	public static List<NullableIdentifier> getNullableIdentifierInScope(Scope scope)
//	{
//		List<NullableIdentifier> scopeNullables = new ArrayList<>();
//		for (HashMap.Entry<NullableIdentifier, Scope> entry : nullables.entrySet())
//		{
//			if (entry.getValue().equals(scope))
//				scopeNullables.add(entry.getKey());
//		}
//		return scopeNullables;
//	}
//
//	static HashMap<Identifier, Scope> nullables = new HashMap<>();


	private NullableCollector() {}

	@Override
	public Object visitScope(MainClass n)
	{
		n.f14.accept(this);
		return null;
	}

	@Override
	public Object visitScope(MethodDeclaration n)
	{
		n.f4.accept(this);
		n.f7.accept(this);
		return null;
	}

	@Override
	public Object visitScope(ClassDeclaration n)
	{
		n.f3.accept(this);
		return null;
	}

	@Override
	protected Object visitScope(ClassExtendsDeclaration n)
	{
		return null;
	}

	@Override
	public Object visit(VarDeclaration n)
	{
		if (isNullable(n.f0))
		{
//			nullables.put(new NullableIdentifier(n.f1), new Scope(getClassName(), getMethodName()));
			nullables.add(new NullableIdentifierDefinition(n.f1, getClassName(), getMethodName(), false));
		}
		return null;
	}

	@Override
	public Object visit(FormalParameter n)
	{
		if (isNullable(n.f0))
		{
//			nullables.put(new NullableIdentifier(n.f1), new Scope(getClassName(), getMethodName()));
			nullables.add(new NullableIdentifierDefinition(n.f1, getClassName(), getMethodName(), true));
		}
		return null;
	}
}
