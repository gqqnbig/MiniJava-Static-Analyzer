package baseVisitors;

import syntaxtree.MessageSend;
import visitor.DepthFirstVisitor;

import java.util.ArrayList;

public class MessageSendCollector extends DepthFirstVisitor
{
	public final ArrayList<MessageSend> messageSends = new ArrayList<>();

	@Override
	public void visit(MessageSend n)
	{
		messageSends.add(n);
		super.visit(n);
	}
}
