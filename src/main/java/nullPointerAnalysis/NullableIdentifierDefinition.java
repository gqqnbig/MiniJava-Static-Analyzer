package nullPointerAnalysis;

import syntaxtree.Identifier;

public class NullableIdentifierDefinition
{
	private final String identifier;
	public String Class;
	public String Method;
	public boolean IsParameter;

	public String getIdentifier()
	{
		return identifier;
	}

	/**
	 * Only NullableCollector should call this.
	 * @param identifier
	 * @param className
	 * @param methodName
	 * @param isParameter
	 */
	public NullableIdentifierDefinition(Identifier identifier, String className, String methodName, boolean isParameter)
	{

		this.identifier = identifier.f0.toString();
		Class = className;
		Method = methodName;
		IsParameter = isParameter;
	}

	public String toString()
	{
		if (Method == null)
			return String.format("Field %s defined in %s", identifier, Class);

		if (IsParameter)
			return String.format("Parameter %s defined in %s.%s", identifier, Class, Method);

		return String.format("Variable %s defined in %s.%s", identifier, Class, Method);

	}
}
