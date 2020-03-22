// This isn't necessarily nifty but it tests basics such as:
//      1. Addition
//      2. Initialization of local variables
//      3. Handling of method calls

class Main {
    public static void main(String[] a){
        BasicTest bt;
        int a;

        bt = new BasicTest();
        System.out.println(bt.t1(a));
    }
}

class BasicTest {
    public int t1(int x) {
        x = x + 5;
        return x;
    }
}
