/*
This input-output pair is nifty because it uses arrays (instead of objects) and
it tests de-initialization through a function call. The analysis must detect
that the deinit method sets the f field to null in the middle of the m method,
and so the array access will be on a null pointer.
 */

class Pair8 {
    public static void main(String[] a){
        System.out.println((new A()).n());
    }
}

class A {
    int[] f;
    int[] nullField;

    public int[] getF() {
        return f;
    }

    public int deinitF() {
        f = nullField;
        return 0;
    }

    public int n() {
        int t;
        int[] ta;
        f = new int[5];
        t = this.deinitF();
        t = f[2];
        return 0;
    }
}
