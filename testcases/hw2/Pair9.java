/*
    This testcases test the following items
        1. empty blocking stmt handling ( if (true) {} else {} )
        2. class variable handling after member function
        3. nested calling ( (x.m()).n() )
        4. array handling ( new int[N] )
        5. should only consider assigned variables inside one class ( call to nullf() )
        6. if all method call fills (assigns) 1 class variable, need to consider in(g, n) ( call to init() )
*/
class Test {
    public static void main(String[] __) {
        A a;
        a = new A();
	    System.out.println(a.check());
    }
}

class A {
    A clsVar;
    int[] arr;
    public A init() {
        clsVar = new A();
        arr = new int[10];
        return clsVar;
    }
    public int check() {
        A p;
        int q;
        int[] r;
        p = (this.init()).init();
        q = p.nullf();
        if (true) {} else {}
        r = new int[20];
        r[q] = arr.length;
        return (clsVar.nullf()) + q;
    }
    public int nullf() {
        return 1;
    }
}