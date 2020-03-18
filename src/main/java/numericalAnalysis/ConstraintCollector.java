package numericalAnalysis;

import baseVisitors.ParameterCollector;
import baseVisitors.VoidScopeVisitor;
import syntaxtree.*;
import utils.Location;
import utils.Scope;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class ConstraintCollector extends VoidScopeVisitor<VariableAuxiliaryData>
{
	private boolean isFirstStatement;
	List<IntIdentifierDefinition> variablesInScope;
	List<IntIdentifierDefinition> fieldsInScope;
	public List<EqualityRelationship> constraints = new ArrayList<>();

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
		variablesInScope = ProgramStructureCollector.getVariableIdentifiersInScope(new Scope(getClassName(), getMethodName()));
		fieldsInScope = ProgramStructureCollector.getFieldIdentifiersInScope(new Scope(getClassName(), getMethodName()));

		ParameterCollector p = new ParameterCollector();
		n.f4.accept(p);

		Set<Location> callSites = ProgramStructureCollector.getCallsites(getClassName(), getMethodName(), p.parameters.size());
		for (Location callSite : callSites)
		{
			VariableAuxiliaryData d = new VariableAuxiliaryData(null, callSite);

			isFirstStatement = true;
			n.f8.accept(this, d);
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

		}
		else
		{
			Stream.concat(fieldsInScope.stream(), variablesInScope.stream()).filter(g->!g.equals(assignee)).forEach(g ->
			{
				constraints.add(new EqualityRelationship(new VariableOut(g, argu.statement, argu.callSite), new VariableIn(g, argu.statement, argu.callSite), "C5"));
			});
		}
		super.visit(n, argu);
	}
}
