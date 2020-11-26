import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class PointSET {

    private final SET<Point2D> point2DSET;

    // construct an empty set of points
    public PointSET() {
        point2DSET = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return point2DSET.isEmpty();
    }

    // number of points in the set
    public int size() {
        return point2DSET.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        point2DSET.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return point2DSET.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        point2DSET.forEach(p -> StdDraw.point(p.x(), p.y()));
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> point2DQueue = new Queue<>();
        point2DSET.forEach(point2D -> {
            if (rect.contains(point2D)) {
                point2DQueue.enqueue(point2D);
            }
        });
        return point2DQueue;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        Point2D nearest = null;

        for (Point2D point2D : point2DSET) {
            double dist = p.distanceSquaredTo(point2D);
            if (nearest == null) {
                nearest = point2D;
            }
            else {
                if (dist < p.distanceSquaredTo(nearest)) {
                    nearest = point2D;
                }
            }
        }
        return nearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

        String filename = args[0];
        In in = new In(filename);

        PointSET brute = new PointSET();

        // Test isEmpty
        StdOut.println("isEmpty " + brute.isEmpty());

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }

        // Test size
        StdOut.println("size " + brute.size());

        // Test contains
        In in1 = new In(filename);
        double x = in1.readDouble();
        double y = in1.readDouble();
        StdOut.println("Contains (" + x + ", " + y + ") " +
                               brute.contains(new Point2D(x, y)));

        double randX = StdRandom.uniform(0.0, 1.0);
        double randY = StdRandom.uniform(0.0, 1.0);
        StdOut.println("Contains (" + randX + ", " + randY + ") " +
                               brute.contains(new Point2D(randX, randY)));

        // Test draw
        StdDraw.setPenRadius(0.01);
        brute.draw();
        StdDraw.setPenRadius(0.002);
        StdDraw.setPenColor(StdDraw.MAGENTA);
        RectHV rectHV = new RectHV(0.34, 0.34, 0.63, 0.78);
        StdDraw.rectangle(0.34 + rectHV.width() / 2, 0.34 + rectHV.height() / 2, rectHV.width() / 2,
                          rectHV.height() / 2);

        // Test range
        StdDraw.setPenColor(StdDraw.BLACK);
        for (Point2D point2D : brute.range(rectHV)) {
            StdOut.println(point2D);
        }

        // Test nearest
        Point2D nearest = brute.nearest(new Point2D(randX, randY));
        double nearX = nearest.x();
        double nearY = nearest.y();
        StdOut.println("nearest (" + randX + ", " + randY + ") to (" + nearX + ", " + nearY + ")");
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.DARK_GRAY);
        StdDraw.point(randX, randY);
        StdDraw.setPenRadius(0.002);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.line(randX, randY, nearX, nearY);
    }
}
