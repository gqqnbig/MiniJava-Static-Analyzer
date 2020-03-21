class ObjectPassingTest
{
	public static void main(String[] args)
	{
		C c;
		int n;
		c = new C();
		n = c.setField(1);
		System.out.println(c.change(c));
		System.out.println(c.getField());
	}
}

class C
{
	int f;

	public int setField(int n)
	{
		f=n;
		return 0;
	}

	public int getField()
	{
		return f;
	}

	public int change(C c)
	{
		int n;
		n = c.setField(0-1);
		return 1;
	}
}
