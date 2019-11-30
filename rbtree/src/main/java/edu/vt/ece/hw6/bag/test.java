package edu.vt.ece.hw6.bag;

public class test {
    public static void main(String[] args){
        Integer a = new Integer(5);
        Integer b = new Integer(5);

        if(a == b){
            System.out.println("same");
        }

        if(a.hashCode() == b.hashCode()){
            System.out.println("Same as well");
        }
    }
}
