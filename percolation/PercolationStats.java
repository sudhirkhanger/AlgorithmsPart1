/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;
    private final int trials;
    private final double mean;
    private final double stddev;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("n or trials <= 0");

        this.trials = trials;
        double[] numOfOpenSites = new double[trials];

        for (int t = 0; t < trials; t++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates())
                percolation.open(
                        StdRandom.uniform(1, n + 1),
                        StdRandom.uniform(1, n + 1));
            numOfOpenSites[t] = (double) percolation.numberOfOpenSites() / (n * n);
        }
        mean = StdStats.mean(numOfOpenSites);
        stddev = StdStats.stddev(numOfOpenSites);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - (CONFIDENCE_95 * stddev / Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + (CONFIDENCE_95 * stddev / Math.sqrt(trials));
    }

    // prints results
    private void results() {
        System.out.print(
                "mean                 = " + mean() +
                        "\nstddev               = " + stddev() +
                        "\n95% confidence level = [" + confidenceLo() + ", " + confidenceHi()
                        + "]");
    }

    // test client (see below)
    public static void main(String[] args) {
        String n = args[0];
        String trials = args[1];
        PercolationStats percolationStats = new PercolationStats(
                Integer.parseInt(n), Integer.parseInt(trials));
        percolationStats.results();
    }
}
