// This example is particularly interesting because it involves
// storing 4 variables (a,b,c,d) and tracking their values
// Each variable is modified in a different way (+,-,*)
// At the end, all four are added together and returned
 
class Main {
    public static void main(String[] a){
        System.out.println(new A().id());
        System.out.println(new A().A());
    }
}
 
class A {
    int a;
    int b;
    public int id(){
        return 1;
    }
    public int A(){
        int c;
        int d;
        int ret;
        a = 1;
        b = 1;
        c = 1;
        d = 1;
        b = b - 2;
        c = c * b;
        d = a + c;
        ret = a;
        ret = ret + b;
        ret = ret + c;
        ret = ret + d;
        // (1) + (-1) + (-1) + (0)
        return ret;
    }
}
