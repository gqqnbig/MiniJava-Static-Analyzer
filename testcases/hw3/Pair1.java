/*
This IOPair is nifty because it requires the nemerical asserition checker to
be able to handle ranges with maximum and minimum extrema (because of the loop).
The check must also handle subtract and multiplication of these ranges.
Lastly, the checker must be at least 1-CFA so that multiple callsites
to Utility functions get unique numerical assignments.
The program ultimately only prints numbers greater than zero.
 */

class Main {
    public static void main(String[] args) {
        int a;
        int b;
        Utility u;
        u = new Utility();
        a = 1;
        b = 0 - 5;
        while (a < 100) {
            a = a + 1;
            b = b - 3;
        }
        System.out.println(u.id(a));
        System.out.println(u.sub(a, b));
        System.out.println(u.mult(0 - 1, b));
    }
}

class Utility {
    public int id(int x) {
        return x;
    }
    public int add(int a, int b) {
        return a + b;
    }
    public int sub(int a, int b) {
        return a - b;
    }
    public int mult(int a, int b) {
        return a * b;
    }
}
