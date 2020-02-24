class IndirectManipulation
{
	public static void main(String[] args)
	{
		System.out.println((new B()).m());
	}
}

class A
{
	A field;
	A _null;

	public int setField()
	{
		field = new A();
		return 1;
	}

	public int unsetField()
	{
		field = _null;
		return 1;
	}

	public int callField()
	{
		return field.get();
	}


	public int get()
	{
		return 1;
	}
}

class B
{
	public int m()
	{
		A a;
		int n;
		a = new A();
		n = a.unsetField();
		n = a.setField();
		n = a.callField();
		return 1;
	}
}