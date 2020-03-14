import baseVisitors.VoidScopeVisitor;
import nullPointerAnalysis.ProgramStructureCollector;
import numericalAnalysis.ConstraintVariable;
import numericalAnalysis.VariableRes;
import syntaxtree.*;
import utils.FlowSensitiveVariable;
import utils.Location;
import utils.Options;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

public class NumericalAssertionChecker
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
		Options.shortform = Arrays.asList(args).contains("--short-form");
		Options.isDebug = Arrays.asList(args).contains("--debug");


		//the constructor sets static fields which affects the static Goal method.
		try {new MiniJavaParser(System.in);} catch (Throwable e) {MiniJavaParser.ReInit(System.in);}
		Goal goal = MiniJavaParser.Goal();

		ProgramStructureCollector.init(goal);
//		ClassHierarchyAnalysis.init(goal);
//		goal.accept(new AllocationVisitor());

		if (Options.isDebug)
		{
			VariableCollector variableCollector = new VariableCollector();
			goal.accept(variableCollector, null);

			debugOut.println("\nVariables:");
			for (FlowSensitiveVariable variable : variableCollector.variables)
			{
				debugOut.println(variable);
			}
		}
//		Solver.debugOut = debugOut;
//		if (Solver.checkNullPointer(goal))
//			System.out.println("null pointer error");
//		else
//			System.out.println("Has no null-pointer problems");

	}


	static class VariableCollector extends VoidScopeVisitor<Location>
	{
		ArrayList<ConstraintVariable> variables = new ArrayList<>();

		@Override
		public void visitScope(MainClass n, Location argu)
		{
			n.f15.accept(this, null);
		}

		@Override
		public void visitScope(MethodDeclaration n, Location argu)
		{
			n.f8.accept(this, null);
		}

		@Override
		public void visitScope(ClassDeclaration n, Location argu)
		{
			n.f4.accept(this, null);
		}

		@Override
		protected void visitScope(ClassExtendsDeclaration n, Location argu)
		{
			n.f6.accept(this, null);
		}


		@Override
		public void visit(MessageSend n, Location argu)
		{
			variables.add(new VariableRes(n, argu, new Location()));
			super.visit(n, argu);
		}


		@Override
		public void visit(Statement n, Location argu)
		{
//		List<utils.ObjectIdentifierDefinition> nullables = NullableCollector.getNullableIdentifierInScope(new Scope(getClassName(), getMethodName()));

			Location location = new Location(n);
//			for (ObjectIdentifierDefinition nullable : nullablesInScope)
//			{
//				variables.add(new VariableIn(nullable, location));
//				variables.add(new VariableOut(nullable, location));
//			}

			super.visit(n, location);
		}


		@Override
		public void visit(AllocationExpression n, Location argu)
		{
			variables.add(new VariableRes(n, argu, new Location()));
		}
	}
}


