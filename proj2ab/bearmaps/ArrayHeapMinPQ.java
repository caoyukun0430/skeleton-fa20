package bearmaps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {

    private ArrayList<PriorityNode> items;
    // Use HashMap to record the <item, index>, since ArrayList contains() is O(n),
    // we need O(logn), HashMap.containsKey() is 0(1).
    // Since we don't allow duplicates in items, HashMap is better choice than treeMap O(logn)
    private HashMap<T, Integer> indexMap;
    private int size;               // number of items on priority queue

    // We don't need to have a comparable PriorityNode since we
    // don't use collection.min() to get smallest
    private class PriorityNode {
        private T item;
        private double priority;

        PriorityNode(T e, double p) {
            this.item = e;
            this.priority = p;
        }

        T getItem() {
            return item;
        }

        double getPriority() {
            return priority;
        }

        void setPriority(double priority) {
            this.priority = priority;
        }
    }

    // three private helper to locate parent and child of index k
    private int parent(int k) {
        return k / 2;
    }

    private int leftChild(int k) {
        return k * 2;
    }

    private int rightChild(int k) {
        return k * 2 + 1;
    }

    // swap the contents of two node during swim
    private void swap(int k, int j) {
        PriorityNode temp = items.get(k);
        items.set(k, items.get(j));
        items.set(j, temp);
        // Do the swap also for indexMap since index changed too
        // items.get(j) is already updated til this point!
        // e.g. k=10, j=20 meaning items.get(j) now is get(10)
        // https://www.geeksforgeeks.org/hashmap-replacekey-value-method-in-java-with-examples/
        indexMap.replace(items.get(j).getItem(), j);
        indexMap.replace(items.get(k).getItem(), k);
    }

    // Move the Node to upper level until the parent is smaller than two children
    private void swim(int k) {
        // We stop swim is k is already the root
        if (parent(k) > 0) {
            if (items.get(parent(k)).getPriority() > items.get(k).getPriority()) {
                swap(k, parent(k));
                swim(parent(k));
            }
        }
    }

    // Sink the Node to lower level until the parent is smaller than two children
    private void sink(int k) {
        int left = leftChild(k);
        // if k is at the bottom (aka no child), don't need to sink
        if (k >= size() || (left > size())) {
            return;
        }
        int right = rightChild(k);
        int minIndex = k;
        // check which is the smallest, left, right or parent
        if (left <= size() && items.get(k).getPriority() > items.get(left).getPriority()) {
            minIndex = left;
        }
        // check is right child is the smallest, if so, overwrite minIndex.
        if (right <= size() && items.get(minIndex).getPriority() > items.get(right).getPriority()) {
            minIndex = right;
        }
        // Only if child is smaller and k sinked, we continue
        if (minIndex != k) {
            swap(minIndex, k);
            sink(minIndex);
        }
    }


    //Constructor
    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        indexMap = new HashMap<>();
        // we will leave one empty spot at the beginning of the array
        // to simplify computation, aka sentinel node
        items.add(new PriorityNode(null, 0.0));
        size = 0;
    }


    /**
     * Add the item to the end of the PQ and swim to parent node is smaller
     *
     * @param  item the key added to the PQ
     * @param priority the priority of the item, deciding whether swim or not
     * @throws IllegalArgumentException if {@code item} is {@code null}
     */
    @Override
    public void add(T item, double priority) {
        if (contains(item) || item == null) {
            throw new IllegalArgumentException("the item already exists or is null");
        }
        items.add(new PriorityNode(item, priority));
        size += 1;
        indexMap.put(item, size());
        swim(size());
    }

    @Override
    public boolean contains(T item) {
        if (item == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return indexMap.containsKey(item);
        // This is not OK since it has O(n)
//        return items.contains(new PriorityNode(item, 0));
    }

    @Override
    public T getSmallest() {
        if (size() == 0) {
            throw new NoSuchElementException("no items exist in the PQ.");
        }
        return items.get(1).getItem();
    }

    @Override
    public T removeSmallest() {
        if (size() == 0) {
            throw new NoSuchElementException("no items exist in the PQ.");
        }
        T removed = items.get(1).getItem();
        T last = items.get(size()).getItem();
        items.set(1, items.get(size()));
        // we remove the <root, 1> in IndexMap and add <last, 1> to root
        indexMap.remove(removed);
        indexMap.put(last, 1);
        items.remove(size());
        size -= 1;
        // sink the root node
        sink(1);
        return removed;
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Changes the priority of the given item. Throws NoSuchElementException if the item
     * doesn't exist.
     *
     * @param  item the key of item we want to change
     * @param priority the priority to be updated
     * @throws NoSuchElementException if {@code item} is not in the PQ
     */
    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("PQ does not contain " + item);
        }
        int itemIndex = indexMap.get(item);
        double itemOriginPrior = items.get(itemIndex).getPriority();
        items.get(itemIndex).setPriority(priority);
        // if the new priority is smaller, we need to swim current item up
        // else if larger, we need to sink down
        if (priority < itemOriginPrior) {
            swim(itemIndex);
        } else if (priority > itemOriginPrior) {
            sink(itemIndex);
        }
    }

    /** @source PrintHeapDemo.java
     * Prints out a vey basic drawing of the given array of Objects assuming it
     *  is a heap starting at index 1. */
    public void printSimpleHeapDrawing() {
        int depth = ((int) (Math.log(size()) / Math.log(2)));
        int level = 0;
        int itemsUntilNext = (int) Math.pow(2, level);
        for (int j = 0; j < depth; j++) {
            System.out.print(" ");
        }

        for (int i = 1; i <= size(); i++) {
            System.out.printf("%d ", items.get(i).getItem());
            if (i == itemsUntilNext) {
                System.out.println();
                level++;
                itemsUntilNext += Math.pow(2, level);
                depth--;
                for (int j = 0; j < depth; j++) {
                    System.out.print(" ");
                }
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        ArrayHeapMinPQ<Integer> heapMinPQ = new ArrayHeapMinPQ<>();
        heapMinPQ.add(1, 1);
        heapMinPQ.add(2, 2);
        heapMinPQ.add(3, 3);
        heapMinPQ.add(4, 4); // results is 1-3-2-4
        heapMinPQ.add(5, 5); // results is 1-3-2-4-5
        heapMinPQ.printSimpleHeapDrawing();
        heapMinPQ.getSmallest();
//        int re = heapMinPQ.removeSmallest();
//        System.out.println(re);
        heapMinPQ.changePriority(4, 0.5);
        heapMinPQ.printSimpleHeapDrawing();
    }
}
