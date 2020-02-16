package math;

public interface Variable<TInput, TOutput>
{
	String getFunctionName();

	TInput getInput();

}
