import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Random;

/** Performs some basic array list tests. */
public class ArrayDequeTest {

    /* Utility method for printing out empty checks. */
    public static boolean checkEmpty(boolean expected, boolean actual) {
        if (expected != actual) {
            System.out.println("isEmpty() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Utility method for printing out empty checks. */
    public static boolean checkSize(int expected, int actual) {
        if (expected != actual) {
            System.out.println("size() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Prints a nice message based on whether a test passed.
     * The \n means newline. */
    public static void printTestStatus(boolean passed) {
        if (passed) {
            System.out.println("Test passed!\n");
        } else {
            System.out.println("Test failed!\n");
        }
    }

    /** Basic tests before enabling resizing
     * */
    public static void noResizingTest() {
        System.out.println("Running noResizingTest test.");
        ArrayDeque<Integer> arrayTest = new ArrayDeque<>();
        boolean passed = checkEmpty(true, arrayTest.isEmpty());
        arrayTest.addLast(1);
        arrayTest.addLast(2);
        arrayTest.addFirst(3);
        arrayTest.addFirst(4);
        passed = checkSize(4, arrayTest.size()) && passed;
        arrayTest.printDeque();

        int remove = arrayTest.removeLast();
        System.out.printf("remove last %d\n", remove);
        int first = arrayTest.removeFirst();
        System.out.printf("remove first %d\n", first);


        passed = checkSize(2, arrayTest.size()) && passed;
        System.out.println("Printing out deque: ");
        arrayTest.printDeque();

        printTestStatus(passed);
    }

    @Test
    public static void testRemove() {
        ArrayDeque<Integer> q = new ArrayDeque<>();
        q.addFirst(0);
        q.addLast(1);
        assertFalse(q.isEmpty());
        assertEquals(2, q.size());

        int actualFirst = q.removeFirst();
        int actualLast = q.removeLast();
        assertEquals(0, actualFirst);
        assertEquals(1, actualLast);
        assertTrue(q.isEmpty());
        assertEquals(0, q.size());

        q.addFirst(1);
        q.addFirst(0);
        int actualLast1 = q.removeLast();
        int actualLast0 = q.removeLast();
        assertEquals(1, actualLast1);
        assertEquals(0, actualLast0);
    }


    /**
     * Tests if ArrayDeque.addFirst & ArrayDeque.addLast methods with resizing.
     * */
    @Test
    public static void testAddResize() {
        ArrayDeque<Integer> q = new ArrayDeque<>();

        for (int i = 0; i < 10; i++) {
            q.addFirst(i);
        }
        assertEquals(10, q.size());
        int actualFirst = q.get(0);
        int actualLast = q.get(9);
        assertEquals(9, actualFirst);
        assertEquals(0, actualLast);

        for (int i = 10; i < 100; i++) {
            q.addLast(i);
        }
        actualLast = q.get(99);
        assertEquals(100, q.size());
        assertEquals(99, actualLast);
    }

    /**
     * Tests if ArrayDeque.removeFirst & ArrayDeque.removeLast method with resizing.
     * @NOTE: To check memory usage, ArrayDeque.capacity should be made public and the 4 lines below
     *  should be commented in.
     * */
    @Test
    public static void testRemoveResize() {
        ArrayDeque<Integer> q = new ArrayDeque<>();

        for (int i = 0; i <= 100; i++) {
            q.addFirst(i);
        }
        q.printDeque();

        for (int i = 100; i >= 10; i--) {
            int removed = q.removeFirst();
//            double ratio = (double) q.size() / q.capacity;
            assertEquals(i, removed);
//            assertTrue(ratio >= 0.25);
        }

        for (int i = 10; i <= 1000; i++) {
            q.addLast(i);
        }
        for (int i = 1000; i >= 10; i--) {
            int removed = q.removeLast();
//            double ratio = (double) q.size() / q.capacity;
            assertEquals(i, removed);
//            assertTrue(ratio >= 0.25);
        }
    }

    public static void main(String[] args) {
        System.out.println("Running tests.\n");
        noResizingTest();
        testRemove();
        testAddResize();
        testRemoveResize();

    }
}
