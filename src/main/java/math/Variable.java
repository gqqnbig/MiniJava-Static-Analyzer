package math;

public interface Variable<TInput, TOutput extends Domain> extends Domain
{
	String getFunctionName();

	TInput getInput();

}
