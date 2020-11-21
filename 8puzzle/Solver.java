import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private final MinPQ<Board> boardMinPQ;
    private final MinPQ<Board> twinMinPQ;
    private int move = 0;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("board is null");
        boardMinPQ = new MinPQ<>(new BoardComparator());
        boardMinPQ.insert(initial);
        twinMinPQ = new MinPQ<>(new BoardComparator());
        twinMinPQ.insert(initial.twin());
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        Board dequedBoard;
        Board dequedTwin;
        while (true) {
            dequedBoard = boardMinPQ.delMin();
            Board finalDequedBoard = dequedBoard;
            dequedBoard.neighbors().forEach(board -> {
                if (!board.equals(finalDequedBoard)) {
                    boardMinPQ.insert(board);
                    move++;
                }
            });
            dequedTwin = twinMinPQ.delMin();
            Board finalDequedTwin = dequedTwin;
            dequedTwin.neighbors().forEach(board -> {
                if (!board.equals(finalDequedTwin)) twinMinPQ.insert(board);
            });
            if (dequedBoard.isGoal()) return true;
            if (dequedTwin.isGoal()) return false;
        }
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable()) {
            return move;
        }
        else {
            return -1;
        }
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSolvable()) {
            return boardMinPQ;
        }
        else {
            return null;
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    private class BoardComparator implements Comparator<Board> {

        public int compare(Board board1, Board board2) {
            if (board1 == null || board2 == null) throw new NullPointerException();
            int priority1 = board1.manhattan() + move;
            int priority2 = board2.manhattan() + move;
            if (priority1 > priority2) return +1;
            if (priority1 < priority2) return -1;
            return 0;
        }
    }
}
