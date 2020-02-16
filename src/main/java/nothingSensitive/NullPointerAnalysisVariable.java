package nothingSensitive;

import utils.NullableIdentifierDefinition;
import utils.Variable;

public abstract class NullPointerAnalysisVariable implements Variable<NullableIdentifierDefinition>
{
	final NullableIdentifierDefinition input;

	public NullPointerAnalysisVariable(NullableIdentifierDefinition input) {this.input = input;}

	@Override
	public final NullableIdentifierDefinition getInput()
	{
		return input;
	}
}
