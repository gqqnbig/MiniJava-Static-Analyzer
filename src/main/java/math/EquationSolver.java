package math;

import java.util.Collection;
import java.util.List;

public abstract class EquationSolver<T extends Domain>
{
	protected abstract Literal<T> union(Literal<T> a, Literal<T> b);

	public <ER extends EqualityRelationship<T>> void clearUpSingleUnion(Collection<ER> constraints)
	{
		for (ER r : constraints)
		{
			if (r.right instanceof FunctionUnion && ((FunctionUnion<T>) r.right).getInput().size() < 2)
			{
				List<T> input = ((FunctionUnion<T>) r.right).getInput();
				assert input.size() != 0;
				r.right = input.get(0);
			}
		}
	}


	/**
	 * if not found, return null.
	 *
	 * @param v
	 * @param constraints
	 * @return
	 */
	public <ER extends EqualityRelationship<T>> Literal<T> findLiteral(T v, Collection<ER> constraints)
	{
		Literal<T> literal = null;
		for (ER r : constraints)
		{
			if (r.left.equals(v) && r.right instanceof Literal<?>)
			{
				literal = union(literal, (Literal<T>) r.right);
			}
		}
		return literal;
	}
}
