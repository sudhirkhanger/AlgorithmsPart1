import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

    private int numSegments = 0;
    private final ArrayList<LineSegment> lineSegments = new ArrayList<>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();

        int pointsLength = points.length;
        if (pointsLength == 0) return;
        Arrays.sort(points);
        for (int i = 0; i < pointsLength - 1; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
            if (points[i].compareTo(points[i + 1]) == 0) throw new IllegalArgumentException();
        }

        for (int i = 0; i < pointsLength; i++) {
            for (int j = i + 1; j < pointsLength; j++) {
                for (int k = j + 1; k < pointsLength; k++) {
                    for (int m = k + 1; m < pointsLength; m++) {
                        Point p = points[i];
                        Point q = points[j];
                        Point r = points[k];
                        Point s = points[m];

                        double slopePq = p.slopeTo(q);
                        double slopePr = p.slopeTo(r);
                        double slopePs = p.slopeTo(s);

                        if (slopePq != Double.NEGATIVE_INFINITY &&
                                slopePr != Double.NEGATIVE_INFINITY &&
                                slopePs != Double.NEGATIVE_INFINITY) {
                            if (slopePq == slopePr && slopePq == slopePs) {
                                numSegments++;
                                Point[] unsorted = new Point[] { p, q, r, s };
                                Arrays.sort(unsorted);
                                lineSegments.add(new LineSegment(unsorted[0], unsorted[3]));
                            }
                        }
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return numSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] lineSegmentArray = new LineSegment[lineSegments.size()];
        for (int i = 0; i < lineSegments.size(); i++)
            lineSegmentArray[i] = lineSegments.get(i);
        return lineSegmentArray;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
