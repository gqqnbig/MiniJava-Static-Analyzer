import baseVisitors.AllocationVisitor;
import baseVisitors.ParameterCollector;
import baseVisitors.VoidScopeVisitor;
import numericalAnalysis.*;
import syntaxtree.*;
import typeAnalysis.ClassHierarchyAnalysis;
import utils.*;
import visitor.DepthFirstVisitor;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
		WrittenFieldsCollector writtenFieldsCollector = new WrittenFieldsCollector();
		writtenFieldsCollector.visit(goal, null);

		ConstraintCollector constraintCollector = new ConstraintCollector(writtenFieldsCollector.writtenFields);
		goal.accept(constraintCollector, null);
		debugOut.println("\nConstraints:");
		for (EqualityRelationship r : constraintCollector.constraints)
		{
			debugOut.println(r);
		}

		Solver solver = new Solver();
		solver.debugOut = debugOut;
		List<EqualityRelationship> solutions = solver.solve(goal, constraintCollector.constraints).stream().filter(r -> r.left instanceof VariableRes).collect(Collectors.toList());

		PrintArgumentCollector printArgumentCollector = new PrintArgumentCollector();
		goal.accept(printArgumentCollector);

		for (Expression exp : printArgumentCollector.printArguments)
		{
			Node node = VariableRes.diveInto(exp);
			if (solutions.stream().filter(r -> ((VariableRes) r.left).getExpression() == node).allMatch(r -> ((LiteralInterval) r.right).lowerBound > 0) == false)
			{
				System.out.println("The program prints at least one integer that is less than or equal to zero");
				return;
			}
		}
		System.out.println("The program prints only integers that are greater than zero");

	}


	static class VariableCollector extends VoidScopeVisitor<VariableAuxiliaryData>
	{
		ArrayList<ConstraintVariable> variables = new ArrayList<>();
		List<IntIdentifierDefinition> integersInScope;


		@Override
		public void visitScope(MainClass n, VariableAuxiliaryData argu)
		{
			integersInScope = ProgramStructureCollector.getIntegerIdentifiersInScope(new Scope(getClassName(), getMethodName()));

			VariableAuxiliaryData d = new VariableAuxiliaryData(null, new Location());
			n.f15.accept(this, d);
		}

		@Override
		public void visitScope(MethodDeclaration n, VariableAuxiliaryData argu)
		{
			ParameterCollector p = new ParameterCollector();
			n.f4.accept(p);

			Set<Location> callSites = ProgramStructureCollector.getCallsites(getClassName(), getMethodName(), p.parameters.size());
			if (callSites == null)
				return;

			integersInScope = ProgramStructureCollector.getIntegerIdentifiersInScope(new Scope(getClassName(), getMethodName()));


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
			for (IntIdentifierDefinition nullable : integersInScope)
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

	static class PrintArgumentCollector extends DepthFirstVisitor
	{
		List<Expression> printArguments = new ArrayList<>();

		@Override
		public void visit(PrintStatement n)
		{
			printArguments.add(n.f2);
		}
	}
}


