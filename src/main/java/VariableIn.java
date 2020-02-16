public class VariableIn extends FlowSensitiveNullPointerAnalysisVariable
{

	public VariableIn(NullableIdentifierDefinition input, Location statement)
	{
		super(input, statement);
	}


	@Override
	public String getFunctionName()
	{
		return "in";
	}
}
