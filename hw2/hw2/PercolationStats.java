package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {

    private double[] fracOfOpen;
    private int numOfExper;

    /** Constructor for the simulation
     * perform T independent experiments on an N-by-N grid
     *
     * @param N Grid size
     * @param T num. of independent experiments
     * @param pf use the PercolationFactory object pf to create new Percolation objects
     */
    public PercolationStats(int N, int T, PercolationFactory pf) {
        numOfExper = T;
        fracOfOpen = new double[T];
        for (int i = 0; i < numOfExper; i++) {
            Percolation p = pf.make(N);
            // Repeat the following until the system percolates
            while (!p.percolates()) {
                int row = StdRandom.uniform(N);
                int col = StdRandom.uniform(N);
                p.open(row, col);
            }
            // The fraction of sites that are opened when the system percolates
            // provides an estimate of the percolation threshold
            fracOfOpen[i] = (double) p.numberOfOpenSites() / (N * N);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(fracOfOpen);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(fracOfOpen);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - (1.96 * stddev() / Math.sqrt(numOfExper));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + (1.96 * stddev() / Math.sqrt(numOfExper));
    }

}
