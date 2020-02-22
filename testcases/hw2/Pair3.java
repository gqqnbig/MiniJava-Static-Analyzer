/*
	What makes this Input Output pair testcase nifty is the fact that the program will 
	run into a null pointer error since method testIO of class X calls method nullarg of 
	class Y with an uninitialized argument of type X.
*/
class Pair3 {
    public static void main(String[] a){
        System.out.println(new X().m(10));
    }
}

class X {
	X x;
    public int m(int a){
		int n;
		n = new Y().m(x, a);
        return n;
    }

    public int op(int a, int b)
    {
    	return a + b;
    }
}

class Y {
	int c;
	public int m(X x, int num){
		c = 5;
		return x.op(c, num);
	}
}

class Z {
	X x;
	public int m(int a){
		return x.op(a, a);
	}
}
