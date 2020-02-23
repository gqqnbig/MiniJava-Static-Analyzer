package baseVisitors;

import syntaxtree.ArrayLookup;
import visitor.DepthFirstVisitor;

import java.util.ArrayList;

public class ArrayLookupVisitor extends DepthFirstVisitor
{
	public ArrayList<ArrayLookup> arrayLookups = new ArrayList<>();

	@Override
	public void visit(ArrayLookup n)
	{
		arrayLookups.add(n);
		super.visit(n);
	}
}
