// This input first tests the correct handling of method chain in the analyzer.
// If obj.init() is called before the print statement, the program doesn't have null,
// but currently it has.
// Finally, by changing the number of calls to the chain method, getField() may return null.

class MethodChainTest
{

	public static void main(String[] args)
	{
		MyClass obj;
		int n;
		obj = new MyClass();

		System.out.println(((((obj.chain()).chain()).chain()).getField()).call());
		n = obj.init();
	}
}


class MyClass
{
	MyClass _notNull;
	MyClass _null;
	MyClass field;
	int c;

	public int init()
	{
		c = 1;
		_notNull = new MyClass();
		return 1;
	}

	public MyClass chain()
	{
		if (c < 1)
		{
			field = _notNull;
			c = 1;
		}
		else
		{
			field = _null;
			c = 0;
		}
		return this;
	}

	public MyClass getField()
	{
		return field;
	}

	public int call()
	{
		return 1;
	}

}
