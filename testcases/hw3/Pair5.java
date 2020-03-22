/* 
This test case is interesting because it tests
whether the analysis is context sensitive.

A context insensitive analysis would conclude that
a negative number could be returned.
But a context sensitive one knows that the
result could only be positive.
*/

class Main {
    public static void main(String[] a){
        System.out.println(new A().foo(4,3));
        System.out.println(new A().foo(2,1));
    }
}

class A {
    public int foo(int a, int b){
    	return a - b;
    }
}
