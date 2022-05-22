package main.java.rbTree;

import main.java.RealNumber;

import static main.java.RealNumber.real;

public class Main {
    public static void main(String[] args) {
        SeqStatTree<RealNumber> tree = new SeqStatTree<>(real(41), real(47), real(30), real(28), real(38), real(35), real(39));
        tree.print(System.out);
        System.out.println("====================");
        System.out.println("max: " + tree.max());
        System.out.println("min: " + tree.min());
        System.out.println("====================\ndelete30:");
        tree.delete(real(30));
        tree.print(System.out);
        System.out.println("====================");
        System.out.println("element with rank 2: " + tree.search(2));
        System.out.println("element 35 has rank: " + tree.rank(real(35)));
        System.out.println("element with rank 5: " + tree.search(5));
        System.out.println("element 41 has rank: " + tree.rank(real(41)));
        System.out.println("element with rank 10: " + tree.search(10));
        System.out.println("element 222 has rank: " + tree.rank(real(222)));
    }
}
