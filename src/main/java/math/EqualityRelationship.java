package math;

import java.util.Objects;

public class EqualityRelationship<TDomain>
{
	public TDomain left;
	public TDomain right;

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EqualityRelationship<?> that = (EqualityRelationship<?>) o;
		return left.equals(that.left) && right.equals(that.right);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(left, right);
	}
}
