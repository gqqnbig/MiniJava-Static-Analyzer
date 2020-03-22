/*
This input-output pair is nifty because there are multiple methods that return a number and although some of them return a number that is greater than zero, one of them does not. This input/output pair tests whether the static analysis can identify that a number less than or equal to zero can be printed in the context of several identically-named methods.
 */
class IOPair {
	public static void main(String[] args) {
		A a;
		a = new D();
		System.out.println(a.returnNumber());
	}
}

class A {
	public int returnNumber() {
		int ret;
		ret = 1;
		return ret;
	}
}

class B extends A {
	public int returnNumber() {
		int ret;
		ret = 2;
		return ret;
	}
}

class C extends A {
	public int returnNumber() {
		int ret;
		ret = 3;
		return ret;
	}
}

class D extends A {
	public int returnNumber() {
		int ret;
		ret = 0 - 1;
		return ret;
	}
}
