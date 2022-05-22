package main.java.rbTree;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class SeqStatTree<T extends Comparable<T>> {
    private Node<T> root;

    public SeqStatTree() {

    }

    public SeqStatTree(T... initialKeys) {
        for (T key : initialKeys) {
            this.insert(key);
        }
    }

    public void insert(T key) {
        if (root == null) {
            root = new Node<>(key);
        } else {
            Node<T> current = root;
            boolean placed = false;
            Node<T> newNode = null;
            while (!placed) {
                current.setSize(current.getSize() + 1);
                if (key.compareTo(current.getKeys().get(0)) < 0) {
                    if (current.getLeft() == null) {
                        newNode = new Node<>(key, current);
                        current.setLeft(newNode);
                        placed = true;
                    } else {
                        current = current.getLeft();
                    }
                } else if (key.compareTo(current.getKeys().get(0)) > 0) {
                    if (current.getRight() == null) {
                        newNode = new Node<>(key, current);
                        current.setRight(newNode);
                        placed = true;
                    } else {
                        current = current.getRight();
                    }
                } else {
                    current.getKeys().add(key);
                    placed = true;
                }
            }
            if (newNode != null) {
                insertFix(newNode);
            }
        }
    }

    private void insertFix(Node<T> node) {
        Node<T> parent = node.parent;
        if (parent == null) {
            return;
        }

        if (!parent.red) {
            return;
        }

        Node<T> grandparent = parent.parent;

        if (grandparent == null) {
            parent.red = false;
            return;
        }

        Node<T> uncle = getUncle(parent);

        if (uncle != null && uncle.red) {
            parent.red = false;
            grandparent.red = true;
            uncle.red = false;
            insertFix(grandparent);
        } else if (parent == grandparent.left) {
            if (node == parent.right) {
                leftTurn(parent);
                parent = node;
            }
            rightTurn(grandparent);
            parent.red = false;
            grandparent.red = true;
        } else {
            if (node == parent.left) {
                rightTurn(parent);
                parent = node;
            }
            leftTurn(grandparent);
            parent.red = false;
            grandparent.red = true;
        }
    }

    private Node<T> getUncle(Node<T> parent) {
        Node<T> grandparent = parent.parent;
        if (grandparent.left == parent) {
            return grandparent.right;
        } else if (grandparent.right == parent) {
            return grandparent.left;
        } else {
            throw new IllegalStateException("Parent is not a child of its grandparent");
        }
    }

//    private void insertFix(Node<T> inserted) {
//        while (inserted.getParent().isRed()) {
//            if (inserted.getParent() == inserted.getParent().getParent().getLeft()) {
//                Node<T> right = inserted.getParent().getParent().getRight();
//                if (right.isRed()) {
//                    inserted.getParent().setRed(false);
//                    right.setRed(false);
//                    inserted.getParent().getParent().setRed(true);
//                    inserted = inserted.getParent().getParent();
//                } else {
//                    if (inserted == inserted.getParent().getRight()) {
//                        inserted = inserted.getParent();
//                        leftTurn(inserted);
//                    }
//                    inserted.getParent().setRed(false);
//                    inserted.getParent().getParent().setRed(true);
//                    rightTurn(inserted.getParent().getParent());
//                }
//            } else {
//                Node<T> left = inserted.getParent().getParent().getLeft();
//                if (left.isRed()) {
//                    inserted.getParent().setRed(false);
//                    left.setRed(false);
//                    inserted.getParent().getParent().setRed(true);
//                    inserted = inserted.getParent().getParent();
//                } else {
//                    if (inserted == inserted.getParent().getLeft()) {
//                        inserted = inserted.getParent();
//                        rightTurn(inserted);
//                    }
//                    inserted.getParent().setRed(false);
//                    inserted.getParent().getParent().setRed(true);
//                    leftTurn(inserted.getParent().getParent());
//                }
//            }
//        }
//        root.setRed(false);
//    }

    public T min() {
        return min(root).getKeys().get(0);
    }

    private Node<T> min(Node<T> node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    public T max() {
        Node<T> current = root;
        while (current.getRight() != null) {
            current = current.getRight();
        }
        return current.getKeys().get(0);
    }

    public void delete(T key) {
        delete(findNode(key));
    }

    //    //TODO size
//    private void delete(Node<T> nodeToDel) {
//        Node<T> y;
//        Node<T> x;
//        if (nodeToDel.getLeft() == null || nodeToDel.getRight() == null) {
//            y = nodeToDel;
//        } else {
//            y = successor(nodeToDel);
//        }
//        if (y.getLeft() != null) {
//            x = y.getLeft();
//        } else {
//            x = y.getRight();
//        }
//        x.setParent(y.getParent());
//        if (y.getParent() == null) {
//            root = x;
//        } else if (y == y.getParent().getLeft()) {
//            y.getParent().setLeft(x);
//        } else {
//            y.getParent().setRight(x);
//        }
//        if (y != nodeToDel) {
//            nodeToDel.setKeys(y.getKeys());
//        }
//        if (!y.isRed()) {
//            deleteFix(x);
//        }
//    }
    private void reduceWeigth(Node<T> node) {
        while (node != null) {
            node.setSize(node.getSize() - 1);
            node = node.getParent();
        }
    }

    private void delete(Node<T> node) {

        if (node == null) {
            return;
        }

        reduceWeigth(node);

        Node<T> movedUpNode;
        boolean deletedNodeColor;

        if (node.left == null || node.right == null) {
            movedUpNode = deleteNodeWithZeroOrOneChild(node);
            deletedNodeColor = node.red;
        } else {
            Node<T> inOrderSuccessor = successor(node);
            node.keys = inOrderSuccessor.keys;
            movedUpNode = deleteNodeWithZeroOrOneChild(inOrderSuccessor);
            deletedNodeColor = inOrderSuccessor.red;
        }

        if (!deletedNodeColor) {
            deleteFix(movedUpNode);

            // Remove the temporary NIL node
            if (movedUpNode.getClass() == NilNode.class) {
                replaceParentsChild(movedUpNode.parent, movedUpNode, null);
            }
        }
    }

    private Node<T> deleteNodeWithZeroOrOneChild(Node<T> node) {
        if (node.left != null) {
            replaceParentsChild(node.parent, node, node.left);
            return node.left; // moved-up node
        } else if (node.right != null) {
            replaceParentsChild(node.parent, node, node.right);
            return node.right; // moved-up node
        } else {
            Node<T> newChild = node.red ? null : new NilNode();
            replaceParentsChild(node.parent, node, newChild);
            return newChild;
        }
    }

    private void replaceParentsChild(Node<T> parent, Node<T> oldChild, Node<T> newChild) {
        if (parent == null) {
            root = newChild;
        } else if (parent.left == oldChild) {
            parent.left = newChild;
        } else if (parent.right == oldChild) {
            parent.right = newChild;
        } else {
            throw new IllegalStateException("Node is not a child of its parent");
        }

        if (newChild != null) {
            newChild.parent = parent;
        }

        if (newChild == null && oldChild != null && parent != null) {
            parent.setSize(parent.getSize() -1);
        }
    }

    private Node<T> successor(Node<T> node) {
        if (node.getRight() != null) {
            return min(node.getRight());
        }
        Node<T> parent = node.getParent();
        while (parent != null && node == parent.getRight()) {
            node = parent;
            parent = parent.getParent();
        }
        return parent;
    }

    private void deleteFix(Node<T> node) {
        // Case 1: Examined node is root, end of recursion
        if (node == root) {
            // Uncomment the following line if you want to enforce black roots (rule 2):
            // node.color = BLACK;
            return;
        }

        Node<T> sibling = getSibling(node);

        // Case 2: Red sibling
        if (sibling.red) {
            handleRedSibling(node, sibling);
            sibling = getSibling(node); // Get new sibling for fall-through to cases 3-6
        }

        // Cases 3+4: Black sibling with two black children
        if ((sibling.left == null || !sibling.left.red) && (sibling.right == null || !sibling.right.red)) {
            sibling.red = true;

            // Case 3: Black sibling with two black children + red parent
            if (node.parent.red) {
                node.parent.red = false;
            }

            // Case 4: Black sibling with two black children + black parent
            else {
                deleteFix(node.parent);
            }
        }

        // Case 5+6: Black sibling with at least one red child
        else {
            handleBlackSiblingWithAtLeastOneRedChild(node, sibling);
        }
    }

    private Node<T> getSibling(Node<T> node) {
        Node<T> parent = node.parent;
        if (node == parent.left) {
            return parent.right;
        } else if (node == parent.right) {
            return parent.left;
        } else {
            throw new IllegalStateException("Parent is not a child of its grandparent");
        }
    }

    private void handleRedSibling(Node<T> node, Node<T> sibling) {
        sibling.red = false;
        node.parent.red = true;

        if (node == node.parent.left) {
            leftTurn(node.parent);
        } else {
            rightTurn(node.parent);
        }
    }

    private void handleBlackSiblingWithAtLeastOneRedChild(Node<T> node, Node<T> sibling) {
        boolean nodeIsLeftChild = node == node.parent.left;

        // Case 5: Black sibling with at least one red child + "outer nephew" is black
        // --> Recolor sibling and its child, and rotate around sibling
        if (nodeIsLeftChild && (sibling.right == null || !sibling.right.red)) {
            sibling.left.red = false;
            sibling.red = true;
            rightTurn(sibling);
            sibling = node.parent.right;
        } else if (!nodeIsLeftChild && (sibling.left == null || !sibling.left.red)) {
            sibling.right.red = false;
            sibling.red = true;
            leftTurn(sibling);
            sibling = node.parent.left;
        }

        // Fall-through to case 6...

        // Case 6: Black sibling with at least one red child + "outer nephew" is red
        // --> Recolor sibling + parent + sibling's child, and rotate around parent
        sibling.red = node.parent.red;
        node.parent.red = false;
        if (nodeIsLeftChild) {
            sibling.right.red = false;
            leftTurn(node.parent);
        } else {
            sibling.left.red = false;
            rightTurn(node.parent);
        }
    }

//    private void deleteFix(Node<T> x) {
//        while (x != root && !x.isRed()) {
//            if (x == x.getParent().getLeft()) {
//                Node<T> w = x.getParent().getRight();
//                if (w.isRed()) {
//                    w.setRed(false);
//                    x.getParent().setRed(true);
//                    leftTurn(x.getParent());
//                    w = x.getParent().getRight();
//                }
//                if (!w.getRight().isRed() && !w.getLeft().isRed()) {
//                    w.setRed(true);
//                    x = x.getParent();
//                } else if (!w.getRight().isRed()) {
//                    w.getLeft().setRed(false);
//                    w.setRed(true);
//                    rightTurn(w);
//                    w = x.getParent().getRight();
//                }
//                w.setRed(x.getParent().isRed());
//                x.getParent().setRed(false);
//                w.getRight().setRed(false);
//                leftTurn(x.getParent());
//                x = root;
//            } else {
//                Node<T> w = x.getParent().getLeft();
//                if (w.isRed()) {
//                    w.setRed(false);
//                    x.getParent().setRed(true);
//                    rightTurn(x.getParent());
//                    w = x.getParent().getLeft();
//                }
//                if (!w.getLeft().isRed() && !w.getRight().isRed()) {
//                    w.setRed(true);
//                    x = x.getParent();
//                } else if (!w.getLeft().isRed()) {
//                    w.getRight().setRed(false);
//                    w.setRed(true);
//                    leftTurn(w);
//                    w = x.getParent().getLeft();
//                }
//                w.setRed(x.getParent().isRed());
//                x.getParent().setRed(false);
//                w.getLeft().setRed(false);
//                rightTurn(x.getParent());
//                x = root;
//            }
//        }
//        x.setRed(false);
//    }

    public T search(int rank) {
        return search(root, rank);
    }

    private T search(Node<T> current, int rank) {
        if (current == null) {
            return null;
        }
        int r = current.getLeft() == null ? 0 : current.getLeft().getSize() + 1;
        if (r == rank) {
            return current.keys.get(0);
        } else if (rank < r) {
            return search(current.getLeft(), rank);
        } else {
            return search(current.getRight(), rank - r);
        }
    }

    private Node<T> findNode(T key) {
        Node<T> current = root;
        while (current != null && !current.keys.get(0).equals(key)) {
            if (key.compareTo(current.keys.get(0)) < 0) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }
        return current;
    }

    public Integer rank(T key) {
        Node<T> current = findNode(key);
        if (current == null) {
            return null;
        }
        int rank = current.getLeft() == null ? 1 : current.getLeft().size + 1;
        while (current != root) {
            if (current == current.getParent().getRight()) {
                rank += current.getParent().getLeft().getSize() + 1;
            } else {
                current = current.getParent();
            }
        }
        return rank;
    }

    private void leftTurn(Node<T> base) {
        Node<T> right = base.getRight();
        base.setRight(right.getLeft());
        if (right.getLeft() != null) {
            right.getLeft().setParent(base);
        }
        right.setParent(base.getParent());
        if (base.getParent() == null) {
            root = right;
        } else if (base == base.getParent().getLeft()) {
            base.getParent().setLeft(right);
        } else {
            base.getParent().setRight(right);
        }
        right.setLeft(base);
        base.setParent(right);
        right.setSize(base.getSize());
        base.recalculateSize();
    }

    private void rightTurn(Node<T> base) {
        Node<T> left = base.getLeft();
        base.setRight(left.getRight());
        if (left.getRight() != null) {
            left.getRight().setParent(base);
        }
        left.setParent(base.getParent());
        if (base.getParent() == null) {
            root = left;
        } else if (base == base.getParent().getLeft()) {
            base.getParent().setLeft(left);
        } else {
            base.getParent().setRight(left);
        }
        left.setRight(base);
        base.setParent(left);
        left.setSize(base.getSize());
        base.recalculateSize();
    }

    public void print(PrintStream os) {
        os.print(traversePreOrder(root));
    }

    private String traversePreOrder(Node<T> root) {

        if (root == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(root.keys).append(root.isRed() ? ":red:size=" : ":black:size=").append(root.size);

        String pointerRight = "└──";
        String pointerLeft = (root.getRight() != null) ? "├──" : "└──";

        traverseNodes(sb, "", pointerLeft, root.getLeft(), root.getRight() != null);
        traverseNodes(sb, "", pointerRight, root.getRight(), false);

        return sb.toString();
    }

    private void traverseNodes(StringBuilder sb, String padding, String pointer, Node<T> node,
                               boolean hasRightSibling) {
        if (node != null) {
            sb.append("\n");
            sb.append(padding);
            sb.append(pointer);
            sb.append(node.keys).append(node.isRed() ? ":red:size=" : ":black:size=").append(node.size);

            StringBuilder paddingBuilder = new StringBuilder(padding);
            if (hasRightSibling) {
                paddingBuilder.append("│  ");
            } else {
                paddingBuilder.append("   ");
            }

            String paddingForBoth = paddingBuilder.toString();
            String pointerRight = "└──";
            String pointerLeft = (node.getRight() != null) ? "├──" : "└──";

            traverseNodes(sb, paddingForBoth, pointerLeft, node.getLeft(), node.getRight() != null);
            traverseNodes(sb, paddingForBoth, pointerRight, node.getRight(), false);
        }
    }

    public static class Node<T> {
        private List<T> keys;
        private Node<T> right;
        private Node<T> left;
        private Node<T> parent;
        private int size;
        private boolean red;

        //create leaf
        public Node(T key, Node<T> parent) {
            this.keys = new ArrayList<>(List.of(key));
            this.parent = parent;
            red = true;
            size = 1;
        }

        //create root
        public Node(T key) {
            this.keys = new ArrayList<>(List.of(key));
            red = false;
            size = 1;
        }

        public void recalculateSize() {
            int left = this.left == null ? 0 : this.left.size;
            int right = this.right == null ? 0 : this.right.size;
            size = left + right + 1;
        }

        public List<T> getKeys() {
            return keys;
        }

        public void setKeys(List<T> keys) {
            this.keys = keys;
        }

        public Node<T> getRight() {
            return right;
        }

        public Node<T> getLeft() {
            return left;
        }

        public Node<T> getParent() {
            return parent;
        }

        public int getSize() {
            return size;
        }

        public boolean isRed() {
            return red;
        }

        public void setRight(Node<T> right) {
            this.right = right;
        }

        public void setLeft(Node<T> left) {
            this.left = left;
        }

        public void setParent(Node<T> parent) {
            this.parent = parent;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public void setRed(boolean red) {
            this.red = red;
        }
    }

    private class NilNode extends Node<T> {
        private NilNode() {
            super(null);
        }
    }
}
