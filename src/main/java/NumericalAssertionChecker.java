import baseVisitors.AllocationVisitor;
import baseVisitors.ParameterCollector;
import baseVisitors.VoidScopeVisitor;
import numericalAnalysis.*;
import syntaxtree.*;
import typeAnalysis.ClassHierarchyAnalysis;
import utils.*;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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

		Solver solver=new Solver();
		solver.debugOut = debugOut;
		if (solver.alwaysGreaterThan0(goal))
			System.out.println("The program prints only integers that are greater than zero");
		else
			System.out.println("The program prints at least one integer that is less than or equal to zero");

	}


	static class VariableCollector extends VoidScopeVisitor<VariableAuxiliaryData>
	{
		ArrayList<ConstraintVariable> variables = new ArrayList<>();
		List<IntIdentifierDefinition> integersInScope;


		@Override
		public void visitScope(MainClass n, VariableAuxiliaryData argu)
		{
			integersInScope = ProgramStructureCollector.getNullableIdentifiersInScope(new Scope(getClassName(), getMethodName()));

			VariableAuxiliaryData d = new VariableAuxiliaryData(null, new Location());
			n.f15.accept(this, d);
		}

		@Override
		public void visitScope(MethodDeclaration n, VariableAuxiliaryData argu)
		{
			integersInScope = ProgramStructureCollector.getNullableIdentifiersInScope(new Scope(getClassName(), getMethodName()));

			ParameterCollector p = new ParameterCollector();
			n.f4.accept(p);

			Set<Location> callSites = ProgramStructureCollector.getCallsites(getClassName(), getMethodName(), p.parameters.size());
			for (Location callSite : callSites)
			{
				VariableAuxiliaryData d = new VariableAuxiliaryData(null, callSite);
				n.f8.accept(this, d);
			}
		}

		@Override
		public void visitScope(ClassDeclaration n, VariableAuxiliaryData argu)
		{
			n.f4.accept(this, null);
		}

		@Override
		protected void visitScope(ClassExtendsDeclaration n, VariableAuxiliaryData argu)
		{
			n.f6.accept(this, null);
		}


		@Override
		public void visit(MessageSend n, VariableAuxiliaryData argu)
		{
			variables.add(new VariableRes(n, argu.statement, argu.callSite));
			super.visit(n, argu);
		}


		@Override
		public void visit(Statement n, VariableAuxiliaryData argu)
		{
			argu.statement = new Location(n);
			for (var nullable : integersInScope)
			{
				variables.add(new VariableIn(nullable, argu.statement, argu.callSite));
				variables.add(new VariableOut(nullable, argu.statement, argu.callSite));
			}

			super.visit(n, argu);
		}

		@Override
		public void visit(Expression n, VariableAuxiliaryData argu)
		{
			assert argu != null;

			VariableRes vRes = new VariableRes(n, argu.statement, argu.callSite);
			variables.add(vRes);

			super.visit(n, argu);
		}

		@Override
		public void visit(AllocationExpression n, VariableAuxiliaryData argu)
		{
			variables.add(new VariableRes(n, argu.statement, argu.callSite));
		}
	}
}


