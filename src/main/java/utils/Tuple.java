package utils;

import java.util.Objects;

public class Tuple<T1, T2>
{
	public T1 item1;
	public T2 item2;

	public Tuple(T1 item1, T2 item2)
	{
		this.item1 = item1;
		this.item2 = item2;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Tuple<?, ?> tuple = (Tuple<?, ?>) o;
		return Objects.equals(item1, tuple.item1) &&
				Objects.equals(item2, tuple.item2);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(item1, item2);
	}
}
