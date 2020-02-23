class ArrayLengthTest
{
	public static void main(String[] args)
	{
		int[] arr;
		C obj;
		obj = new C();
		System.out.println((obj.getArray(arr.length)).length);
	}
}

class C
{
	public int[] getArray(int len)
	{
		return new int[len];
	}
}