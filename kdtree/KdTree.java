import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {

    private Node root;

    // construct an empty set of points
    public KdTree() {

    }

    // is the set empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // number of points in the set
    public int size() {
        return size(root);
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("p is null");
        root = insert(root, p);
    }

    private Node insert(Node node, Point2D p) {
        if (node == null) return new Node(p, null, null, 1);

        double cmp;
        if (height(root) % 2 != 0)
            cmp = p.x() - root.p.x();
        else
            cmp = p.y() - root.p.y();

        if (cmp < 0) node.lb = insert(node.lb, p);
        else if (cmp > 0) node.rt = insert(node.rt, p);
        else node.p = p;
        node.size = 1 + size(node.lb) + size(node.rt);
        return node;
    }

    private int height(Node x) {
        if (x == null) return -1;
        return 1 + Math.max(height(x.lb), height(x.rt));
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("p is null");
        return get(p);
    }

    private boolean get(Point2D p) {
        return get(root, p);
    }

    private boolean get(Node node, Point2D p) {
        if (p == null) throw new IllegalArgumentException("p is null");
        if (node == null) return false;
        double cmp;
        if (height(root) % 2 != 0)
            cmp = p.x() - root.p.x();
        else
            cmp = p.y() - root.p.y();
        if (cmp < 0) return get(node.lb, p);
        else if (cmp > 0) return get(node.rt, p);
        else return node.p.equals(p);
    }

    // draw all points to standard draw
    public void draw() {

    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        return null;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        return null;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree kdTree = new KdTree();

        StdOut.println("size " + kdTree.size());
        StdOut.println("isEmpty " + kdTree.isEmpty());

        Point2D p1 = new Point2D(0.000000, 0.500000);
        Point2D p2 = new Point2D(0.500000, 1.000000);
        Point2D p3 = new Point2D(0.500000, 0.000000);
        Point2D p4 = new Point2D(1.000000, 0.500000);

        kdTree.insert(p1);

        StdOut.println("size " + kdTree.size());
        StdOut.println("isEmpty " + kdTree.isEmpty());
    }

    private static class Node {
        private Point2D p;
        private Node lb;
        private Node rt;
        private int size;

        public Node(Point2D p, Node lb, Node rt, int size) {
            this.p = p;
            this.lb = lb;
            this.rt = rt;
            this.size = size;
        }
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return x.size;
    }
}
