package baseVisitors;

import syntaxtree.ClassDeclaration;
import syntaxtree.ClassExtendsDeclaration;
import syntaxtree.MainClass;
import syntaxtree.MethodDeclaration;
import visitor.GJNoArguDepthFirst;

public abstract class ScopeVisitor<R> extends GJNoArguDepthFirst<R>
{

	private String className;
	private String methodName;


	public String getClassName()
	{
		return className;
	}

	public String getMethodName()
	{
		return methodName;
	}


	public abstract R visitScope(MainClass n);

	public abstract R visitScope(MethodDeclaration n);

	public abstract R visitScope(ClassDeclaration n);

	protected abstract R visitScope(ClassExtendsDeclaration n);

//region  Collect class name and method name

	@Override
	public final R visit(MainClass n)
	{
		className = n.f1.f0.toString();
		methodName = "main";

		R r = visitScope(n);

		methodName = null;
		className = null;
		return r;
	}

	@Override
	public final R visit(MethodDeclaration n)
	{
		methodName = n.f2.f0.tokenImage;

		R r = visitScope(n);

		methodName = null;
		return r;
	}


	@Override
	public final R visit(ClassDeclaration n)
	{
		className = n.f1.f0.toString();
		R r = visitScope(n);
		className = null;
		return r;
	}


	@Override
	public final R visit(ClassExtendsDeclaration n)
	{
		className = n.f1.f0.toString();
		R r = visitScope(n);
		className = null;
		return r;
	}
//endregion
}
