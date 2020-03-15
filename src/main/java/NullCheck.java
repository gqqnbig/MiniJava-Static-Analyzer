import baseVisitors.AllocationVisitor;
import baseVisitors.VoidScopeVisitor;
import nullPointerAnalysis.*;
import syntaxtree.*;
import typeAnalysis.ClassHierarchyAnalysis;
import utils.*;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		Options.shortform = Arrays.asList(args).contains("--short-form");
		Options.isDebug = Arrays.asList(args).contains("--debug");


		//the constructor sets static fields which affects the static Goal method.
		try {new MiniJavaParser(System.in);} catch (Throwable e) {MiniJavaParser.ReInit(System.in);}
		Goal goal = MiniJavaParser.Goal();

		ProgramStructureCollector.init(goal);
		ClassHierarchyAnalysis.init(goal);
		//Initialize AllocationVisitor.usedClasses so that we can skip analyzing ununsed classes.
		goal.accept(new AllocationVisitor());

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
		Solver.debugOut = debugOut;
		if (Solver.checkNullPointer(goal))
			System.out.println("null pointer error");
//		else
//			System.out.println("Has no null-pointer problems");

	}


	/**
	 * Collect mathematical variables for solving constraints.
	 * The variable doesn't refer to variables in programming.
	 */
	static class VariableCollector extends VoidScopeVisitor<Location>
	{
		ArrayList<FlowSensitiveVariable> variables = new ArrayList<>();

		List<ObjectIdentifierDefinition> nullablesInScope;

		@Override
		public void visitScope(MainClass n, Location argu)
		{
//		n.f14.accept(this, null);

			nullablesInScope = ProgramStructureCollector.getNullableIdentifiersInScope(new Scope(getClassName(), getMethodName()));
			n.f15.accept(this, null);
		}

		@Override
		public void visitScope(MethodDeclaration n, Location argu)
		{
//		n.f7.accept(this, null);

			nullablesInScope = ProgramStructureCollector.getNullableIdentifiersInScope(new Scope(getClassName(), getMethodName()));
			n.f8.accept(this, null);

			//return statement
			Location returnLocation = new Location(n.f9);
			for (ObjectIdentifierDefinition nullable : nullablesInScope)
			{
				variables.add(new VariableIn(nullable, returnLocation));
				variables.add(new VariableOut(nullable, returnLocation));
			}

			n.f10.accept(this, new Location(n.f9));
		}

		@Override
		public void visitScope(ClassDeclaration n, Location argu)
		{
			if (AllocationVisitor.usedClasses.contains(getClassName()))
				n.f4.accept(this, null);
		}

		@Override
		protected void visitScope(ClassExtendsDeclaration n, Location argu)
		{
			if (AllocationVisitor.usedClasses.contains(getClassName()))
				n.f6.accept(this, null);
		}


		@Override
		public void visit(MessageSend n, Location argu)
		{
			variables.add(new VariableRes(n, argu));
			super.visit(n, argu);
		}

		@Override
		public void visit(Statement n, Location argu)
		{
			Location location = new Location(n);
			for (ObjectIdentifierDefinition nullable : nullablesInScope)
			{
				variables.add(new VariableIn(nullable, location));
				variables.add(new VariableOut(nullable, location));
			}

			super.visit(n, location);
		}

		@Override
		public void visit(Expression n, Location argu)
		{
			assert argu != null;

			VariableRes vRes = new VariableRes(n, argu);
			variables.add(vRes);

			super.visit(n, argu);
		}

		@Override
		public void visit(PrimaryExpression n, Location argu)
		{
			if (n.f0.choice instanceof Identifier)
				variables.add(new VariableRes(n, argu));
			else if (n.f0.choice instanceof NotExpression)
				variables.add(new VariableRes(n, argu));
			else if (n.f0.choice instanceof BracketExpression)
				variables.add(new VariableRes(n, argu));

			super.visit(n, argu);
		}

		@Override
		public void visit(AllocationExpression n, Location argu)
		{
			variables.add(new VariableRes(n, argu));
		}
	}
}
