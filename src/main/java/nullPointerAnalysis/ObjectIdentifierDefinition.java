package nullPointerAnalysis;

import syntaxtree.Identifier;

public class ObjectIdentifierDefinition
{
	private final String identifier;
	public String Class;
	public String Method;
	public int parameterIndex;

	public String getIdentifier()
	{
		return identifier;
	}

	/**
	 * Only NullableCollector should call this.
	 *
	 * @param identifier
	 * @param className
	 * @param methodName
	 * @param parameterIndex -1 means not a parameter.
	 */
	public ObjectIdentifierDefinition(Identifier identifier, String className, String methodName, int parameterIndex)
	{

		this.identifier = identifier.f0.toString();
		Class = className;
		Method = methodName;
		this.parameterIndex = parameterIndex;
	}

	public ObjectIdentifierDefinition(Identifier mainMethodArgs, String className)
	{

		this.identifier = mainMethodArgs.f0.toString();
		Class = className;
		Method = "main";
		parameterIndex = 0;
	}

	public boolean getIsParameter()
	{
		return parameterIndex > -1;
	}

}
