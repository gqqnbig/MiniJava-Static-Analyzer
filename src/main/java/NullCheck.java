import syntaxtree.*;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;

public class NullCheck
{
	public static PrintStream debugOut;

	public static void main(String[] args) throws ParseException
	{
		//OutputStream.nullOutputStream() is available in JDK 11.
		PrintStream nullOutputStream = new PrintStream(new OutputStream()
		{
			public void write(int b) { }
		});
		if (args.length == 0 || Arrays.asList(args).contains("--debug") == false)
		{

			debugOut = nullOutputStream;
		}
		else
		{
			//debug mode
			debugOut = System.out;
			System.setOut(nullOutputStream);
		}

		//the constructor sets static fields which affects the static Goal method.
		new MiniJavaParser(System.in);
		Goal goal = MiniJavaParser.Goal();

		NullableCollector.init(goal);
		VariableCollector variableCollector = new VariableCollector();
		goal.accept(variableCollector, null);

		for (FlowSensitiveVariable variable : variableCollector.variables)
		{
			debugOut.println(variable);
		}

	}
}
