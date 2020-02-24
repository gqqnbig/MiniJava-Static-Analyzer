package baseVisitors;

import syntaxtree.*;
import utils.Scope;
import visitor.GJDepthFirst;

import java.util.HashMap;
import java.util.Map;

/**
 * Get the string content of a node, and fully qualify identifiers.
 */
public class ExpressionToStringVisitor extends GJDepthFirst<String, Object>
{

	@Override
	public String visit(MessageSend n, Object argu)
	{
		String optional = n.f4.accept(this, argu);
		if (optional == null)
			optional = "";
		String primaryExpressionString = n.f0.accept(this, argu);
//		Integer l = messageReceiverLabellings.get(n.f0);
//		if (l != null)
//			primaryExpressionString += "#" + l.intValue();
		return primaryExpressionString + "." + n.f2.accept(this, argu) + "(" + optional + ")";
	}

	@Override
	public String visit(Expression n, Object argu)
	{
		return n.f0.accept(this, argu);
	}

	//region  MessageSend


	@Override
	public String visit(PrimaryExpression n, Object argu)
	{
//		Integer l = messageReceiverLabellings.get(n);
////		String receiverSuffix = l == null ? "" : "#" + l.toString();
//
//		if (idQualifications != null && n.f0.choice instanceof Identifier)
//		{
//			Scope varScope = idQualifications.get((Identifier) n.f0.choice);
//
//			if (varScope == null)
//			{
//				NodeToken token = ((Identifier) n.f0.choice).f0;
//				assert false : token.toString() + " is not captured as ID on line " + token.beginLine + " column " + token.beginColumn;
//			}
//
//			return varScope.qualify((Identifier) n.f0.choice);
//		}
		return n.f0.accept(this, argu);
	}

	@Override
	public String visit(IntegerLiteral n, Object argu)
	{
		return n.f0.toString();
	}

	@Override
	public String visit(TrueLiteral n, Object argu)
	{
		return n.f0.toString();
	}

	@Override
	public String visit(FalseLiteral n, Object argu)
	{
		return n.f0.toString();
	}

	@Override
	public String visit(Identifier n, Object argu)
	{
		return n.f0.toString();
	}

	@Override
	public String visit(ThisExpression n, Object argu)
	{
//		assert className != null : "Must replace keyword `this` with to the actual class name.";
		return "this";
	}

	@Override
	public String visit(ArrayAllocationExpression n, Object argu)
	{
		return "new int[" + n.f3.accept(this, argu) + "]";
	}

	@Override
	public String visit(AllocationExpression n, Object argu)
	{
		return "new " + n.f1.f0.toString() + "()";
	}


	@Override
	public String visit(NotExpression n, Object argu)
	{
		return "! " + n.f1.accept(this, argu);
	}

	@Override
	public String visit(BracketExpression n, Object argu)
	{
		return "(" + n.f1.accept(this, argu) + ")";
	}

//endregion


	@Override
	public String visit(AndExpression n, Object argu)
	{
		return n.f0.accept(this, argu) + " && " + n.f2.accept(this, argu);
	}

	@Override
	public String visit(CompareExpression n, Object argu)
	{
		return n.f0.accept(this, argu) + " < " + n.f2.accept(this, argu);
	}


	@Override
	public String visit(PlusExpression n, Object argu)
	{
		return n.f0.accept(this, argu) + " + " + n.f2.accept(this, argu);
	}


	@Override
	public String visit(TimesExpression n, Object argu)
	{
		return n.f0.accept(this, argu) + " * " + n.f2.accept(this, argu);
	}


	@Override
	public String visit(ArrayLookup n, Object argu)
	{
		return n.f0.accept(this, argu) + " < " + n.f2.accept(this, argu);
	}


	@Override
	public String visit(ArrayLength n, Object argu)
	{
		return n.f0.accept(this, argu) + ".length";
	}

	@Override
	public String visit(ExpressionList n, Object argu)
	{
		String a = n.f0.accept(this, argu);
		String b = n.f1.accept(this, argu);
		if (b == null)
			return a;
		else
			return a + b;
	}

	@Override
	public String visit(ExpressionRest n, Object argu)
	{
		return ", " + n.f1.accept(this, argu);
	}
}
