import java.util.Iterator;
import java.util.NoSuchElementException;

public class KthIntList implements Iterator<Integer> {
    public int k;
    private IntList curList;
    private boolean hasNext;

    public KthIntList(IntList I, int k) {
        this.k = k;
        this.curList = I;
        this.hasNext = true;
    }

    /** Returns true iff there is a next Kth element. Do not modify. */
    @Override
    public boolean hasNext() {
        return this.hasNext;
    }

    @Override
    public Integer next() {
        if (!hasNext() ||  curList == null) {
            throw new NoSuchElementException();
        }
        Integer nextItem = curList.first;
        // here we cover both even and odd cases
        if (curList.rest == null || curList.rest.rest == null) {
            hasNext = false;
        } else {
            curList = curList.rest.rest;
        }
        return nextItem;
    }

    public static void main(String[] args) {
        IntList L = new IntList(1, null);
        L = new IntList(2, L);
        L = new IntList(3, L);
        L = new IntList(4, L);
        Iterator<Integer> p = new KthIntList(L, 2);
        while (p.hasNext()) {
            System.out.println(p.next());
        }
    }
}
