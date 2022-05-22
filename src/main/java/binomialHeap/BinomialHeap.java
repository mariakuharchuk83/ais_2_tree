package main.java.binomialHeap;

import main.java.RealNumber;

public class BinomialHeap {
    // Member variables of this class
    private BinomialHeapNode Nodes;
    private int size;

    // Constructor of this class
    public BinomialHeap() {
        Nodes = null;
        size = 0;
    }

    // Checking if heap is empty
    public boolean isEmpty() {
        return Nodes == null;
    }

    // Method 1
    // To get the size
    public int getSize() {
        return size;
    }

    // Method 2
    // Clear heap
    public void makeEmpty() {
        Nodes = null;
        size = 0;
    }

    // Method 3
    // To insert
    public void insert(RealNumber value) {

        if (value.isPositive()) {
            BinomialHeapNode temp
                    = new BinomialHeapNode(value);
            if (Nodes == null) {
                Nodes = temp;
                size = 1;
            } else {
                unionNodes(temp);
                size++;
            }
        }
    }

    // Method 4
    // To unite two binomial heaps
    private void merge(BinomialHeapNode binHeap) {
        BinomialHeapNode temp1 = Nodes, temp2 = binHeap;

        while ((temp1 != null) && (temp2 != null)) {

            if (temp1.degree == temp2.degree) {

                BinomialHeapNode tmp = temp2;
                temp2 = temp2.sibling;
                tmp.sibling = temp1.sibling;
                temp1.sibling = tmp;
                temp1 = tmp.sibling;
            } else {

                if (temp1.degree < temp2.degree) {

                    if ((temp1.sibling == null)
                            || (temp1.sibling.degree
                            > temp2.degree)) {
                        BinomialHeapNode tmp = temp2;
                        temp2 = temp2.sibling;
                        tmp.sibling = temp1.sibling;
                        temp1.sibling = tmp;
                        temp1 = tmp.sibling;
                    } else {
                        temp1 = temp1.sibling;
                    }
                } else {
                    BinomialHeapNode tmp = temp1;
                    temp1 = temp2;
                    temp2 = temp2.sibling;
                    temp1.sibling = tmp;

                    if (tmp == Nodes) {
                        Nodes = temp1;
                    } else {
                    }
                }
            }
        }

        if (temp1 == null) {
            temp1 = Nodes;

            while (temp1.sibling != null) {
                temp1 = temp1.sibling;
            }
            temp1.sibling = temp2;
        } else {
        }
    }

    // Method 5
    // For union of nodes
    private void unionNodes(BinomialHeapNode binHeap) {
        merge(binHeap);

        BinomialHeapNode prevTemp = null, temp = Nodes,
                nextTemp = Nodes.sibling;

        while (nextTemp != null) {

            if ((temp.degree != nextTemp.degree)
                    || ((nextTemp.sibling != null)
                    && (nextTemp.sibling.degree
                    == temp.degree))) {
                prevTemp = temp;
                temp = nextTemp;
            } else {

                if (temp.key.compareTo(nextTemp.key) <= 0) {
                    temp.sibling = nextTemp.sibling;
                    nextTemp.parent = temp;
                    nextTemp.sibling = temp.child;
                    temp.child = nextTemp;
                    temp.degree++;
                } else {

                    if (prevTemp == null) {
                        Nodes = nextTemp;
                    } else {
                        prevTemp.sibling = nextTemp;
                    }

                    temp.parent = nextTemp;
                    temp.sibling = nextTemp.child;
                    nextTemp.child = temp;
                    nextTemp.degree++;
                    temp = nextTemp;
                }
            }
            nextTemp = temp.sibling;
        }
    }

    // Method 6
    // To return minimum key
    public RealNumber findMinimum() {
        return Nodes.findMinNode().key;
    }

    // Method 7
    // To delete a particular element */
    public void delete(RealNumber value) {

        if ((Nodes != null)
                && (Nodes.findANodeWithKey(value) != null)) {
            decreaseKeyValue(value, findMinimum());//TODO
            extractMin();
        }
    }

    // Method 8
    // To decrease key with a given value */
    public void decreaseKeyValue(RealNumber old_value,
                                 RealNumber new_value) {
        BinomialHeapNode temp
                = Nodes.findANodeWithKey(old_value);
        if (temp == null)
            return;
        temp.key = new_value;
        BinomialHeapNode tempParent = temp.parent;

        while ((tempParent != null)
                && (temp.key.compareTo(tempParent.key) < 0)) {
            RealNumber z = temp.key;
            temp.key = tempParent.key;
            tempParent.key = z;

            temp = tempParent;
            tempParent = tempParent.parent;
        }
    }

    // Method 9
    // To extract the node with the minimum key
    public RealNumber extractMin() {
        if (Nodes == null)
            return null;

        BinomialHeapNode temp = Nodes, prevTemp = null;
        BinomialHeapNode minNode = Nodes.findMinNode();

        while (!temp.key.equals(minNode.key)) {
            prevTemp = temp;
            temp = temp.sibling;
        }

        if (prevTemp == null) {
            Nodes = temp.sibling;
        } else {
            prevTemp.sibling = temp.sibling;
        }

        temp = temp.child;
        BinomialHeapNode fakeNode = temp;

        while (temp != null) {
            temp.parent = null;
            temp = temp.sibling;
        }

        if ((Nodes == null) && (fakeNode == null)) {
            size = 0;
        } else {
            if ((Nodes == null) && (fakeNode != null)) {
                Nodes = fakeNode.reverse(null);
                size = Nodes.getSize();
            } else {
                if ((Nodes != null) && (fakeNode == null)) {
                    size = Nodes.getSize();
                } else {
                    unionNodes(fakeNode.reverse(null));
                    size = Nodes.getSize();
                }
            }
        }

        return minNode.key;
    }

    // Method 10
    // To display heap
    public void displayHeap() {
        System.out.print("\nHeap : ");
        displayHeap(Nodes);
        System.out.println("\n");
    }

    private void displayHeap(BinomialHeapNode r) {
        if (r != null) {
            displayHeap(r.child);
            System.out.print(r.key + " ");
            displayHeap(r.sibling);
        }
    }

    private static class BinomialHeapNode {
        RealNumber key;
        int degree;
        BinomialHeapNode parent;
        BinomialHeapNode sibling;
        BinomialHeapNode child;

        // Constructor of this class
        public BinomialHeapNode(RealNumber k) {

            key = k;
            degree = 0;
            parent = null;
            sibling = null;
            child = null;
        }

        // Method 1
        // To reverse
        public BinomialHeapNode reverse(BinomialHeapNode sibl) {
            BinomialHeapNode ret;
            if (sibling != null)
                ret = sibling.reverse(this);
            else
                ret = this;
            sibling = sibl;
            return ret;
        }

        // Method 2
        // To find minimum node
        public BinomialHeapNode findMinNode() {
            // this keyword refers to current instance itself
            BinomialHeapNode x = this, y = this;
            RealNumber min = x.key;

            while (x != null) {
                if (x.key.compareTo(min) < 0) {
                    y = x;
                    min = x.key;
                }

                x = x.sibling;
            }

            return y;
        }

        // Method 3
        // To find node with key value
        public BinomialHeapNode findANodeWithKey(RealNumber value) {

            BinomialHeapNode temp = this, node = null;

            while (temp != null) {
                if (temp.key .equals( value)) {
                    node = temp;
                    break;
                }

                if (temp.child == null)
                    temp = temp.sibling;

                else {
                    node = temp.child.findANodeWithKey(value);
                    if (node == null)
                        temp = temp.sibling;
                    else
                        break;
                }
            }

            return node;
        }

        // Method 4
        // To get the size
        public int getSize() {
            return (
                    1 + ((child == null) ? 0 : child.getSize())
                            + ((sibling == null) ? 0 : sibling.getSize()));
        }
    }
}
