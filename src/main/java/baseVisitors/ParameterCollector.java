package baseVisitors;

import syntaxtree.FormalParameter;
import visitor.DepthFirstVisitor;

import java.util.ArrayList;

public class ParameterCollector extends DepthFirstVisitor
{
	public ArrayList<FormalParameter> parameters = new ArrayList<>();

	@Override
	public void visit(FormalParameter n)
	{
		parameters.add(n);
	}
}
