class SimpleArrayTest
{
	public static void main(String[] args)
	{
		System.out.println(new C().get());
	}
}

class C
{
	int[] nullField;

	public int get()
	{
		return nullField[1];
	}
}