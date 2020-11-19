import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Board {

    private static final int BLANK_TILE = 0;
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
        if (neighborCorner(q, pos)) return q; // 2 neighbours
        if (neighborSide(q, pos)) return q; // 3 neighbors
        neighborCenter(q, pos); // 4 neighbors
        return q;
    }

    private boolean neighborCorner(Queue<Board> q, Pair pos) {
        if (pos.row == 0) {
            if (pos.col == 0) {
                q.enqueue(exch(0, 0, 0, 1, BLANK_TILE));
                q.enqueue(exch(0, 0, 1, 0, BLANK_TILE));
                return true;
            }
            if (pos.col == n - 1) {
                q.enqueue(exch(0, n - 1, 0, n - 2, BLANK_TILE));
                q.enqueue(exch(0, n - 1, 1, n - 1, BLANK_TILE));
                return true;
            }
        }

        if (pos.row == n - 1) {
            if (pos.col == 0) {
                q.enqueue(exch(n - 1, 0, n - 2, 0, BLANK_TILE));
                q.enqueue(exch(n - 1, 0, n - 1, 1, BLANK_TILE));
                return true;
            }
            if (pos.col == n - 1) {
                q.enqueue(exch(n - 1, n - 1, n - 2, n - 1, BLANK_TILE));
                q.enqueue(exch(n - 1, n - 1, n - 1, n - 2, BLANK_TILE));
                return true;
            }
        }
        return false;
    }

    private boolean neighborSide(Queue<Board> q, Pair pos) {
        if (pos.col == 0 || pos.col == n - 1) {
            if (pos.row == 0) {
                q.enqueue(exch(pos.row, pos.col, pos.row, pos.col - 1, BLANK_TILE));
                q.enqueue(exch(pos.row, pos.col, pos.row, pos.col + 1, BLANK_TILE));
                q.enqueue(exch(pos.row, pos.col, pos.row + 1, pos.col, BLANK_TILE));
                return true;
            }
            if (pos.row == n - 1) {
                q.enqueue(exch(pos.row, pos.col, pos.row, pos.col - 1, BLANK_TILE));
                q.enqueue(exch(pos.row, pos.col, pos.row, pos.col + 1, BLANK_TILE));
                q.enqueue(exch(pos.row, pos.col, pos.row - 1, pos.col, BLANK_TILE));
                return true;
            }
        }
        if (pos.row > 0 || pos.row < n - 1) {
            if (pos.col == 0) {
                q.enqueue(exch(pos.row, pos.col, pos.row - 1, pos.col, BLANK_TILE));
                q.enqueue(exch(pos.row, pos.col, pos.row + 1, pos.col, BLANK_TILE));
                q.enqueue(exch(pos.row, pos.col, pos.row, pos.col + 1, BLANK_TILE));
                return true;
            }
            if (pos.row == n - 1) {
                q.enqueue(exch(pos.row, pos.col, pos.row - 1, pos.col, BLANK_TILE));
                q.enqueue(exch(pos.row, pos.col, pos.row + 2, pos.col, BLANK_TILE));
                q.enqueue(exch(pos.row, pos.col, pos.row, pos.col - 1, BLANK_TILE));
                return true;
            }
        }
        return false;
    }

    private void neighborCenter(Queue<Board> q, Pair pos) {
        q.enqueue(exch(pos.row, pos.col, pos.row, pos.col - 1, BLANK_TILE));
        q.enqueue(exch(pos.row, pos.col, pos.row, pos.col + 2, BLANK_TILE));
        q.enqueue(exch(pos.row, pos.col, pos.row - 1, pos.col, BLANK_TILE));
        q.enqueue(exch(pos.row, pos.col, pos.row + 1, pos.col, BLANK_TILE));
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int tile1row = 0;
        int tile1col = 0;
        int tile2row = 0;
        int tile2col = 0;

        while (tiles[tile1row][tile1col] == tiles[tile2row][tile2col]) {
            Pair pair1 = getTile();
            Pair pair2 = getTile();
            tile1row = pair1.row;
            tile1col = pair1.col;
            tile2row = pair2.row;
            tile2col = pair2.col;
        }

        return exch(tile1row, tile1col, tile2row, tile2col, tiles[tile1row][tile1col]);
    }

    private Pair getTile() {
        int tile = 0;
        int row = -1;
        int col = -1;
        while (tile == 0) {
            row = StdRandom.uniform(n);
            col = StdRandom.uniform(n);
            tile = tiles[row][col];
        }
        return new Pair(row, col);
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

        // todo fix neighbour using puzzle3x3-05.txt
        /*for (Board board : initial.neighbors()) {
            StdOut.println(board.toString());
        }*/

        StdOut.println(initial.twin().toString());
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
            int exchTileCol,
            int exchValue) {
        int[][] t = copyTiles();
        t[blankTileRow][blankTileCol] = t[exchTileRow][exchTileCol];
        t[exchTileRow][exchTileCol] = exchValue;
        return new Board(t);
    }
}
