import baseVisitors.AllocationVisitor;
import nullPointerAnalysis.*;
import syntaxtree.*;
import typeAnalysis.ClassHierarchyAnalysis;
import nullPointerAnalysis.Solver;
import utils.FlowSensitiveVariable;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;

public class NullCheck
{
	public static PrintStream debugOut;

	public static void main(String[] args) throws ParseException
	{
		//OutputStream.nullOutputStream() is available in JDK 11.
		PrintStream nullOutputStream = new PrintStream(new OutputStream()
		{
			public void write(int b) { }
		});
		if (args.length == 0 || Arrays.asList(args).contains("--debug") == false)
		{
			debugOut = nullOutputStream;
		}
		else
		{
			//debug mode
			debugOut = System.out;
//			System.setOut(nullOutputStream);
		}

		//the constructor sets static fields which affects the static Goal method.
		try {new MiniJavaParser(System.in);} catch (Throwable e) {MiniJavaParser.ReInit(System.in);}
		Goal goal = MiniJavaParser.Goal();

		ProgramStructureCollector.init(goal);
		ClassHierarchyAnalysis.init(goal);
		goal.accept(new AllocationVisitor());

		if (Solver.checkNullPointer(goal))
			System.out.println("null pointer error");
//		else
//			System.out.println("Has no null-pointer problems");

	}


}
