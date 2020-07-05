import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Adopted from ResizingArrayStack
 * https://algs4.cs.princeton.edu/13stacks/ResizingArrayStack.java.html
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] a;
    private int n;

    // construct an empty randomized queue
    public RandomizedQueue() {
        a = (Item[]) new Object[2];
        n = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("item is null");
        if (n == a.length) resize(2 * a.length);
        a[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("no items in the queue");
        int pos = StdRandom.uniform(n);
        Item item = a[pos];
        a[pos] = a[n - 1];
        a[n - 1] = null;
        n--;
        if (n > 0 && n == a.length / 4) resize(a.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("no items in the queue");
        return a[StdRandom.uniform(n)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        int n = 5;
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        queue.dequeue();
        queue.enqueue(12);
        queue.dequeue();
        StdOut.print("Adding ");
        for (int i = 0; i < n; i++) {
            StdOut.print(i + " ");
            queue.enqueue(i);
        }

        StdOut.println("isEmpty " + queue.isEmpty());
        StdOut.println("size " + queue.size());
        StdOut.println("removed " + queue.dequeue());
        StdOut.println("size " + queue.size());
        StdOut.println("sample " + queue.sample());

        for (int x : queue) {
            StdOut.print(x + " ");
        }
    }

    // an iterator, doesn't implement remove() since it's optional
    private class RandomizedQueueIterator implements Iterator<Item> {
        private int currentItem;
        private final Item[] tempArray;

        public RandomizedQueueIterator() {
            tempArray = (Item[]) new Object[n];
            for (int i = 0; i < n; i++) {
                tempArray[i] = a[i];
            }
            StdRandom.shuffle(tempArray);
            currentItem = n - 1;
        }

        public boolean hasNext() {
            return currentItem >= 0;
        }

        public void remove() {
            throw new UnsupportedOperationException("method not supported");
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("no more items to return");
            return tempArray[currentItem--];
        }
    }

    // resize the underlying array holding the elements
    private void resize(int capacity) {
        assert capacity >= n;

        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            copy[i] = a[i];
        }
        a = copy;
    }
}
