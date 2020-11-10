import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
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
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int num = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (tiles[i][j] != 0 && tiles[i][j] != xyTo1D(i, j)) num++;
        return num;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != 0) {
                    Pair pair = toXy(tiles[i][j]);
                    if (!(i == pair.row && j == pair.col)) manhattan +=
                            toPositive(i - pair.row) + toPositive(j - pair.col);
                }
            }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (that.n != n) return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                if (that.tiles[i][j] != tiles[i][j]) return false;
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> q = new Queue<Board>();

        Pair pos = null;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    pos = new Pair(i, j);
                }
            }
        }

        if (pos == null) return q;

        // blank tile in corner
        // return 2 neighbours
        if (pos.row == 0) {
            if (pos.col == 0) {
                q.enqueue(exch(0, 0, 0, 1));
                q.enqueue(exch(0, 0, 1, 0));
            }
            if (pos.col == 3) {
                q.enqueue(exch(0, 3, 0, 2));
                q.enqueue(exch(0, 3, 1, 3));
            }
            return q;
        }

        if (pos.row == 3) {
            if (pos.col == 0) {
                q.enqueue(exch(3, 0, 2, 0));
                q.enqueue(exch(3, 0, 3, 1));
            }
            if (pos.col == 3) {
                q.enqueue(exch(3, 3, 2, 3));
                q.enqueue(exch(3, 3, 3, 2));
            }
            return q;
        }

        // blank tile in side
        // return 3 neighbours
        if (pos.row == 0 || pos.row == n - 1 || pos.col == 0 || pos.col == n - 1) {
            // todo
            return q;
        }

        // blank tile inside
        // return 4 neighbours

        return q;
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

        // Test toString
        StdOut.println(initial.toString());

        // Test dimensions
        StdOut.println("dimension " + initial.dimension());

        // Test hamming distance
        StdOut.println("hamming " + initial.hamming());

        // Test manhattan distance
        StdOut.println("manhattan " + initial.manhattan());

        // Test manhattan distance
        StdOut.println("isGoal " + initial.isGoal());

        // Test equals
        int testBoardSize = 4;
        int[][] testTiles = new int[testBoardSize][testBoardSize];
        for (int i = 0; i < testBoardSize; i++) {
            for (int j = 0; j < testBoardSize; j++)
                testTiles[i][j] = i * testBoardSize + j + 1;
        }
        testTiles[testBoardSize - 1][testBoardSize - 1] = 0;
        Board testBoard = new Board(testTiles);

        StdOut.println("equals " + initial.equals(testBoard));

        for (Board board : initial.neighbors()) {
            StdOut.println(board.toString());
        }
    }

    class Pair {
        private final int row;
        private final int col;

        Pair(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    private int xyTo1D(int row, int col) {
        return row * n + col + 1;
    }

    private Pair toXy(int given) {
        int a = given / n;
        int b = toPositive((given % n) - 1);
        if (given % n == 0) {
            a = a - 1;
            b = n - 1;
        }
        return new Pair(a, b);
    }

    private int toPositive(int given) {
        if (given < 0) return given * -1;
        return given;
    }

    private int[][] copyTiles() {
        int[][] t = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                t[i][j] = tiles[i][j];
            }
        return t;
    }

    private Board exch(
            int blankTileRow,
            int blankTileCol,
            int exchTileRow,
            int exchTileCol) {
        int[][] t = copyTiles();
        t[blankTileRow][blankTileCol] = t[exchTileRow][exchTileCol];
        t[exchTileRow][exchTileCol] = 0;
        return new Board(t);
    }
}
