import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
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
        root = insert(null, root, p, "unknown", true);
    }

    private Node insert(Node parent, Node node, Point2D p, String orientation, boolean isVertical) {
        if (node == null) {
            RectHV rectHV = rect(parent, orientation);
            return new Node(p, rectHV, 1, isVertical);
        }

        double cmp;
        if (node.isVertical) {
            cmp = p.x() - node.p.x();
            if (cmp < 0) {
                node.lb = insert(node, node.lb, p, "left", false);
            } else {
                node.rt = insert(node, node.rt, p, "right", false);
            }
        } else {
            cmp = p.y() - node.p.y();
            if (cmp < 0) {
                node.lb = insert(node, node.lb, p, "bottom", true);
            } else {
                node.rt = insert(node, node.rt, p, "top", true);
            }
        }

        node.size = 1 + size(node.lb) + size(node.rt);
        return node;
    }

    private RectHV rect(Node x, String orientation) {
        if (x != null) {
            switch (orientation) {
                case "left":
                    return new RectHV(
                            x.rect.xmin(),
                            x.rect.ymin(),
                            x.p.x(),
                            x.rect.ymax());
                case "right":
                    return new RectHV(
                            x.p.x(),
                            x.rect.ymin(),
                            x.rect.xmax(),
                            x.rect.ymax());
                case "top":
                    return new RectHV(
                            x.rect.xmin(),
                            x.p.y(),
                            x.rect.xmax(),
                            x.rect.ymax());
                case "bottom":
                    return new RectHV(
                            x.rect.xmin(),
                            x.rect.ymin(),
                            x.rect.xmin(),
                            x.p.y());
            }
        }
        return new RectHV(0, 0, 1, 1);
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
        if (node.isVertical)
            cmp = p.x() - root.p.x();
        else
            cmp = p.y() - root.p.y();
        if (cmp < 0) return get(node.lb, p);
        else if (cmp > 0) return get(node.rt, p);
        else return node.p.equals(p);
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(Node x) {
        if (x == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.02);
        StdDraw.point(x.p.x(), x.p.y());
        StdDraw.setPenRadius(0.005);
        if (x.isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }
        draw(x.lb);
        draw(x.rt);
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

        Point2D p1 = new Point2D(0.7, 0.2);
        Point2D p2 = new Point2D(0.5, 0.4);
        Point2D p3 = new Point2D(0.2, 0.3);
        Point2D p4 = new Point2D(0.4, 0.7);
        Point2D p5 = new Point2D(0.9, 0.6);

        kdTree.insert(p1);
        kdTree.insert(p2);
        kdTree.insert(p3);
        kdTree.insert(p4);
        kdTree.insert(p5);

        StdOut.println("size " + kdTree.size());
        StdOut.println("isEmpty " + kdTree.isEmpty());

        kdTree.draw();
    }

    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node lb;
        private Node rt;
        private int size;
        private boolean isVertical;

        public Node(Point2D p, RectHV rect, int size, boolean isVertical) {
            this.p = p;
            this.rect = rect;
            this.size = size;
            this.isVertical = isVertical;
        }
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return x.size;
    }
}
