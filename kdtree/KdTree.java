import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

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
                            x.rect.xmax(),
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
        if (rect == null) throw new IllegalArgumentException("rect is null");
        Queue<Point2D> points = new Queue<>();
        range(root, rect, points);
        return points;
    }

    private void range(Node node, RectHV rect, Queue<Point2D> points) {
        if (node == null) return;
        if (rect.contains(node.p)) points.enqueue(node.p);
        if (node.rect.intersects(rect)) {
            range(node.lb, rect, points);
            range(node.rt, rect, points);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("p is null");
        return nearest(root, p, null);
    }

    private Point2D nearest(Node x, Point2D p, Point2D nearest) {
        if (x == null) return nearest;
        if (nearest == null)
            nearest = x.p;
        else if (p.distanceSquaredTo(x.p) < p.distanceSquaredTo(nearest))
            nearest = x.p;
        if (x.isVertical) {
            if (p.x() < x.p.x()) {
                return nearest(x.lb, p, nearest);
            } else if (p.distanceSquaredTo(nearest) > p
                    .distanceSquaredTo(new Point2D(x.p.x(), p.y()))) {
                return nearest(x.rt, p, nearest);
            }
        } else {
            if (p.y() < x.p.y()) {
                return nearest(x.lb, p, nearest);
            } else if (p.distanceSquaredTo(nearest) > p
                    .distanceSquaredTo(new Point2D(p.x(), x.p.y()))) {
                return nearest(x.rt, p, nearest);
            }
        }
        return nearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree kdTree = new KdTree();

        String filename = args[0];
        In in = new In(filename);
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdTree.insert(p);
            kdTree.draw();
        }

        kdTree.draw();

        Point2D p = kdTree.nearest(new Point2D(0.81, 0.30));
        StdDraw.line(p.x(), p.y(), 0.81, 0.30);
    }

    private static class Node {
        private final Point2D p;
        private final RectHV rect;
        private Node lb;
        private Node rt;
        private int size;
        private final boolean isVertical;

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
