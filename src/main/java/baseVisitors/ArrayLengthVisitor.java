package baseVisitors;

import syntaxtree.ArrayLength;
import visitor.DepthFirstVisitor;

import java.util.ArrayList;

public class ArrayLengthVisitor extends DepthFirstVisitor
{
	public ArrayList<ArrayLength> arrayLengths = new ArrayList<>();

	@Override
	public void visit(ArrayLength n)
	{
		arrayLengths.add(n);
		super.visit(n);
	}
}
