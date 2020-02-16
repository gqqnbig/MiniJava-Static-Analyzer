import baseVisitors.ScopeVisitor;
import syntaxtree.*;
import utils.NodeCapture;
import utils.Scope;

import java.util.ArrayList;
import java.util.HashMap;

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


	static ArrayList<NodeCapture<Identifier>> nullables = new ArrayList<>();
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
			nullables.add(new NodeCapture<>(n.f1, new Scope(getClassName(), getMethodName())));
		}
		return null;
	}

	@Override
	public Object visit(FormalParameter n)
	{
		if (isNullable(n.f0))
		{
			nullables.add(new NodeCapture<>(n.f1, new Scope(getClassName(), getMethodName())));
		}
		return null;
	}
}
