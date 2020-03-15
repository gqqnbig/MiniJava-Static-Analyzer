class CallSiteTest
{
	public static void main(String[] a)
	{
		int n;
		n = new A().id();
	}
}

class A
{
	public int id()
	{
		System.out.println(1);
		return 1;
	}
}
