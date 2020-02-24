package baseVisitors;

import syntaxtree.ClassDeclaration;
import syntaxtree.ClassExtendsDeclaration;
import syntaxtree.MainClass;
import syntaxtree.MethodDeclaration;
import visitor.GJVoidDepthFirst;

public abstract class VoidScopeVisitor<A> extends GJVoidDepthFirst<A>
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


	public abstract void visitScope(MainClass n, A argu);

	public abstract void visitScope(MethodDeclaration n, A argu);

	public abstract void visitScope(ClassDeclaration n, A argu);

	protected abstract void visitScope(ClassExtendsDeclaration n, A argu);

//region  Collect class name and method name

	@Override
	public final void visit(MainClass n, A argu)
	{
		className = n.f1.f0.toString();
		methodName = "main";

		visitScope(n, argu);

		methodName = null;
		className = null;
	}

	@Override
	public final void visit(MethodDeclaration n, A argu)
	{
		methodName = n.f2.f0.tokenImage;

		visitScope(n, argu);

		methodName = null;
	}


	@Override
	public final void visit(ClassDeclaration n, A argu)
	{
		className = n.f1.f0.toString();
		visitScope(n, argu);
		className = null;
	}


	@Override
	public final void visit(ClassExtendsDeclaration n, A argu)
	{
		className = n.f1.f0.toString();
		visitScope(n, argu);
		className = null;
	}
//endregion
}
