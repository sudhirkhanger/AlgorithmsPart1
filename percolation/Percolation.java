/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF weightedUF;
    private final boolean[][] openSites;
    private int counter = 0;
    private final int gridSideLength;
    private final int topVirtualSite;
    private final int bottomVirtualSite;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n is <= 0");
        gridSideLength = n;
        openSites = new boolean[gridSideLength][gridSideLength];
        int ufObjectSize = gridSideLength * gridSideLength + 2;
        weightedUF = new WeightedQuickUnionUF(ufObjectSize);
        topVirtualSite = ufObjectSize - 1;
        bottomVirtualSite = ufObjectSize - 2;
    }

    // this is a test client
    public static void main(String[] args) {
        In in = new In(args[0]);      // input file
        int n = in.readInt();         // n-by-n percolation system

        // repeatedly read in sites to open and draw resulting system
        Percolation perc = new Percolation(n);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
        }

        System.out.println("Percolates " + perc.percolates());
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isOpen(row, col)) {
            int site = xyTo1D(row, col);
            openSites[row - 1][col - 1] = true;
            counter++;
            assignVirtualSites(row, col);
            connectAdjacentSites(row, col, site);
        }
    }

    // connect with adjacent sites
    private void connectAdjacentSites(int row, int col, int site) {
        if (row != 1) {
            connectWithExisting(site, row - 1, col);
        }
        if (row != gridSideLength) {
            connectWithExisting(site, row + 1, col);
        }
        if (col != 1) {
            connectWithExisting(site, row, col - 1);
        }
        if (col != gridSideLength) {
            connectWithExisting(site, row, col + 1);
        }
    }

    // assigns bottom and top rows to virtual sites
    private void assignVirtualSites(int row, int col) {
        if (row == 1 && weightedUF.find(topVirtualSite) !=
                weightedUF.find(xyTo1D(row, col)))
            weightedUF.union(topVirtualSite, xyTo1D(row, col));
        if (row == gridSideLength && weightedUF.find(bottomVirtualSite) !=
                weightedUF.find(xyTo1D(row, col)))
            weightedUF.union(bottomVirtualSite, xyTo1D(row, col));
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (validate(row, col))
            throw new IllegalArgumentException(
                    "invalid row " + row + " or col " + col);
        else
            return openSites[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return isOpen(row, col) &&
                weightedUF.find(topVirtualSite) ==
                        weightedUF.find(xyTo1D(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return counter;
    }

    // does the system percolate?
    public boolean percolates() {
        return weightedUF.find(topVirtualSite) ==
                weightedUF.find(bottomVirtualSite);
    }

    // validate the provided values
    private boolean validate(final int row, final int col) {
        return row <= 0 || col <= 0 ||
                row > gridSideLength || col > gridSideLength;
    }

    // convert row and col to an array index
    private int xyTo1D(final int row, final int col) {
        if (validate(row, col))
            throw new IllegalArgumentException(
                    "invalid row " + row + " or col " + col);
        else
            return ((row - 1) * gridSideLength) + col - 1;
    }

    // connects with existing open sites
    private void connectWithExisting(int primarySite, int row, int col) {
        if (isOpen(row, col)) weightedUF.union(primarySite, xyTo1D(row, col));
    }
}
