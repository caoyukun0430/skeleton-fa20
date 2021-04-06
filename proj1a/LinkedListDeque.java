public class LinkedListDeque<T> {
    /** Proj1a implementation of DLList with circular sentinel
     * @Rule: All the method should follow "Deque API" described in
     * https://fa20.datastructur.es/materials/proj/proj1a/proj1a
     * @Rule: The amount of memory that your program uses at any given
     * time must be proportional to the number of items.
     * */
    private class IntNode {
        T item;
        IntNode prev;
        IntNode next;

        IntNode(T i, IntNode p, IntNode n) {
            item = i;
            prev = p;
            next = n;
        }
    }

    /* The first item (if it exists) is at sentinel.next. */
    private IntNode sentinel;
    private int size;

    /** Constructor
     * Creates an empty SLList. */
    public LinkedListDeque() {
        sentinel = new IntNode(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    /**
     * A list of methods are implemented below:
     * CHECK: boolean isEmpty()
     * ADD: addFirst(T item) addLast(T item)
     * REMOVE: T removeFirst(), T removeLast()
     * GET: T get(int index), T getRecursive(int index)
     * SIZE: int size()
     * PRINT: printDeque()
    * */


    public boolean isEmpty() {
        return size == 0;
    }

    /** Adds an item of type T to the front of the deque.
     * @Rule: A single operation should be executed in constant time.
     * */
    public void addFirst(T item) {
        // when addFirst, the prev and next for newly added node is always known!
        sentinel.next = new IntNode(item, sentinel, sentinel.next);
        // update the prev for lastly updated item, i.e. 1-2-3 update 3.prev
        sentinel.next.next.prev = sentinel.next;
        size += 1;
    }

    /** Adds an item of type T to the back of the deque
     * @Rule: A single operation should be executed in constant time.
     * */
    public void addLast(T item) {
        // the beauty of symmetry
        // same as addFirst, the prev and next for newly added node is always known!
        sentinel.prev = new IntNode(item, sentinel.prev, sentinel);
        sentinel.prev.prev.next = sentinel.prev;
        size += 1;
    }

    /** Removes and returns the item at the front of the deque.
     * If no such item exists, returns null
     * @Rule: A single operation should be executed in constant time.
     */
    // Easily derived after drawing
    public T removeFirst() {
        if (size == 0) {
            return null;
        } else {
            T removedItem = sentinel.next.item;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
            size -= 1;
            return removedItem;
        }
    }

    /** Removes and returns the item at the back of the deque.
     * If no such item exists, returns null
     * @Rule: A single operation should be executed in constant time.
     */
    public T removeLast() {
        if (size == 0) {
            return null;
        } else {
            T removedItem = sentinel.prev.item;
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next = sentinel;
            size -= 1;
            return removedItem;
        }
    }

    public int size() {
        return size;
    }

    /** Gets the item at the given index, where 0 is the front, 1 is the next item,
     * and so forth. If no such items exists, returns null.
     * @Rule: not alter the deque!
     * @Rule: Must use iteration!
     */
    public T get(int index) {
        if (size == 0 || index >= size) {
            return null;
        } else {
            IntNode p = sentinel;
            while (index > 0) {
                p = p.next;
                index -= 1;
            }
            return p.next.item;
        }
    }

    /** Gets the item at the given index, where 0 is the front, 1 is the next item,
     * and so forth. If no such items exists, returns null.
     * @Rule: not alter the deque!
     * @Rule: Must use recursion!
     */
    // Refer to lecture 4 note, sizeRecursive()
    private T getRecursiveHelper(int index, IntNode p) {
        if (index == 0) {
            return p.next.item;
        }
        return getRecursiveHelper(index - 1, p.next);
    }

    public T getRecursive(int index) {
        if (size == 0 || index > size) {
            return null;
        } else {
            return getRecursiveHelper(index, sentinel);
        }
    }

    /** Prints the items in the deque from first to last, separated by a space */
    public void printDeque() {
        int tempSize = size;
        IntNode p = sentinel;
        while (tempSize > 0) {
            System.out.print(p.next.item + " ");
            p = p.next;
            tempSize -= 1;
        }
        System.out.println();
    }

    /**
     * From @aviatesk
     * @NOTE: I used this main method while developing this class, but commented out this section,
     *  in order to satisfy The Deque API.
     */
    /*public static void main(String[] args) {
        LinkedListDeque<Integer> q = new LinkedListDeque<>();
        System.out.println("isEmpty: " + q.isEmpty());
        System.out.println("   size: " + q.size());
        System.out.println("removeFirst: " + q.removeFirst());
        System.out.println("removeLast: " + q.removeLast());
        q.printDeque();
        q.addFirst(1);
        System.out.println("isEmpty: " + q.isEmpty());
        System.out.println("   size: " + q.size());
        System.out.println("get 0th: " + q.get(0));
        System.out.println("get 1st: " + q.getRecursive(1));
        q.printDeque();
        q.addLast(-1);
        System.out.println("isEmpty: " + q.isEmpty());
        System.out.println("   size: " + q.size());
        System.out.println("get 0th: " + q.get(0));
        System.out.println("get 1st: " + q.getRecursive(1));
        q.printDeque();
        int removedFirst = q.removeFirst();
        System.out.println("isEmpty: " + q.isEmpty());
        System.out.println("   size: " + q.size());
        System.out.println("removed: " + removedFirst);
        System.out.println("get 0th: " + q.get(0));
        System.out.println("get 1st: " + q.getRecursive(1));
        q.printDeque();
        int removedLast = q.removeLast();
        System.out.println("isEmpty: " + q.isEmpty());
        System.out.println("   size: " + q.size());
        System.out.println("removed: " + removedLast);
        System.out.println("get 0th: " + q.get(0));
        System.out.println("get 1st: " + q.getRecursive(1));
        q.printDeque();
    }*/

}
