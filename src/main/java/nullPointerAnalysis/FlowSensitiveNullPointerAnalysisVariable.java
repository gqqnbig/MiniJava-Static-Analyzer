package nullPointerAnalysis;

import utils.FlowSensitiveVariable;
import utils.Location;
import math.Variable;

import java.util.Objects;

/**
 * A variable used in flow sensitive null pointer analysis. The domain of the variable is <code>AnalysisResult</code>.
 *
 * The input can be arbitrary.
 * @param <TInput>
 */
public abstract class FlowSensitiveNullPointerAnalysisVariable<TInput> implements Variable<TInput, AnalysisResult>, FlowSensitiveVariable<TInput, AnalysisResult>, AnalysisResult
{
	protected Location statement;
	TInput input;

	public FlowSensitiveNullPointerAnalysisVariable(TInput input, Location statement)
	{
		this.input = input;
		this.statement = statement;
	}

	@Override
	public final Location getStatement()
	{
		return statement;
	}

	@Override
	public abstract String getFunctionName();

	@Override
	public final TInput getInput()
	{
		return input;
	}

	@Override
	public String toString()
	{
		return String.format("%s[%s, %s]", getFunctionName(), input, getStatement());
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FlowSensitiveNullPointerAnalysisVariable that = (FlowSensitiveNullPointerAnalysisVariable) o;
		return statement.equals(that.statement) &&
				input.equals(that.input);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(statement, input);
	}
}


