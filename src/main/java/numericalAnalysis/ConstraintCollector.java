package numericalAnalysis;

import baseVisitors.ParameterCollector;
import baseVisitors.VoidScopeVisitor;
import syntaxtree.*;
import utils.Location;
import utils.Scope;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConstraintCollector extends VoidScopeVisitor<VariableAuxiliaryData>
{
	private boolean isFirstStatement;
	List<IntIdentifierDefinition> integersInScope;
	public List<EqualityRelationship> constraints = new ArrayList<>();

	@Override
	public void visitScope(MainClass n, VariableAuxiliaryData argu)
	{
		integersInScope = ProgramStructureCollector.getNullableIdentifiersInScope(new Scope(getClassName(), getMethodName()));

		VariableAuxiliaryData d = new VariableAuxiliaryData(null, new Location());
		isFirstStatement = true;
		n.f15.accept(this, d);
		isFirstStatement = false;
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
			for (IntIdentifierDefinition x : integersInScope)
			{
				VariableIn vIn = new VariableIn(x, argu.statement, argu.callSite);
				constraints.add(new EqualityRelationship(vIn, new LiteralInterval(Integer.MIN_VALUE, Integer.MAX_VALUE)));

			}

		}


		isFirstStatement = false;
		super.visit(n, argu);
	}
}
