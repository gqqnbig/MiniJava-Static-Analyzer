class Main {
    public static void main(String[] a){
        System.out.println(new A().id());
    }
}

class A {
    A f;
    public int id(){
        return (((f))).id();
    }
}
