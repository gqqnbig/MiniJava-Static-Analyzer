package utils;

public interface Variable<TInput>
{
	String getFunctionName();

	TInput getInput();
}
