import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private final SearchNode last;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("board is null");

        final MinPQ<SearchNode> searchNodes = new MinPQ<>(new SearchNodeComparator());
        searchNodes.insert(new SearchNode(initial, 0, null));

        final MinPQ<SearchNode> twinSearchNodes = new MinPQ<>(new SearchNodeComparator());
        twinSearchNodes.insert(new SearchNode(initial.twin(), 0, null));

        while (true) {
            final SearchNode dequedNode = searchNodes.delMin();
            dequedNode.board.neighbors().forEach(board -> {
                if (dequedNode.previous == null) {
                    searchNodes.insert(new SearchNode(board, dequedNode.move + 1, dequedNode));
                }
                else if (!board.equals(dequedNode.previous.board)) {
                    searchNodes.insert(new SearchNode(board, dequedNode.move + 1, dequedNode));
                }
            });
            final SearchNode dequedTwin = twinSearchNodes.delMin();
            dequedTwin.board.neighbors().forEach(board -> {
                if (dequedTwin.previous == null) {
                    twinSearchNodes.insert(new SearchNode(board, dequedTwin.move + 1, dequedTwin));
                }
                else if (!board.equals(dequedTwin.previous.board)) {
                    twinSearchNodes.insert(new SearchNode(board, dequedTwin.move + 1, dequedTwin));
                }
            });
            if (dequedNode.board.isGoal()) {
                last = dequedNode;
                break;
            }
            if (dequedTwin.board.isGoal()) {
                last = null;
                break;
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return last != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable())
            return last.move;
        else
            return -1;

    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (last == null) return null;
        final Stack<Board> stack = new Stack<>();
        SearchNode game = last;
        stack.push(game.board);

        while (game.previous != null) {

            stack.push(game.previous.board);
            game = game.previous;
        }
        return stack;
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

        StdOut.println(initial.toString());

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

    private class SearchNodeComparator implements Comparator<SearchNode> {
        public int compare(SearchNode t1, SearchNode t2) {
            int priority1 = t1.manhattan + t1.move;
            int priority2 = t2.manhattan + t2.move;
            if (priority1 > priority2) return +1;
            if (priority1 < priority2) return -1;
            return 0;
        }
    }

    private class SearchNode {
        private final Board board;
        private final int move;
        private final int manhattan;
        private final SearchNode previous;

        public SearchNode(Board board, int move, SearchNode previous) {
            this.board = board;
            this.move = move;
            manhattan = board.manhattan();
            this.previous = previous;
        }
    }
}
