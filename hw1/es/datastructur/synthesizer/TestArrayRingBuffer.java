package es.datastructur.synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void enqueueTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<Integer>(5);
        System.out.println("Testing enqueue behaviors.");
        assertEquals(5, arb.capacity());
        arb.enqueue(1);
        arb.enqueue(2);
        arb.enqueue(3);
        assertEquals(3, arb.fillCount());
        arb.enqueue(4);
        arb.enqueue(5);
        assertEquals(5, arb.fillCount());
//        arb.enqueue(6);

        System.out.println("Testing dequeue behaviors.");
        int actual =  arb.dequeue();
        assertEquals(1, actual);
        arb.enqueue(6);
        int actual2 =  arb.dequeue();
        assertEquals(2, actual2);
//        assertEquals(0, arb.fillCount());
//        arb.dequeue();
    }

    @Test
    public void dequeueTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(10);

        for (int i = 0; i < arb.capacity(); i += 1) {
            arb.enqueue(i);
        }
        Integer expected1 = 0;
        assertEquals(expected1, arb.dequeue());

        arb.enqueue(10);
        Integer expected2 = 1;
        assertEquals(expected2, arb.dequeue());
    }

    @Test
    public void peekTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(10);

        for (int i = 0; i < arb.capacity(); i += 1) {
            arb.enqueue(i);
        }
        Integer expected1 = 0;
        assertEquals(expected1, arb.peek());
        assertTrue(arb.isFull());
    }

    @Test
    public void iteratorTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<Integer>(5);

        for (int i = 0; i < arb.capacity(); i += 1) {
            arb.enqueue(i);
        }
        for (int i : arb) {
            System.out.println(i);
        }
    }

    @Test
    public void equalTest() {
        // As required, the capability can be different, only need the contents
        // ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<Integer>(3);
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<Integer>(3);
        ArrayRingBuffer<Integer> arb2 = new ArrayRingBuffer<Integer>(3);

        for (int i = 0; i < arb.capacity(); i += 1) {
            arb.enqueue(i);
            arb2.enqueue(i);
        }
        assertTrue(arb.equals(arb2));

        arb.dequeue();
        arb2.dequeue();
        assertTrue(arb.equals(arb2));

        arb.enqueue(4);
        arb2.enqueue(4);
        assertTrue(arb.equals(arb2));
        arb2.dequeue();
        arb2.enqueue(5);
        assertFalse(arb.equals(arb2));
    }
}
