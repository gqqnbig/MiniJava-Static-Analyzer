// Pair 4
/**
 * proj 3 io pair
 * cs232 winter 2020
 *
 * The idea here is that the second nested call, q(), called from p(),
 * has unified localvars. Therefore, the second call to p, even though
 * its localvars itself are different copies because it is consiered level
 * 1 of 1cfa, will return the unified localvars of q(); so the second call
 * to p will overwrite the value of the first call to p.
 *
 */
 
 
 
class MainClass {
   public static void main(String[] args) {
      System.out.println(new Test().m());
   }
}
 
 
class Test {
 
   public int m() {
      int ret1;
      int ret2;
      ret1 = this.p(15);
      System.out.println(ret1);
 
      ret2 = this.p(0);
      System.out.println(ret2);
 
      return 100;  // some number > 0
   }
 
   public int p(int localvar) {
      return this.q(localvar);
   }
 
   // When q is called from p, the effect is that its localvars
   //   become like fieldvars, because it is 1cfa.
   public int q(int fieldvar1) {
      int fieldvar2;
      fieldvar2 = fieldvar1;
      return fieldvar2;
   }
}
