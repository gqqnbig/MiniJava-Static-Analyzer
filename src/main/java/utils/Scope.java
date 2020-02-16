package utils;

import syntaxtree.Identifier;

import java.util.Objects;

public class Scope
{
	public String Class;
	public String Method;

	public Scope(String $class, String method)
	{
		Class = $class;
		Method = method;
	}

	public String methodScopeToString()
	{
		assert Method.endsWith("()");
		return Class + "." + Method;
	}

	public Scope convertToMethodBodyScope()
	{
		if (Method == null)
			throw new UnsupportedOperationException("Method name is missing. Cannot convert to method body scope.");
		if (Method.endsWith("()"))
			return new Scope(Class, Method.substring(0, Method.length() - 2));
		else
			return this;
	}


	public String qualify(Identifier variableIdentifier)
	{
		if (variableIdentifier == null)
			throw new IllegalArgumentException("id cannot be null.");
		if (Method == null)
			return Class + "." + variableIdentifier.f0.toString();
		else
			return Class + "." + Method + "." + variableIdentifier.f0.toString();
	}

	/**
	 * I call this for numbered parameter.
	 *
	 * @param variableIdentifier
	 * @return
	 */
	public String qualify(String variableIdentifier)
	{
		if (variableIdentifier == null)
			throw new IllegalArgumentException("id cannot be null.");
		if (Method == null)
			return Class + "." + variableIdentifier;
		else
			return Class + "." + Method + "." + variableIdentifier;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Scope scope = (Scope) o;
		return Objects.equals(Class, scope.Class) &&
				Objects.equals(Method, scope.Method);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(Class, Method);
	}
}
