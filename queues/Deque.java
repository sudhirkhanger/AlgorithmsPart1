import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node firstItem;
    private Node lastItem;
    private int n;

    // construct an empty deque
    public Deque() {
        firstItem = null;
        lastItem = null;
        n = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return firstItem == null;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldFirst = firstItem;
        Node newFirst = new Node();
        newFirst.item = item;
        newFirst.first = null;
        newFirst.last = null;
        if (isEmpty()) {
            firstItem = newFirst;
            lastItem = firstItem;
        } else {
            firstItem = newFirst;
            oldFirst.first = firstItem;
            firstItem.last = oldFirst;
        }
        n++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldLast = lastItem;
        Node newLast = new Node();
        newLast.item = item;
        newLast.first = null;
        newLast.last = null;
        if (isEmpty()) {
            lastItem = newLast;
            firstItem = lastItem;
        } else {
            lastItem = newLast;
            oldLast.last = lastItem;
            lastItem.first = oldLast;
            lastItem.last = null;
        }
        n++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        Node removedFirst = firstItem;
        firstItem = firstItem.last;
        firstItem.first = null;
        removedFirst.last = null;
        n--;
        return removedFirst.item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        Node removedLast = lastItem;
        lastItem = lastItem.first;
        lastItem.last = null;
        removedLast.first = null;
        n--;
        return removedLast.item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        String firstItem = "first item";
        String lastItem = "last item";
        String removedFirst = "removedFirst";
        String removedLast = "removedLast";

        // test isEmpty
        if (deque.isEmpty()) {
            StdOut.println("isEmpty Success");
        } else {
            StdOut.println("isEmpty Failed");
        }

        deque.addFirst("addFirst1");
        deque.addFirst("addFirst2");
        deque.addLast("addLast1");
        deque.addFirst("addFirst3");
        deque.addFirst("addFirst4");
        deque.addLast(lastItem);
        deque.addFirst("addFirst5");
        deque.addFirst(firstItem);
        deque.addFirst(removedFirst);
        String removeFirstItem = deque.removeFirst();
        deque.addLast(removedLast);
        String removeLastItem = deque.removeLast();

        StdOut.println("last " + deque.lastItem.item);
        StdOut.println("first " + deque.firstItem.item);

        // test isEmpty
        if (deque.size() == 8) {
            StdOut.println("size Success");
        } else {
            StdOut.println("size Failed");
        }

        // test addLast
        if (deque.lastItem.item.equals(lastItem)) {
            StdOut.println("addLast Success");
        } else {
            StdOut.println("addLast Failure");
        }

        // test addFirst
        if (deque.firstItem.item.equals(firstItem)) {
            StdOut.println("addFirst Success");
        } else {
            StdOut.println("addFirst Failure");
        }

        // test removeFirst
        if (removeFirstItem.equals(removedFirst)) {
            StdOut.println("removeFirst Success");
        } else {
            StdOut.println("removeFirst Failure");
        }

        // test removeLast
        if (removeLastItem.equals(removedLast)) {
            StdOut.println("removeLast Success");
        } else {
            StdOut.println("removeLast Failure");
        }


        StdOut.println(deque.firstItem.item);
        for (String item : deque) {
            StdOut.println(item);
        }
    }

    private class Node {
        private Item item;
        private Node first;
        private Node last;
    }

    private class DequeIterator implements Iterator<Item> {

        private Node current = firstItem;

        public boolean hasNext() {
            return current.last != null;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.last;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
