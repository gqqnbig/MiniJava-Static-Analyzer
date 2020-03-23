package numericalAnalysis;

import baseVisitors.ArgumentsCollector;
import baseVisitors.ParameterCollector;
import baseVisitors.VoidScopeVisitor;
import syntaxtree.*;
import typeAnalysis.ClassHierarchyAnalysis;
import utils.Location;
import utils.Scope;

import java.util.*;
import java.util.stream.Stream;

public class ConstraintCollector extends VoidScopeVisitor<VariableAuxiliaryData>
{
	private boolean isFirstStatement;
	List<IntIdentifierDefinition> variablesInScope;
	List<IntIdentifierDefinition> fieldsInScope;
	public List<EqualityRelationship> constraints = new ArrayList<>();


	final HashSet<IntIdentifierDefinition> writtenFields;

	public ConstraintCollector(HashSet<IntIdentifierDefinition> writtenFields)
	{
		this.writtenFields = writtenFields;
	}

	@Override
	public void visitScope(MainClass n, VariableAuxiliaryData argu)
	{
		variablesInScope = ProgramStructureCollector.getVariableIdentifiersInScope(new Scope(getClassName(), getMethodName()));
		fieldsInScope = ProgramStructureCollector.getFieldIdentifiersInScope(new Scope(getClassName(), getMethodName()));

		VariableAuxiliaryData d = new VariableAuxiliaryData(null, new Location());
		isFirstStatement = true;
		n.f15.accept(this, d);
		isFirstStatement = false;
	}

	@Override
	public void visitScope(MethodDeclaration n, VariableAuxiliaryData argu)
	{
		ParameterCollector p = new ParameterCollector();
		p.visit(n.f4);
		Set<Location> callSites = ProgramStructureCollector.getCallsites(getClassName(), getMethodName(), p.parameters.size());
		if (callSites == null)
			return; //It's an unused method.

		variablesInScope = ProgramStructureCollector.getVariableIdentifiersInScope(new Scope(getClassName(), getMethodName()));
		fieldsInScope = ProgramStructureCollector.getFieldIdentifiersInScope(new Scope(getClassName(), getMethodName()));


		Location returnStatement = new Location(n.f9);
		for (Location callSite : callSites)
		{

			isFirstStatement = true;
			n.f8.accept(this, new VariableAuxiliaryData(null, callSite));

			//return expression
			if (isFirstStatement)
			{
				for (IntIdentifierDefinition x : fieldsInScope)
				{
					VariableIn vIn = new VariableIn(x, returnStatement, callSite);
					if (writtenFields.contains(x))
						constraints.add(new EqualityRelationship(vIn, new LiteralInterval(Integer.MIN_VALUE, Integer.MAX_VALUE), "C1"));
					else
						constraints.add(new EqualityRelationship(vIn, new LiteralInterval(0, 0)));
				}
			}
			visit(n.f10, new VariableAuxiliaryData(returnStatement, callSite));
			isFirstStatement = false;
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
	public void visit(Statement n, VariableAuxiliaryData argu)
	{
		argu.statement = new Location(n);
		if (isFirstStatement)
		{
			for (IntIdentifierDefinition x : fieldsInScope)
			{
				VariableIn vIn = new VariableIn(x, argu.statement, argu.callSite);
				constraints.add(new EqualityRelationship(vIn, new LiteralInterval(Integer.MIN_VALUE, Integer.MAX_VALUE), "C1"));
			}

			for (IntIdentifierDefinition x : variablesInScope)
			{
				VariableIn vIn = new VariableIn(x, argu.statement, argu.callSite);
				constraints.add(new EqualityRelationship(vIn, new LiteralInterval(0, 0), "C2"));
			}
		}

		isFirstStatement = false;

		Stream.concat(fieldsInScope.stream(), variablesInScope.stream()).forEach(x ->
		{
			UnionFunction union = new UnionFunction();
			List<Location> successors = ProgramStructureCollector.getSuccessors(getClassName(), getMethodName(), argu.statement);
			for (Location s : successors)
			{
				union.getInput().add(new VariableIn(x, s, argu.callSite));
			}
			if (union.getInput().size() > 0)
				constraints.add(new EqualityRelationship(new VariableOut(x, argu.statement, argu.callSite), union, "C8"));
		});

		super.visit(n, argu);
	}

	@Override
	public void visit(PrintStatement n, VariableAuxiliaryData argu)
	{
		Stream.concat(fieldsInScope.stream(), variablesInScope.stream()).forEach(x ->
		{
			constraints.add(new EqualityRelationship(new VariableOut(x, argu.statement, argu.callSite), new VariableIn(x, argu.statement, argu.callSite), "C3"));
		});


		super.visit(n, argu);
	}

	@Override
	public void visit(AssignmentStatement n, VariableAuxiliaryData argu)
	{
		IntIdentifierDefinition assignee = ProgramStructureCollector.getDefinition(n.f0, new Scope(getClassName(), getMethodName()));
		if (assignee != null)
		{
			VariableOut vOut = new VariableOut(assignee, argu.statement, argu.callSite);
			VariableRes vRes = new VariableRes(n.f2, argu.statement, argu.callSite);
			constraints.add(new EqualityRelationship(vOut, vRes, "C4"));
		}


		if (n.f2.f0.choice instanceof MessageSend)
		{
			MessageSend ms = (MessageSend) n.f2.f0.choice;
			ArgumentsCollector c = new ArgumentsCollector();
			ms.f4.accept(c);
			String methodName = ms.f2.f0.toString();
			Collection<String> types = ClassHierarchyAnalysis.getPossibleTypes(ms.f0, getClassName(), methodName, c.arguments.size());

			Stream.concat(fieldsInScope.stream(), variablesInScope.stream()).filter(g -> !g.equals(assignee)).forEach(g ->
			{
				UnionFunction u = new UnionFunction();
				u.getInput().add(new VariableIn(g, argu.statement, argu.callSite));

				for (String type : types)
					u.getInput().add(new VariableOut(g, ProgramStructureCollector.getLastStatement(type, methodName), argu.statement));

				if (u.getInput().size() > 0)
					constraints.add(new EqualityRelationship(new VariableOut(g, argu.statement, argu.callSite), u, "C6"));
			});
		}
		else
		{
			Stream.concat(fieldsInScope.stream(), variablesInScope.stream()).filter(g -> !g.equals(assignee)).forEach(g ->
			{
				constraints.add(new EqualityRelationship(new VariableOut(g, argu.statement, argu.callSite), new VariableIn(g, argu.statement, argu.callSite), "C5"));
			});
		}
		super.visit(n, argu);
	}

	@Override
	public void visit(PrimaryExpression n, VariableAuxiliaryData argu)
	{
		if (n.f0.choice instanceof Identifier)
		{
			String identifierName = ((Identifier) n.f0.choice).f0.toString();
			Optional<IntIdentifierDefinition> localVariable = variablesInScope.stream().filter(o -> o.getIdentifier().equals(identifierName)).findAny();
			IntIdentifierDefinition receiver = null;
			if (localVariable.isPresent())
				receiver = localVariable.get();
			else
			{
				Optional<IntIdentifierDefinition> field = fieldsInScope.stream().filter(o -> o.getIdentifier().equals(identifierName)).findAny();
				if (field.isPresent())
					receiver = field.get();
			}

			//make sure receiver is of type int.
			if (receiver != null)
			{
				EqualityRelationship r = new EqualityRelationship(new VariableRes(n, argu.statement, argu.callSite), new VariableIn(receiver, argu.statement, argu.callSite), "C10");
				constraints.add(r);
			}
		}
		super.visit(n, argu);
	}

	@Override
	public void visit(MessageSend n, VariableAuxiliaryData argu)
	{
		UnionFunction u = new UnionFunction();
		ArgumentsCollector c = new ArgumentsCollector();
		n.f4.accept(c);
		String methodName = n.f2.f0.toString();
		Collection<String> types = ClassHierarchyAnalysis.getPossibleTypes(n.f0, getClassName(), methodName, c.arguments.size());
		Location callSite = new Location(n.f2);
		for (String type : types)
		{
			//the method may be inherited from parent.
			Expression returnExpression = ProgramStructureCollector.getReturnExpression(type, methodName);
			if (returnExpression != null)
				u.getInput().add(new VariableRes(returnExpression, ProgramStructureCollector.getLastStatement(type, methodName), callSite));
		}
		constraints.add(new EqualityRelationship(new VariableRes(n, argu.statement, argu.callSite), u, "C11"));


		for (String type : types)
		{
			for (int i = 0; i < c.arguments.size(); i++)
			{
				VariableIn vIn = new VariableIn(ProgramStructureCollector.getParameter(type, methodName, i), ProgramStructureCollector.getFirstStatement(type, methodName), callSite);
				constraints.add(new EqualityRelationship(new VariableRes(c.arguments.get(i), argu.statement, argu.callSite), vIn, "C7"));
			}
		}

		super.visit(n, argu);
	}

	@Override
	public void visit(AllocationExpression n, VariableAuxiliaryData argu)
	{
		constraints.add(new EqualityRelationship(new VariableRes(n, argu.statement, argu.callSite), LiteralInterval.NONE, "C9"));

		super.visit(n, argu);
	}

	@Override
	public void visit(IntegerLiteral n, VariableAuxiliaryData argu)
	{
		long value;
		value = Long.parseLong(n.f0.toString());
		constraints.add(new EqualityRelationship(new VariableRes(n, argu.statement, argu.callSite), new LiteralInterval(value, value), "C12"));
	}

	@Override
	public void visit(PlusExpression n, VariableAuxiliaryData argu)
	{
		constraints.add(new EqualityRelationship(new VariableRes(n, argu.statement, argu.callSite),
				new PlusInterval(new VariableRes(n.f0, argu.statement, argu.callSite), new VariableRes(n.f2, argu.statement, argu.callSite)),
				"C13"));

		super.visit(n, argu);
	}

	@Override
	public void visit(MinusExpression n, VariableAuxiliaryData argu)
	{
		constraints.add(new EqualityRelationship(new VariableRes(n, argu.statement, argu.callSite),
				new MinusInterval(new VariableRes(n.f0, argu.statement, argu.callSite), new VariableRes(n.f2, argu.statement, argu.callSite)),
				"C13"));

		super.visit(n, argu);
	}

	@Override
	public void visit(TimesExpression n, VariableAuxiliaryData argu)
	{
		constraints.add(new EqualityRelationship(new VariableRes(n, argu.statement, argu.callSite),
				new MultiplyInterval(new VariableRes(n.f0, argu.statement, argu.callSite), new VariableRes(n.f2, argu.statement, argu.callSite)),
				"C13"));

		super.visit(n, argu);
	}
}
