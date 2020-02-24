package baseVisitors;

import syntaxtree.AllocationExpression;
import syntaxtree.MainClass;
import visitor.DepthFirstVisitor;

import java.util.HashSet;

public class AllocationVisitor extends DepthFirstVisitor
{
	public HashSet<String> usedClasses = new HashSet<>();

	@Override
	public void visit(AllocationExpression n)
	{
		usedClasses.add(n.f1.f0.toString());
	}

	@Override
	public void visit(MainClass n)
	{
		usedClasses.add(n.f1.f0.toString());
		super.visit(n);
	}
}
