import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

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
                if (tiles[i][j] != 0 && tiles[i][j] != (i * n + j + 1)) num++;
        return num;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                int tile = tiles[i][j];
                if (tile != 0) {
                    int row = getRow(tile);
                    int col = getCol(tile);
                    if (!(i == row && j == col)) manhattan +=
                            toPositive(i - row) + toPositive(j - col);

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

        // Pair pos = null;
        int row = -1;
        int col = -1;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    row = i;
                    col = j;
                }
            }
        }

        if (neighborCorner(q, row, col)) return q; // 2 neighbours
        if (neighborSide(q, row, col)) return q; // 3 neighbors
        neighborCenter(q, row, col); // 4 neighbors
        return q;
    }

    private boolean neighborCorner(Queue<Board> q, int row, int col) {
        if (row == 0) {
            if (col == 0) {
                q.enqueue(exch(0, 0, 0, 1, BLANK_TILE));
                q.enqueue(exch(0, 0, 1, 0, BLANK_TILE));
                return true;
            }
            if (col == n - 1) {
                q.enqueue(exch(0, n - 1, 0, n - 2, BLANK_TILE));
                q.enqueue(exch(0, n - 1, 1, n - 1, BLANK_TILE));
                return true;
            }
        }

        if (row == n - 1) {
            if (col == 0) {
                q.enqueue(exch(n - 1, 0, n - 2, 0, BLANK_TILE));
                q.enqueue(exch(n - 1, 0, n - 1, 1, BLANK_TILE));
                return true;
            }
            if (col == n - 1) {
                q.enqueue(exch(n - 1, n - 1, n - 2, n - 1, BLANK_TILE));
                q.enqueue(exch(n - 1, n - 1, n - 1, n - 2, BLANK_TILE));
                return true;
            }
        }
        return false;
    }

    private boolean neighborSide(Queue<Board> q, int row, int col) {
        if (col > 0 || col < n - 1) {
            if (row == 0) {
                q.enqueue(exch(row, col, row, col - 1, BLANK_TILE));
                q.enqueue(exch(row, col, row, col + 1, BLANK_TILE));
                q.enqueue(exch(row, col, row + 1, col, BLANK_TILE));
                return true;
            }
            if (row == n - 1) {
                q.enqueue(exch(row, col, row, col - 1, BLANK_TILE));
                q.enqueue(exch(row, col, row, col + 1, BLANK_TILE));
                q.enqueue(exch(row, col, row - 1, col, BLANK_TILE));
                return true;
            }
        }
        if (row > 0 || row < n - 1) {
            if (col == 0) {
                q.enqueue(exch(row, col, row - 1, col, BLANK_TILE));
                q.enqueue(exch(row, col, row + 1, col, BLANK_TILE));
                q.enqueue(exch(row, col, row, col + 1, BLANK_TILE));
                return true;
            }
            if (col == n - 1) {
                q.enqueue(exch(row, col, row - 1, col, BLANK_TILE));
                q.enqueue(exch(row, col, row + 1, col, BLANK_TILE));
                q.enqueue(exch(row, col, row, col - 1, BLANK_TILE));
                return true;
            }
        }
        return false;
    }

    private void neighborCenter(Queue<Board> q, int row, int col) {
        q.enqueue(exch(row, col, row, col - 1, BLANK_TILE));
        q.enqueue(exch(row, col, row, col + 1, BLANK_TILE));
        q.enqueue(exch(row, col, row - 1, col, BLANK_TILE));
        q.enqueue(exch(row, col, row + 1, col, BLANK_TILE));
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int firstTileRow = 0;
        int firstTileCol = 0;
        int secondTileRow = 0;
        int secondTileCol = 1;

        if (n == 2) {
            if (tiles[firstTileRow][firstTileCol] == 0) {
                firstTileCol = 1;
                secondTileRow = secondTileRow + 1;
            }
            if (tiles[secondTileRow][secondTileCol] == 0) {
                secondTileRow = secondTileRow + 1;
                secondTileCol = secondTileCol - 1;
            }
        }
        else {
            if (tiles[firstTileRow][firstTileCol] == 0) {
                firstTileCol = firstTileCol + 1;
                secondTileCol = secondTileCol + 1;
            }
            if (tiles[secondTileRow][secondTileCol] == 0) {
                secondTileCol = secondTileCol + 1;
            }
        }

        return exch(firstTileRow, firstTileCol, secondTileRow, secondTileCol,
                    tiles[firstTileRow][firstTileCol]);
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

        StdOut.println("Twin---\n" + initial.twin().toString());

        StdOut.println("Neighbours---");

        for (Board board : initial.neighbors()) {
            StdOut.println(board.toString());
        }
    }

    private int getRow(int tile) {
        if (tile % n == 0)
            return tile / n - 1;
        else
            return tile / n;
    }

    private int getCol(int tile) {
        int mod = tile % n;
        if (mod == 0)
            return n - 1;
        else
            return toPositive(mod - 1);
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
