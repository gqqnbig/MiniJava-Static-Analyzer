class SingleUseFieldTest
{
	public static void main(String[] a)
	{
		System.out.println(new A().id());
	}
}

class A
{
	int f;

	public int id()
	{
		return f - 1;
	}
}
