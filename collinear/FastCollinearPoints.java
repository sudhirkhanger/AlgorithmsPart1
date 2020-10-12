import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FastCollinearPoints {

    private final ArrayList<LineSegment> lineSegments = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();

        int pointsLength = points.length;
        if (pointsLength == 0) return;

        for (int i = 0; i < pointsLength; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
        }

        for (int i = 0; i < pointsLength; i++) {
            for (int j = i + 1; j < pointsLength; j++)
                if (points[i].compareTo(points[j]) == 0) throw new IllegalArgumentException();
        }

        for (int i = 0; i < pointsLength; i++) {
            Point p = points[i];

            Point[] sortedArray = new Point[pointsLength];
            for (int j = 0; j < pointsLength; j++) sortedArray[j] = points[j];

            Arrays.sort(sortedArray, p.slopeOrder());

            ArrayList<Point> collinearPoints = new ArrayList<>();

            for (int j = 0; j < pointsLength - 2; j++) {
                Point q = sortedArray[j];
                Point r = sortedArray[j + 1];
                Point s = sortedArray[j + 2];

                double slopePq = p.slopeTo(q);
                if (!collinearPoints.isEmpty())
                    slopePq = p.slopeTo(collinearPoints.get(1));
                double slopePr = p.slopeTo(r);
                double slopePs = p.slopeTo(s);
                if (slopePq == slopePr && slopePq == slopePs) {
                    collinearPoints.add(p);
                    collinearPoints.add(q);
                    collinearPoints.add(r);
                    collinearPoints.add(s);
                }
            }

            if (collinearPoints.size() > 3) {
                Collections.sort(collinearPoints);
                Point minPoint = collinearPoints.get(0);
                Point maxPoint = collinearPoints.get(collinearPoints.size() - 1);
                if (p.compareTo(maxPoint) == 0) {
                    lineSegments.add(new LineSegment(minPoint, maxPoint));
                }
            }
        }
    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
