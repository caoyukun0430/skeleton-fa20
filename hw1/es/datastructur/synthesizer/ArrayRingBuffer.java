package es.datastructur.synthesizer;
import java.util.Iterator;

//Make sure to that this class and all of its methods are public
//Make sure to add the override tag for all overridden methods
//Make sure to make this class implement BoundedQueue<T>

public class ArrayRingBuffer<T> implements BoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;
    /* Index for the next enqueue. */
    private int last;
    /* Variable for the fillCount. */
    private int fillCount;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        rb = (T[]) new Object[capacity];
        first = 0;
        last = 0;
        fillCount = 0;
    }

    @Override
    public int capacity() {
        return rb.length;
    }

    @Override
    public int fillCount() {
        return fillCount;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow").
     */
    public void enqueue(T x) {
        if (isFull()) {
            throw new RuntimeException("Ring buffer overflow");
        }
        rb[last] = x;
        last = (last + 1) % capacity();
        fillCount += 1;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Ring Buffer underflow");
        }
        T removed = rb[first];
        first = (first + 1) % capacity();
        fillCount -= 1;
        return removed;
    }

    /**
     * Return oldest item, but don't remove it. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Ring Buffer underflow");
        }
        T removed = rb[first];
        return removed;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayRingBufferIterator();
    }

    private class ArrayRingBufferIterator implements Iterator<T> {

        // size gets fillCount of rb
        // index get the current added item index
        private int size;
        private int index;

        ArrayRingBufferIterator() {
            size = fillCount();
            index = first;
        }

        @Override
        public boolean hasNext() {
            return size > 0;
        }

        // similar to dequeue(), but non-destructive
        @Override
        public T next() {
            T item = rb[index];
            size -= 1;
            index = (index + 1) % capacity();
            return item;
        }
    }

    @Override
    public boolean equals(Object o) {
        // trivial true
        if (this == o) {
            return true;
        }
        // several trivial false cases to speed up.
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }

        ArrayRingBuffer<T> other = (ArrayRingBuffer<T>) o;
        if (fillCount() != other.fillCount()) {
            return false;
        }
        Iterator<T> otherIterator = other.iterator();
        // check each item should be identical in the same order
        for (T item: this) {
            if (otherIterator.hasNext()) {
                T next = otherIterator.next();
                if (item.equals(next)) {
                    return true;
                }
            }
        }
        return false;
    }
}

