class CallSiteColumnTest
{
	public static void main(String[] args)
	{
	    C c;
	    c = ((new C()).f()).f();
	}
}

class C
{
	public C f()
	{
		return this;
	}
}
