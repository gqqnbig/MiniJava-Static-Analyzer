package numericalAnalysis;

import baseVisitors.VoidScopeVisitor;
import syntaxtree.*;
import utils.Scope;
import visitor.DepthFirstVisitor;

import java.util.HashSet;
import java.util.List;

public class WrittenFieldsCollector extends VoidScopeVisitor<VariableAuxiliaryData>
{

	List<IntIdentifierDefinition> fieldsInScope;
	public HashSet<IntIdentifierDefinition> writtenFields = new HashSet<>();

	@Override
	public void visitScope(MainClass n, VariableAuxiliaryData argu)
	{
		fieldsInScope = ProgramStructureCollector.getFieldIdentifiersInScope(new Scope(getClassName(), getMethodName()));

		visit(n.f15, argu);
	}

	@Override
	public void visitScope(MethodDeclaration n, VariableAuxiliaryData argu)
	{
		fieldsInScope = ProgramStructureCollector.getFieldIdentifiersInScope(new Scope(getClassName(), getMethodName()));
		visit(n.f8, argu);
	}

	@Override
	public void visitScope(ClassDeclaration n, VariableAuxiliaryData argu)
	{
		visit(n.f4, null);
	}

	@Override
	protected void visitScope(ClassExtendsDeclaration n, VariableAuxiliaryData argu)
	{
		visit(n.f6, null);
	}

	@Override
	public void visit(AssignmentStatement n, VariableAuxiliaryData argu)
	{
		IntIdentifierDefinition assignee = ProgramStructureCollector.getDefinition(n.f0, new Scope(getClassName(), getMethodName()));
		if (assignee != null && assignee.Method == null)
			writtenFields.add(assignee);

	}
}
