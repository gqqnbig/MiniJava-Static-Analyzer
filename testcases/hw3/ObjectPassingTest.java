class ObjectPassingTest
{
	public static void main(String[] args)
	{
		C c;
		c = new C();
		c.f = 1;
		System.out.println(c.change(c));
		System.out.println(c.f);
	}
}

class C
{
	int f;

	public int change(C c)
	{
		c.f = 2;
		return 1;
	}
}