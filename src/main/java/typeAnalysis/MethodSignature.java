package typeAnalysis;

import java.util.Objects;

public class MethodSignature
{
	public String name;
	public Integer parameterCount;

	public MethodSignature(String name, Integer parameterCount)
	{
		this.name = name;
		this.parameterCount = parameterCount;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MethodSignature that = (MethodSignature) o;
		return name.equals(that.name) &&
				parameterCount.equals(that.parameterCount);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(name, parameterCount);
	}
}
