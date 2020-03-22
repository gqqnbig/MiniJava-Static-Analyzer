/*
This input/output pair is nifty because it tests the power of 1-CFA analysis.
Using only 0-CFA, we would incorrectly overestimate that this program may output
a number <= 0, because the output of the sub5 method is -5 from the first call.
However, because 1-CFA differentiates between call sites, we detect that the
printed results are always greater than 0.

This also tests that ranges correctly propagate through subexpressions, as both
the multiplication and the subtraction must be accounted for.
*/


class Main {
    public static void main(String[] a){
        int t;
        Subber s;
        s = new Subber();

        t = s.dubAndSub(0);
        System.out.println(s.dubAndSub(5));
        System.out.println(s.dubAndSub(3));
    }
}

class Subber {
    public int dubAndSub(int x) {
        return (x * 2) - 5;
    }
}
