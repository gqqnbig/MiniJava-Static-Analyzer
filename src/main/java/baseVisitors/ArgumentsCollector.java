package baseVisitors;

import syntaxtree.Expression;
import visitor.DepthFirstVisitor;

import java.util.ArrayList;

public class ArgumentsCollector extends DepthFirstVisitor
{
	public ArrayList<Expression> arguments=new ArrayList<>();

	@Override
	public void visit(Expression n)
	{
		arguments.add(n);
	}
}
