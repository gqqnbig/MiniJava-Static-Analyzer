class ThisNameTest
{
	public static void main(String[] args)
	{
		System.out.println(new A().g());
	}
}

class A
{
	public int g()
	{
		return this.f();
	}


	public int f()
	{
		return 1;
	}
}

class B
{
	public int f()
	{
		return 0 - 1;
	}
}

class C extends A {}
class D extends A {}
class E extends C {}
class F extends E {}
