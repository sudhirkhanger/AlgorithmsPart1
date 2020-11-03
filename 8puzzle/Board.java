import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Board {

    private final int[][] tiles;
    private final int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = new int[tiles.length][tiles.length];
        n = tiles.length;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return -1;
    }

    // number of tiles out of place
    public int hamming() {
        return -1;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return -1;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return false;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return null;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        return null;
    }

    // unit testing (not graded)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        StdOut.println(initial.toString());
    }
}
