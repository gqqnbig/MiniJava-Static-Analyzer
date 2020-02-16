package baseVisitors;

import syntaxtree.*;
import visitor.DepthFirstVisitor;

public class StatementStartVisitor extends DepthFirstVisitor
{
	public int line;
	public int column;

	@Override
	public void visit(AssignmentStatement n)
	{
		line = n.f0.f0.beginLine;
		column = n.f0.f0.beginColumn;
	}

	@Override
	public void visit(ArrayAssignmentStatement n)
	{
		line = n.f0.f0.beginLine;
		column = n.f0.f0.beginColumn;
	}

	@Override
	public void visit(IfStatement n)
	{
		line = n.f0.beginLine;
		column = n.f0.beginColumn;
	}

	@Override
	public void visit(WhileStatement n)
	{
		line = n.f0.beginLine;
		column = n.f0.beginColumn;
	}

	@Override
	public void visit(PrintStatement n)
	{
		line = n.f0.beginLine;
		column = n.f0.beginColumn;
	}
}
