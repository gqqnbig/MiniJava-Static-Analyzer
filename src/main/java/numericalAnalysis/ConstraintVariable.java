package numericalAnalysis;

import math.EquationSolver;
import math.Literal;
import utils.ContextSensitiveVariable;
import utils.FlowSensitiveVariable;
import utils.Location;

import java.util.Collection;
import java.util.Objects;

public abstract class ConstraintVariable<TInput> implements FlowSensitiveVariable<TInput, Interval>,
		ContextSensitiveVariable<TInput, Interval>,
		Interval
{
	protected final Location statement;
	protected final Location callSite;
	private final TInput input;


	public ConstraintVariable(TInput input, Location statement, Location callSite)
	{
		assert statement != null;

		this.input = input;
		this.statement = statement;
		this.callSite = callSite;
	}

	@Override
	public final Location getStatement()
	{
		return statement;
	}

	@Override
	public Location getCallSite()
	{
		return callSite;
	}

	/**
	 * A math function is considered as a variable. This method returns the name of the math function.
	 *
	 * @return
	 */
	@Override
	public abstract String getFunctionName();

	@Override
	public final TInput getInput()
	{
		return input;
	}

	@Override
	public <ER extends math.EqualityRelationship<Interval>> Literal<Interval> reduce(Collection<ER> constraints, EquationSolver<Interval> solver)
	{
		return solver.findLiteral(this, constraints);
	}

	@Override
	public String toString()
	{
		return String.format("%s[%s, %s, %s]", getFunctionName(), input, getStatement(), getCallSite());
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ConstraintVariable that = (ConstraintVariable) o;
		return statement.equals(that.statement) &&
				input.equals(that.input) &&
				callSite.equals(that.callSite);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(statement, input);
	}
}
