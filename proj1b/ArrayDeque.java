
/**
 * Array-based double ended queue, which accepts generic types.
 * @Rule: All the method should follow "Deque API" described in
 *  https://fa20.datastructur.es/materials/proj/proj1a/proj1a#the-deque-api
 * @Rule: The stating size of array should be 8.
 * @Rule: The amount of memory that this program uses at any given time must be
 *  proportional to the number of items.
 * @Rule: For Arrays of length 16 or more, the usage factor should always be at least 25%.
 */
public class ArrayDeque<T> implements Deque<T> {

//         0 1  2 3 4 5 6 7
// items: [6 9 -1 2 0 0 0 0 ...]
// size: 5

    /* Invariants:
     addLast: The next item we want to add, will go into position size
     getLast: The item we want to return is in position size - 1
     size: The number of items in the list should be size.
    */
    private T[] items;
    private int nextFirst;
    private int nextLast;
    private int size;
    private int initCapability = 8;
    private int resizeFactor = 2;

    /** Creates an empty list. */
    public ArrayDeque() {
        items = (T[]) new Object[initCapability];
        nextLast = 0;
        nextFirst = initCapability - 1;
        size = 0;
    }

    /** Private helper to locate nextLast*/
    private int plusOne(int index) {
        return (index + 1) % items.length;
    }

    /** Private helper to locate nextFirst*/
    private int minusOne(int index) {
        return (index - 1 + items.length) % items.length;
    }

    /** Resizes the underlying array to the target capacity.
     *  Before we expand, we reorder the items from first to last,
     *  then the new nextLast will be at size + 1, and new nextFirst
     *  will be at capacity - 1*/
    private void resize(int capacity) {
        T[] newArray = (T[]) new Object[capacity];
        int tempStart = plusOne(nextFirst);
        int tempEnd = minusOne(nextLast);
//        int newIndex = 0;
//        for (int i = tempStart; i < tempStart + size ; i++) {
//            newArray[newIndex] = items[i % items.length];
//            newIndex++;
//        }
        // Only in case we use addLast only, then tempStart < tempEnd
        if (tempStart < tempEnd) {
            System.arraycopy(items, tempStart, newArray, 0, size);
        } else {
            // copy the first to end of items
            System.arraycopy(items, tempStart, newArray, 0, size - tempStart);
            // copy from 0 to tempEnd
            System.arraycopy(items, 0, newArray, size - tempStart, tempStart);
        }
        items = newArray;
        nextLast = size;
        nextFirst = capacity - 1;

    }

    private void expand() {
        if (size == items.length) {
            resize(size * resizeFactor);
        }
    }

    private void compress() {
        // can't compress to length < 8, so the length before compressing >=16
        double ratio = (double) size / items.length;
//        System.out.printf("begin %f",ratio);
        if (ratio < 0.5 && items.length >= 16) {
            resize(items.length / resizeFactor);
        }
    }

//    Implemented in interface Deque.java
//    public boolean isEmpty() {
//        return size == 0;
//    }

    @Override
    public int size() {
//        System.out.println(items.length);
//        System.out.println(size);
        return size;
    }

    /** Inserts X into the back of the list. */
    @Override
    public void addLast(T x) {
        // Check size before add
        expand();

        items[nextLast] = x;
        nextLast = plusOne(nextLast);
        size += 1;
    }

    /** Inserts X into the front of the list. */
    @Override
    public void addFirst(T x) {
        // Check size before add
        expand();

        items[nextFirst] = x;
        nextFirst = minusOne(nextFirst);
        size += 1;
    }

    /** Removes X from the back of the list. */
    @Override
    public T removeLast() {
//        if (size == items.length) {
//            resize(size + 1);
//        }
        compress();
        if (size == 0) {
            return null;
        } else {
            nextLast = minusOne(nextLast);
            size -= 1;
            // check size before return
//            System.out.println(items[nextLast]);
            return items[nextLast];
        }
    }

    /** Removes X from the front of the list.
     *  corresponds with addFirst, here plusOne,
     *  so in addFirst, minusOne is used*/
    @Override
    public T removeFirst() {
//        if (size == items.length) {
//            resize(size + 1);
//        }
        compress();
        if (size == 0) {
            return null;
        } else {
            nextFirst = plusOne(nextFirst);
            size -= 1;
            // check size before return

//            System.out.println(items[nextFirst]);
            return items[nextFirst];
        }
    }

    /** Gets the ith item in the list. 0 is the front.
     * This is core idea, no matter what our where the nextFirst and
     * nextLast start. So it appears to the uses the list start from 0*/
    @Override
    public T get(int i) {
        if (size == 0 || i >= size) {
            return null;
        } else {
            int tempStart = plusOne(nextFirst);
//            System.out.println(items[(tempStart + i) % items.length]);
            return items[(tempStart + i) % items.length];
        }
    }


    /**
     * Print the non-null items from first to last
    * */
    public void printDeque() {
        int tempStart = plusOne(nextFirst);
        for (int i = tempStart; i < tempStart + size; i++) {
            System.out.print(items[i % items.length] + " ");
        }
        System.out.println();
    }


//    public static void main(String[] args) {
////        ArrayDeque<Integer> arrayTest = new ArrayDeque<>();
////        arrayTest.addLast(1);
////        arrayTest.addLast(2);
////        arrayTest.addFirst(3);
////        arrayTest.addFirst(4);
////        arrayTest.addFirst(5);
////        arrayTest.addFirst(6);
////        arrayTest.addLast(7);
////        arrayTest.addLast(8);
////        arrayTest.size();
////        arrayTest.printDeque();
////        arrayTest.removeLast();
////        arrayTest.removeFirst();
////        arrayTest.removeFirst();
////        arrayTest.removeFirst();
////        arrayTest.removeFirst();
////        arrayTest.size();
////        arrayTest.printDeque();
////
//        ArrayDeque<Integer> q = new ArrayDeque<>();
//
//        for (int i = 0; i <= 15; i++) {
//            q.addFirst(i);
//        }
//        q.printDeque();
//
//        for (int i = 15; i >= 2; i--) {
//            int removed = q.removeFirst();
//            System.out.printf("remove %d\n",removed);
//            q.printDeque();
//            q.size();
////            double ratio = (double) q.size() / q.capacity;
//            assertEquals(i, removed);
////            assertTrue(ratio >= 0.25);
//        }
//    }

}
