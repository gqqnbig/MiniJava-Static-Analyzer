package typeAnalysis;

import syntaxtree.ClassDeclaration;
import syntaxtree.ClassExtendsDeclaration;
import syntaxtree.MainClass;
import visitor.GJDepthFirst;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

// R = [ method -> {classname} ] 
// A = classname 
// 
// R: 
// At each MethodDeclaration add the current A argument to the set for that 
// method name. All other nodes should merge and return 
// 
// A: 
// At each ClassDeclaration pass in the name as A. All other node types should 
// hand down what was given 
class CHAVisitor extends GJDepthFirst<Object, String>
{
    HashMap<String, HashSet<String>> parent2child = new HashMap<String, HashSet<String>>();
    List<String> rootlist = new Vector<String>();

    /** 
     * 
     * f0 -> "class" 
     * f1 -> Identifier() 
     * f2 -> "{" 
     * f3 -> ( VarDeclaration() )* 
     * f4 -> ( MethodDeclaration() )* 
     * f5 -> "}" 
     */ 
    public Object visit(ClassDeclaration n, String argu) {
        String root = n.f1.f0.toString();
        this.rootlist.add(root);
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        return null; 
    } 

    /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "extends"
    * f3 -> Identifier()
    * f4 -> "{"
    * f5 -> ( VarDeclaration() )*
    * f6 -> ( MethodDeclaration() )*
    * f7 -> "}"
    */
    public Object visit(ClassExtendsDeclaration n, String argu) {
        String child = n.f1.f0.toString();
        String parent = n.f3.f0.toString();
        if (this.parent2child.containsKey(parent)){
            this.parent2child.get(parent).add(child);
        } else{
            HashSet<String> child_set = new HashSet<String>();
            child_set.add(child);
            this.parent2child.put(parent, child_set);
        }
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        n.f7.accept(this, argu);
        return null;
    }

    /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> "public"
    * f4 -> "static"
    * f5 -> "void"
    * f6 -> "main"
    * f7 -> "("
    * f8 -> "String"
    * f9 -> "["
    * f10 -> "]"
    * f11 -> Identifier()
    * f12 -> ")"
    * f13 -> "{"
    * f14 -> ( VarDeclaration() )*
    * f15 -> ( Statement() )*
    * f16 -> "}"
    * f17 -> "}"
    */
    public Object visit(MainClass n, String argu) {
        String root = n.f1.f0.toString();
        this.rootlist.add(root);
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        n.f7.accept(this, argu);
        n.f8.accept(this, argu);
        n.f9.accept(this, argu);
        n.f10.accept(this, argu);
        n.f11.accept(this, argu);
        n.f12.accept(this, argu);
        n.f13.accept(this, argu);
        n.f14.accept(this, argu);
        n.f15.accept(this, argu);
        n.f16.accept(this, argu);
        n.f17.accept(this, argu);
        return null;
    }
} 