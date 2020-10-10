/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        if (compareTo(that) == 0) return Double.NEGATIVE_INFINITY;
        if (x == that.x) return Double.POSITIVE_INFINITY;
        if (y == that.y) return 0.0;
        return (double) (y - that.y) / (x - that.x);
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     * point (x0 = x1 and y0 = y1);
     * a negative integer if this point is less than the argument
     * point; and a positive integer if this point is greater than the
     * argument point
     */
    public int compareTo(Point that) {
        if (y < that.y) return -1;
        if (y > that.y) return +1;
        if (x < that.x) return -1;
        if (x > that.x) return +1;
        else return 0;
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new PointsComparator();
    }

    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        // y0 < y1
        assert testCompareTo(8, 5, 7, 6) == -1;
        // y0 > y1
        assert testCompareTo(8, 9, 7, 5) == 1;
        // x0 < x1
        assert testCompareTo(5, 7, 6, 7) == -1;
        // x0 > x1
        assert testCompareTo(8, 7, 7, 7) == 1;
        // x0y0 == x1y1
        assert testCompareTo(4, 7, 4, 7) == 0;

        // some value
        assert testSlopeTo(2, 6, 4, 8) == 1.0;
        // horizontal line
        assert testSlopeTo(3, 4, 3, 2) == Double.POSITIVE_INFINITY;
        // vertical line
        assert testSlopeTo(8, 5, 9, 5) == 0.0;
        // same points
        assert testSlopeTo(4, 7, 4, 7) == Double.NEGATIVE_INFINITY;

        Point p = new Point(10, 31);
        Point q = new Point(347, 82);
        Point r = new Point(475, 31);
        StdOut.println("pq " + p.slopeTo(q) +
                               " pr " + p.slopeTo(r) +
                               " compare " + p.slopeOrder().compare(q, r));

        Point p1 = new Point(6, 5);
        Point q1 = new Point(1, 1);
        Point r1 = new Point(5, 5);
        StdOut.println("pq " + p1.slopeTo(q1) +
                               " pr " + p1.slopeTo(r1) +
                               " compare " + p1.slopeOrder().compare(q1, r1));
    }

    private class PointsComparator implements Comparator<Point> {

        public int compare(Point point1, Point point2) {
            if (point1 == null || point2 == null) throw new NullPointerException();
            double slopePoint1 = Point.this.slopeTo(point1);
            double slopePoint2 = Point.this.slopeTo(point2);
            if (slopePoint1 > slopePoint2) return +1;
            if (slopePoint1 < slopePoint2) return -1;
            return 0;
        }
    }

    private static int testCompareTo(int x, int y, int x1, int y1) {
        Point point1 = new Point(x, y);
        Point point2 = new Point(x1, y1);
        return point1.compareTo(point2);
    }

    private static double testSlopeTo(int x, int y, int x1, int y1) {
        Point point1 = new Point(x, y);
        Point point2 = new Point(x1, y1);
        return point1.slopeTo(point2);
    }
}
