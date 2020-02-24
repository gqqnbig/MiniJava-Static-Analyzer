class RtaTest
{
	public static void main(String[] args)
	{
		E obj;
		obj = new E();

		System.out.println((obj.m(1, 2)).n());
	}
}

class E extends RtaTest
{
	public E m(int a, int b)
	{
		return new E();
	}

	public int n()
	{
		return 1;
	}
}

class Unknown
{
	E _null;

	public E m(int a, int b)
	{
		return _null;
	}

}
