/*
This input contains multiple tests together.
1. test while handling.
2. test block handling.
3. test statement n handling since multiple statements are in one line.
4. test callsite and method chains.
5. test default int value.
6. test array length.
7. test underflow.
 */


class Program
{
	public static void main(String[] args)
	{
		int n;
		int[] arr;

		{{{n = 2;while (n > -2) {System.out.println(n);n = n + 1;}}}}

		System.out.println(new ArrayTest().init().get()); //should be 1

		arr = new int[0];
		System.out.println(arr.length); //should be 1


		System.out.println(((((-2147483648)))) - 1); //should be +
	}
}

class ArrayTest
{
	int[] arr;

	public ArrayTest init()
	{
		arr = new int[2];
		arr[1] = arr[0] + 1; //arr[0] is 0.
		return this;
	}


	public int get()
	{
		return arr[1];
	}
}

