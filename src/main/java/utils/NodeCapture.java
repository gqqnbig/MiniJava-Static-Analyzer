package utils;

public class NodeCapture<T>
{
	public final T node;
	public final Scope scope;


	public NodeCapture(T node, Scope scope)
	{
		this.node = node;
		this.scope = scope;
	}
}
