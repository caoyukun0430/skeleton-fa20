package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF connectedGrid;
    private WeightedQuickUnionUF connectedGridNoBottom;
    private int[] openedGrid;
    private int N;
    private int numberOfOpenSites;

    /* Constructor
    * create N-by-N grid, with all sites initially blocked */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("Grid size N ≤ 0");
        }
        this.N = N;
        numberOfOpenSites = 0;

        // Initialize openedGrid and connectedGrid
        openedGrid = new int[N * N];
        // Add virtual top at N*N (Not 0, because it conflicts with first index)
        // and virtual bottom at N*N+1
        connectedGrid = new WeightedQuickUnionUF(N * N + 2);
        // Add virtual top at N*N, no virtual bottom to prevent backwash
        // for nodes only connected to bottom row
        connectedGridNoBottom = new WeightedQuickUnionUF(N * N + 1);
    }

    /* Returns 1D-array index corresponding to the given grid */
    private int xyTo1D(int row, int col) {
        return row * N + col;
    }

    // Private helper to update connection with neighborhood when element is opened
    private void upDateConnect(int row, int col) {
        int index1D = xyTo1D(row, col);
        // If open a node on top row, connect with virtual top
        if (row == 0 && !connectedGrid.connected(N * N, index1D)) {
            connectedGrid.union(N * N, index1D);
            connectedGridNoBottom.union(N * N, index1D);
        }

        // If open a node on bottom row, connect with virtual bottom
        if (row == N - 1 && !connectedGrid.connected(N * N + 1, index1D)) {
            connectedGrid.union(N * N + 1, index1D);
        }
        // Connect to already opened elements in the neighborhood if exists
        // neighborhood is left, right, up, bottom
        int [][] neighborArray =
                new int[][]{{row, col - 1}, {row, col + 1}, {row - 1, col}, {row + 1, col}};
        for (int[] neighbor : neighborArray) {
            // If the neighbor has valid index
            if (validateWOThrow(neighbor[0], neighbor[1])) {
                int index1DNeighbor = xyTo1D(neighbor[0], neighbor[1]);
                // if neighbor is opened and we should not check if connected here,
                // let union() do itself
                // otherwise it leads to bug in backwash
                if (isOpen(neighbor[0], neighbor[1])) {
                    connectedGrid.union(index1DNeighbor, index1D);
                    connectedGridNoBottom.union(index1DNeighbor, index1D);
                }
            }
        }
    }

    // validate that row or col is a valid index
    private void validate(int row, int col) {
        if (row < 0 || row >= N) {
            throw new IndexOutOfBoundsException("row " + row + " is not between 0 and " + (N - 1));
        } else if (col < 0 || col >= N) {
            throw new IndexOutOfBoundsException("col " + col + " is not between 0 and " + (N - 1));
        }
    }

    // Private helper to check if valid index but don't throw exception
    private boolean validateWOThrow(int row, int col) {
        return row >= 0 && row < N && col >= 0 && col < N;
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);
        int index1D = xyTo1D(row, col);
        // check if it is not open already
        // if not, set to open and update sites and connections
        if (openedGrid[index1D] == 0) {
            openedGrid[index1D] = 1;
            numberOfOpenSites += 1;
            upDateConnect(row, col);
        }
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return openedGrid[xyTo1D(row, col)] == 1;
    }

    // is the site (row, col) full?
    // check if it's connected to virtual top
    public boolean isFull(int row, int col) {
        validate(row, col);
        return connectedGridNoBottom.connected(N * N, xyTo1D(row, col));
    }

    // does the system percolate? meaning virtual top and bottom connected
    public boolean percolates() {
        return connectedGrid.connected(N * N, N * N + 1);
    }

}
