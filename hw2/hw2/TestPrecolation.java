package hw2;

import edu.princeton.cs.introcs.Stopwatch;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestPrecolation {
    @Test
    public void testProco() {
        // Use example for slides
        Percolation P = new Percolation(5);
        P.open(3, 4);
        P.open(2, 4);
        P.open(2, 2);
        P.open(2, 3);
        P.open(0, 2);
        assertEquals(5, P.numberOfOpenSites());
        assertTrue(P.isFull(0, 2));
        assertFalse(P.isFull(2, 2));
        P.open(1, 2);
        assertTrue(P.isFull(2, 2));
        assertFalse(P.percolates());
        P.open(4, 4);
        assertTrue(P.percolates());
        // Test backwash
        P.open(4, 2);
        assertFalse(P.isFull(4, 2));
        P.open(4, 3);
        assertTrue(P.isFull(4, 2));
    }

    @Test
    public void testStats() {
        PercolationStats stats = new PercolationStats(20, 30, new PercolationFactory());
        double pMean = stats.mean();
        double pStd = stats.stddev();
        System.out.println("N = " + 20 + ", mean = " + pMean + ", std is " + pStd);
    }

    @Test
    public void testRuntime() {
        int[] narray = new int[]{40, 80, 160, 320, 640};
        int T = 50;
        for (int i : narray) {
            Stopwatch timer = new Stopwatch();
            PercolationStats stats = new PercolationStats(i, T, new PercolationFactory());
            double time = timer.elapsedTime();
            double pMean = stats.mean();
            double pStd = stats.stddev();
            System.out.println("N = " + i + ", mean = " + pMean + ", std is " + pStd
                    + "time is" + time);
        }

    }
}
