package math;

import java.util.Collection;

public interface Variable<TInput, TOutput extends Domain> extends Domain
{
	String getFunctionName();

	TInput getInput();

	/**
	 * Reduce to a simpler form. If no simpler form, return null.
	 */
	<ER extends EqualityRelationship<TOutput>> Literal<TOutput> reduce(Collection<ER> constraints, EquationSolver<TOutput> solver);

}
