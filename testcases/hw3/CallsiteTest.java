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
	int f;

	public int id()
	{
		int m;
		System.out.println(1);
		m = m + f;
		return m - 1;
	}
}
