package main.java.binomialHeap;

import main.java.RealNumber;

import static main.java.RealNumber.real;

public class Main {
    public static void main(String[] args) {
        // Make object of BinomialHeap
        BinomialHeap binHeap = new BinomialHeap();

        // Inserting in the binomial heap
        // Custom input integer values
        binHeap.insert(real(12));
        binHeap.insert(real(8));
        binHeap.insert(real(5));
        binHeap.insert(real(15));
        binHeap.insert(real(7));
        binHeap.insert(real(2));
        binHeap.insert(real(9));

        // Size of binomial heap
        System.out.println("Size of the binomial heap is "
                + binHeap.getSize());

        // Displaying the binomial heap
        binHeap.displayHeap();

        // Deletion in binomial heap
        binHeap.delete(real(15));
        binHeap.delete(real(8));

        // Size of binomial heap
        System.out.println("Size of the binomial heap is "
                + binHeap.getSize());

        // Displaying the binomial heap
        binHeap.displayHeap();

        // Making the heap empty
        binHeap.makeEmpty();

        // checking if heap is empty
        System.out.println(binHeap.isEmpty());
    }
}
