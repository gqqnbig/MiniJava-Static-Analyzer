package nullPointerAnalysis;

import baseVisitors.AllocationVisitor;
import baseVisitors.ArgumentsCollector;
import baseVisitors.VoidScopeVisitor;
import syntaxtree.*;
import typeAnalysis.ClassHierarchyAnalysis;
import utils.Location;
import utils.ObjectIdentifierDefinition;
import utils.Scope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ConstraintCollector extends VoidScopeVisitor<Location>
{
	private boolean isFirstStatement;

	List<ObjectIdentifierDefinition> nullablesInScope;

	public List<EqualityRelationship> constraints = new ArrayList<>();

	@Override
	public void visitScope(MainClass n, Location argu)
	{
		nullablesInScope = ProgramStructureCollector.getNullableIdentifiersInScope(new Scope(getClassName(), getMethodName()));
		isFirstStatement = true;
		n.f15.accept(this, null);

		assert n.f15.present() == false || isFirstStatement == false : "If method body has statements, isFirstStatement must already be turned off.";
		isFirstStatement = false;
	}

	@Override
	public void visitScope(MethodDeclaration n, Location argu)
	{
//		n.f7.accept(this, null);

		nullablesInScope = ProgramStructureCollector.getNullableIdentifiersInScope(new Scope(getClassName(), getMethodName()));
		isFirstStatement = true;
		n.f8.accept(this, null);
		assert n.f8.present() == false || isFirstStatement == false : "If method body has statements, isFirstStatement must already be turned off.";

		//return statement
		Location returnLocation = new Location(n.f9);
		if (isFirstStatement)
		{
			for (ObjectIdentifierDefinition nullable : nullablesInScope)
			{
				EqualityRelationship r = new EqualityRelationship();
				r.left = new VariableIn(nullable, returnLocation);
				r.right = PossibleNullLiteral.instance;
				constraints.add(r);
			}
		}
		addC4(returnLocation, null);
		n.f10.accept(this, returnLocation);
		isFirstStatement = false;
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
	public void visit(Statement n, Location argu)
	{
		if (n.f0.choice instanceof Block)
		{
			n.f0.accept(this, null);
			return;
		}

		Location location = new Location(n);
		if (isFirstStatement)
		{
			//If there are no available nullables in the first statement, there will not be any nullables in the subsequent statements.
			for (ObjectIdentifierDefinition nullable : nullablesInScope)
			{
				EqualityRelationship r = new EqualityRelationship();
				r.left = new VariableIn(nullable, location);
				r.right = PossibleNullLiteral.instance;
				constraints.add(r);
			}
		}
		isFirstStatement = false;


		//C7
		for (ObjectIdentifierDefinition nullable : nullablesInScope)
		{
			UnionFunction union = new UnionFunction();
			List<Location> successors = ProgramStructureCollector.getSuccessors(getClassName(), getMethodName(), location);
			for (Location s : successors)
			{
				VariableIn vIn = new VariableIn(nullable, s);
				union.getInput().add(vIn);
			}
			if (union.getInput().size() > 0)
			{
				EqualityRelationship r = new EqualityRelationship();
				r.left = new VariableOut(nullable, location);
				r.right = union;
				r.comment = "C7";
				constraints.add(r);
			}
		}


		super.visit(n, location);
	}

	@Override
	public void visit(PrintStatement n, Location argu)
	{
		for (ObjectIdentifierDefinition nullable : nullablesInScope)
		{
			EqualityRelationship r = new EqualityRelationship();
			r.left = new VariableOut(nullable, argu);
			r.right = new VariableIn(nullable, argu);
			constraints.add(r);
		}
		n.f2.accept(this, argu);
	}

	@Override
	public void visit(AssignmentStatement n, Location location)
	{
		Scope scope = new Scope(getClassName(), getMethodName());
		ObjectIdentifierDefinition assignee = ProgramStructureCollector.getDefinition(n.f0, scope);
		if (assignee != null)
		{
			VariableOut vOut = new VariableOut(assignee, location);
			VariableRes vRes = new VariableRes(n.f2, location);

			constraints.add(new EqualityRelationship(vOut, vRes, "C3"));
		}

		if (n.f2.f0.choice instanceof MessageSend)
		{
			String methodName = ((MessageSend) n.f2.f0.choice).f2.f0.toString();
			ArgumentsCollector argumentsCollector = new ArgumentsCollector();
			((MessageSend) (n.f2.f0.choice)).f4.accept(argumentsCollector);
			ArrayList<Expression> arguments = argumentsCollector.arguments;
			Collection<String> possibleTypes = ClassHierarchyAnalysis.getPossibleTypes(n.f0, methodName, arguments.size());
			assert possibleTypes != null && possibleTypes.size() > 0;

			addC5(location, assignee, methodName, possibleTypes);


			for (int i = 0; i < arguments.size(); i++)
			{
				Expression argument = arguments.get(i);
				EqualityRelationship r = new EqualityRelationship();
				r.left = new VariableRes(argument, location);
				r.comment = "C6";

				UnionFunction union = new UnionFunction();
				for (String type : possibleTypes)
				{
					ObjectIdentifierDefinition parameter = ProgramStructureCollector.getParameter(type, methodName, i);
					if (parameter != null)
					{
						VariableIn vIn = new VariableIn(parameter, ProgramStructureCollector.getFirstStatement(type, methodName));
						union.getInput().add(vIn);
					}
				}
				if (union.getInput().size() > 0)
				{
					r.right = union;
					constraints.add(r);
				}
			}
		}
		else
		{
			addC4(location, assignee);
		}


		n.f2.accept(this, location);
	}

	private void addC4(Location location, ObjectIdentifierDefinition assignee)
	{
		for (ObjectIdentifierDefinition g : nullablesInScope)
		{
			if (g.equals(assignee))
				continue;

			EqualityRelationship r = new EqualityRelationship();
			r.left = new VariableOut(g, location);
			r.right = new VariableIn(g, location);
			r.comment = "C4";
			constraints.add(r);
		}
	}

	/**
	 * For an assignment statement x = e, where e is a call x1.m(x2):
	 * out[g, n] = in[g, n] ⊔ (⊔(C,m) ∈ CHA(x1) out[g, last(C, m)] )
	 *
	 * @param argu
	 * @param assignee
	 * @param methodName
	 * @param possibleTypes
	 */
	private void addC5(Location argu, ObjectIdentifierDefinition assignee, String methodName, Collection<String> possibleTypes)
	{
		for (ObjectIdentifierDefinition g : nullablesInScope)
		{
			if (g.equals(assignee))
				continue;

			EqualityRelationship r = new EqualityRelationship();
			r.left = new VariableOut(g, argu);

			UnionFunction union = new UnionFunction();
			union.getInput().add(new VariableIn(g, argu));

			for (String className : possibleTypes)
			{
				union.getInput().add(new VariableOut(g, ProgramStructureCollector.getLastStatement(className, methodName)));
			}
			r.right = union;
			r.comment = "C5";
			constraints.add(r);
		}
	}

//	@Override
//	public void visit(AllocationExpression n, Location argu)
//	{
//		EqualityRelationship r = new EqualityRelationship();
//		r.left = new VariableRes(n, argu);
//		r.right = new NotNullLiteral();
//		r.comment="C8";
//		constraints.add(r);
//	}


	@Override
	public void visit(MessageSend n, Location argu)
	{
		EqualityRelationship r = new EqualityRelationship();
		r.comment = "C10";

		r.left = new VariableRes(n, argu);
		UnionFunction union = new UnionFunction();

		ArgumentsCollector argumentsCollector = new ArgumentsCollector();
		n.f4.accept(argumentsCollector);
		String methodName = n.f2.f0.toString();
		Collection<String> possibleTypes = ClassHierarchyAnalysis.getPossibleTypes(n.f0, methodName, argumentsCollector.arguments.size());
		for (String type : possibleTypes)
		{
			VariableRes vRes = new VariableRes(ProgramStructureCollector.getReturnExpression(type, methodName), ProgramStructureCollector.getLastStatement(type, methodName));
			union.getInput().add(vRes);
		}
		r.right = union;
		constraints.add(r);


		super.visit(n, argu);
	}

	@Override
	public void visit(PrimaryExpression n, Location argu)
	{
		if (n.f0.choice instanceof BracketExpression)
		{
			EqualityRelationship r = new EqualityRelationship();
			r.left = new VariableRes(n, argu);
			r.right = new VariableRes(((BracketExpression) n.f0.choice).f1, argu);
			if (r.left.equals(r.right) == false)
				constraints.add(r);
		}
		else if (n.f0.choice instanceof AllocationExpression)
		{
			EqualityRelationship r = new EqualityRelationship();
			r.left = new VariableRes((AllocationExpression) n.f0.choice, argu);
			r.right = NotNullLiteral.instance;
			r.comment = "C8";
			constraints.add(r);
		}
		else if (n.f0.choice instanceof Identifier)
		{
			String identifierName = ((Identifier) n.f0.choice).f0.toString();
			Optional<ObjectIdentifierDefinition> localVariable = nullablesInScope.stream().filter(o -> o.getIdentifier().equals(identifierName) && o.Method != null).findAny();
			ObjectIdentifierDefinition receiver = null;
			if (localVariable.isPresent())
			{
				receiver = localVariable.get();
			}
			else
			{
				Optional<ObjectIdentifierDefinition> field = nullablesInScope.stream().filter(o -> o.getIdentifier().equals(identifierName)).findAny();
				if (field.isPresent())
					receiver = field.get();
			}
			//make sure receiver is of type Object.
			if (receiver != null)
			{
				VariableRes vRes = new VariableRes(n, argu);
				VariableIn vIn = new VariableIn(receiver, argu);
				EqualityRelationship r = new EqualityRelationship();
				r.comment = "C9";
				r.left = vRes;
				r.right = vIn;
				constraints.add(r);
			}
		}


		super.visit(n, argu);
	}

//	@Override
//	public void visit(Expression n, Location argu)
//	{
//		EqualityRelationship r = new EqualityRelationship();
//		r.left = new VariableRes(n, argu);
//		r.right = new VariableRes(n.f0.choice, argu);
//		constraints.add(r);
//		super.visit(n, argu);
//	}
}
