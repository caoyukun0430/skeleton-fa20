package bearmaps;

import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.introcs.StdRandom;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ArrayHeapMinPQTest {

    private static void printTimingTable(List<Integer> Ns, List<Double> times,
                                         List<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# queries", "microsec/query");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    @Test
    public void testArrayHeapMinPQDemo() {
        // array is [1, 2, 4, 5, 3] after inserting 5, 4, 3, 2, 1.
        ArrayHeapMinPQ<Integer> heapMinPQ = new ArrayHeapMinPQ<>();
        NaiveMinPQ<Integer> minPQ = new NaiveMinPQ<>();
        for (int i = 0; i < 5; i++) {
            heapMinPQ.add(5 - i, 5 - i);
            minPQ.add(5 - i, 5 - i);
        }
        heapMinPQ.printSimpleHeapDrawing();
        assertEquals(5, heapMinPQ.size());
        assertEquals(1, heapMinPQ.getSmallest(), 1e-6);
        assertEquals(minPQ.getSmallest(), heapMinPQ.getSmallest(), 1e-6);

        // now 4 should be the smallest
        heapMinPQ.changePriority(4, 0.5);
        minPQ.changePriority(4, 0.5);
        heapMinPQ.printSimpleHeapDrawing();
        assertEquals(4, heapMinPQ.getSmallest(), 1e-6);
        assertEquals(minPQ.getSmallest(), heapMinPQ.getSmallest(), 1e-6);

        heapMinPQ.removeSmallest();
        assertEquals(1, heapMinPQ.getSmallest(), 1e-6);
    }

    @Test
    public void testRandomPoints() {
        ArrayHeapMinPQ<Double> heapMinPQ = new ArrayHeapMinPQ<>();
        NaiveMinPQ<Double> naiveMinPQ = new NaiveMinPQ<>();
        for (int i = 0; i < 10000; i++) {
            double item = StdRandom.uniform(-1000.0, 1000.0);
            if (!heapMinPQ.contains(item)) {
                heapMinPQ.add(item, item);
                naiveMinPQ.add(item, item);
            }
        }

        // make 1000 queries on smallest
        for (int i = 0; i < 1000; i++) {
            assertEquals(naiveMinPQ.getSmallest(), heapMinPQ.getSmallest(), 1e-6);
            heapMinPQ.removeSmallest();
            naiveMinPQ.removeSmallest();
        }
    }

    @Test
    public void testRandomPointsChangePriority() {
        // Since we need to change priorty, we need to hit the item,
        // it's easier to hit with random int than double
        ArrayHeapMinPQ<Integer> heapMinPQ = new ArrayHeapMinPQ<>();
        NaiveMinPQ<Integer> naiveMinPQ = new NaiveMinPQ<>();
        for (int i = 0; i < 10000; i++) {
            int item = StdRandom.uniform(-1000, 1000);
            if (!heapMinPQ.contains(item)) {
                heapMinPQ.add(item, item);
                naiveMinPQ.add(item, item);
            }
        }

        // random change priority and then remove points
        for (int i = 0; i < 2000; i++) {
            int item = StdRandom.uniform(-1000, 1000);
            // make sure the prior is double and random enough to avoid same priority!!
            // Because if two has same priority, it's tricky!
            double prior = StdRandom.uniform(-1000.0, 1000.0);
            if (heapMinPQ.contains(item)) {
//                System.out.println(item);
                heapMinPQ.changePriority(item, prior);
                naiveMinPQ.changePriority(item, prior);
            }
        }

        // make 1000 queries on smallest
        for (int i = 0; i < 1000; i++) {
            assertEquals(naiveMinPQ.getSmallest(), heapMinPQ.getSmallest(), 1e-6);
            heapMinPQ.removeSmallest();
            naiveMinPQ.removeSmallest();
        }
    }

    private double[] createItemSet(int N) {
        double[] itemSet = new double[N];
        for (int i = 0; i < N; i++) {
            itemSet[i] = StdRandom.uniform(-1000.0, 1000.0);
        }
        return itemSet;
    }
    @Test
    public void testTimeNaiveMinPQAdd() {
        List<Integer> Ns = new ArrayList<>();
        List<Double> times = new ArrayList<>();
        List<Integer> opCounts = new ArrayList<>();

        NaiveMinPQ<Double> naiveMinPQ = new NaiveMinPQ<>();
        for (int i = 10000; i <= 1280000; i = i * 2) {
            double[] itemSet = createItemSet(i);
            Stopwatch sw = new Stopwatch();
            for (int j = 0; j < i; j++) {
                naiveMinPQ.add(itemSet[j], itemSet[j]);
            }
            double timeInSeconds = sw.elapsedTime();
            Ns.add(i);
            times.add(timeInSeconds);
            opCounts.add(i);
        }
        printTimingTable(Ns, times, opCounts);
    }

    @Test
    public void testTimeArrayHeapMinPQAdd() {
        List<Integer> Ns = new ArrayList<>();
        List<Double> times = new ArrayList<>();
        List<Integer> opCounts = new ArrayList<>();

        ArrayHeapMinPQ<Double> heapMinPQ = new ArrayHeapMinPQ<>();
        for (int i = 10000; i <= 1280000; i = i * 2) {
            double[] itemSet = createItemSet(i);
            Stopwatch sw = new Stopwatch();
            for (int j = 0; j < i; j++) {
                heapMinPQ.add(itemSet[j], itemSet[j]);
            }
            double timeInSeconds = sw.elapsedTime();
            Ns.add(i);
            times.add(timeInSeconds);
            opCounts.add(i);
        }
        printTimingTable(Ns, times, opCounts);
    }

    @Test
    public void testTimeNaiveMinPQSmallest() {
        List<Integer> Ns = new ArrayList<>();
        List<Double> times = new ArrayList<>();
        List<Integer> opCounts = new ArrayList<>();

        NaiveMinPQ<Double> naiveMinPQ = new NaiveMinPQ<>();
        for (int i = 10000; i <= 1280000; i = i * 2) {
            double[] itemSet = createItemSet(i);
            for (int j = 0; j < i; j++) {
                naiveMinPQ.add(itemSet[j], itemSet[j]);
            }
            Stopwatch sw = new Stopwatch();
            for (int k = 0; k < 20; k++) {
                naiveMinPQ.getSmallest();
                naiveMinPQ.removeSmallest();
            }
            double timeInSeconds = sw.elapsedTime();
            Ns.add(i);
            times.add(timeInSeconds);
            opCounts.add(10);
        }
        printTimingTable(Ns, times, opCounts);
    }

    // Compared the getSmallest() and removeSmallest() runtime
    // HeapMinPQ is at least 300x faster than NaivePQ !!!
    @Test
    public void testTimeArrayHeapMinPQSmallest() {
        List<Integer> Ns = new ArrayList<>();
        List<Double> times = new ArrayList<>();
        List<Integer> opCounts = new ArrayList<>();

        ArrayHeapMinPQ<Double> heapMinPQ = new ArrayHeapMinPQ<>();
        for (int i = 10000; i <= 1280000; i = i * 2) {
            double[] itemSet = createItemSet(i);
            for (int j = 0; j < i; j++) {
                heapMinPQ.add(itemSet[j], itemSet[j]);
            }
            Stopwatch sw = new Stopwatch();
            for (int k = 0; k < 6000; k++) {
                heapMinPQ.getSmallest();
                heapMinPQ.removeSmallest();
            }
            double timeInSeconds = sw.elapsedTime();
            Ns.add(i);
            times.add(timeInSeconds);
            opCounts.add(1000);
        }
        printTimingTable(Ns, times, opCounts);
    }

}
