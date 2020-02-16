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
//	private final static HashMap<Node, String> expressionMapping = new HashMap<>();

	/**
	 * Look up cache and return the expression of the node.
	 *
	 * @param n
	 * @param idQualifications
	 * @return
	 */
//	public static String getExpression(Node n, Map<Identifier, Scope> idQualifications, Map<PrimaryExpression, Integer> messageReceiverLabellings)
//	{
//		return getExpression(n, ScopeVisitor.getScope(n), idQualifications, messageReceiverLabellings);
//	}
//
//	public static String getExpression(Node n, Scope scope, Map<Identifier, Scope> idQualifications, Map<PrimaryExpression, Integer> messageReceiverLabellings)
//	{
//		String expression = expressionMapping.get(n);
//		if (expression != null)
//			return expression;
//
//		ExpressionToStringVisitor visitor = new ExpressionToStringVisitor(scope, idQualifications, messageReceiverLabellings);
//		expression = n.accept(visitor, null);
//
//
//		if (expression != null)
//			assert expression.chars().filter(c -> c == '.').count() < 3 || messageReceiverLabellings.size() > 0 :
//					String.format("%s is a message send, but we cannot find it in messageReceiverLabellings", expression);
//		else
//			assert n instanceof NodeOptional;
//
//		expressionMapping.put(n, expression);
//		return expression;
//	}


	/**
	 * Cache is based on messageReceiverLabellings and idQualifications. If any of them changes, the expression may change. Therefore we have to clear the cache.
	 */
//	public static void clearCache()
//	{
//		expressionMapping.clear();
//	}


//	private final String className;
//	private final Map<Identifier, Scope> idQualifications;
//	private final Map<PrimaryExpression, Integer> messageReceiverLabellings;
//	private boolean appendMessageReceiverLabel = false;
//
//	public ExpressionToStringVisitor(String className, Map<Identifier, Scope> idQualifications, Map<PrimaryExpression, Integer> messageReceiverLabellings)
//	{
//		this.className = className;
//		this.idQualifications = idQualifications;
//		this.messageReceiverLabellings = messageReceiverLabellings;
//	}

//	public ExpressionToStringVisitor(Scope scope, Map<Identifier, Scope> idQualifications, Map<PrimaryExpression, Integer> messageReceiverLabellings)
//	{
//		if (scope != null)
//			this.className = scope.Class;
//		else
//			this.className = null;
//		this.idQualifications = idQualifications;
//		this.messageReceiverLabellings = messageReceiverLabellings;
//	}

	public ExpressionToStringVisitor()
	{

	}

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

//	@Override
//	public String visit(ExpressionList n, Object argu)
//	{
//		String a = n.f0.accept(this, argu);
//		String b = n.f1.accept(this, argu);
//		if (b == null)
//			return a;
//		else
//			return a + b;
//	}
//
//	@Override
//	public String visit(ExpressionRest n, Object argu)
//	{
//		return ", " + n.f1.accept(this, argu);
//	}
}
