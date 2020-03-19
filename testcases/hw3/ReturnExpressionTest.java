class ReturnExpressionTest
{
	public static void main(String[] args)
	{
		C c;
		c = new C();

		System.out.println((c.f()).g());
	}
}

class C
{
	public C f()
	{
		return new C();
	}

	public int g()
	{
		return 1;
	}
}