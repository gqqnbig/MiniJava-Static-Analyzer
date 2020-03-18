package utils;

import baseVisitors.StatementStartVisitor;
import syntaxtree.Identifier;
import syntaxtree.NodeToken;
import syntaxtree.Statement;

import java.util.Objects;

/**
 * represents line and column number.
 */
public class Location implements Comparable<Location>
{

	private int line;
	private int column;

	/**
	 * An unknown location, for example the line calls the main method.
	 */
	public Location()
	{
		line = -1;
		column = -1;
	}

	public Location(Statement statement)
	{
//		assert statement.f0.choice instanceof Block == false : "Block statement is not allowed";

		StatementStartVisitor statementStartVisitor = new StatementStartVisitor();
		statement.accept(statementStartVisitor);

		line = statementStartVisitor.line;
		column = statementStartVisitor.column;
	}

	public Location(NodeToken returnStatement)
	{
		assert returnStatement.toString().equals("return");

		line = returnStatement.beginLine;
		column = returnStatement.beginColumn;
	}

	public Location(Identifier methodName)
	{
		line = methodName.f0.beginLine;
		column = methodName.f0.beginColumn;
	}

	public int getLine()
	{
		return line;
	}

	public int getColumn()
	{
		return column;
	}

	@Override
	public String toString()
	{
		if (line == -1)
			return "unknown";

		if (Options.shortform)
			return "L" + line;
		else
			return String.format("L%dC%d", line, column);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Location location = (Location) o;
		return line == location.line &&
				column == location.column;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(line, column);
	}

	@Override
	public int compareTo(Location o)
	{
		int r = Integer.compare(line, o.line);
		if (r != 0)
			return r;

		return Integer.compare(column, o.column);
	}
}
