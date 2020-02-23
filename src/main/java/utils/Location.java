package utils;

import baseVisitors.StatementStartVisitor;
import syntaxtree.*;

import java.util.Objects;

public class Location implements Comparable<Location>
{

	private int line;
	private int column;

//	public Location(VarDeclaration statement)
//	{
//		Node choice = statement.f0.f0.choice;
//		if (choice instanceof ArrayType)
//		{
//			line = ((ArrayType) choice).f0.beginLine;
//			column = ((ArrayType) choice).f0.beginColumn;
//		}
//		else if (choice instanceof BooleanType)
//		{
//			line = ((BooleanType) choice).f0.beginLine;
//			column = ((BooleanType) choice).f0.beginColumn;
//		}
//		else if (choice instanceof IntegerType)
//		{
//			line = ((IntegerType) choice).f0.beginLine;
//			column = ((IntegerType) choice).f0.beginColumn;
//		}
//		else
//		{
//			assert choice instanceof Identifier;
//			line = ((Identifier) choice).f0.beginLine;
//			column = ((Identifier) choice).f0.beginColumn;
//		}
//	}


	public Location(Statement statement)
	{
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
