import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FastCollinearPoints {

    private int numSegments = 0;
    private final ArrayList<LineSegment> lineSegments = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();

        int pointsLength = points.length;
        if (pointsLength == 0) return;
        Arrays.sort(points);
        for (int i = 0; i < pointsLength - 1; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
            if (points[i].compareTo(points[i + 1]) == 0) throw new IllegalArgumentException();
        }

        for (int i = 0; i < pointsLength; i++) {
            Point p = points[i];

            Point[] sortedArray = new Point[pointsLength];
            for (int j = 0; j < pointsLength; j++) sortedArray[j] = points[j];

            Arrays.sort(sortedArray, p.slopeOrder());

            System.out.println();
            System.out.println("p " + p.toString());
            printArrays(points);
            System.out.println();
            printArrays(sortedArray);
            System.out.println();
            for (int j = 0; j < pointsLength; j++) {
                if (p.compareTo(sortedArray[j]) != 0)
                    System.out.print(p.slopeTo(sortedArray[j]) + " ");
            }
            System.out.println();

            ArrayList<Point> collinearPoints = new ArrayList<>();

            for (int j = 0; j < pointsLength - 2; j++) {
                if (p.compareTo(sortedArray[j]) != 0) {
                    Point q = sortedArray[j];
                    Point r = sortedArray[j + 1];
                    Point s = sortedArray[j + 2];
                    double slopePq = p.slopeTo(q);
                    double slopePr = p.slopeTo(r);
                    double slopePs = p.slopeTo(s);
                    if (slopePq == slopePr && slopePq == slopePs) {
                        System.out.println(
                                "pq " + slopePq +
                                        " pr " + slopePr +
                                        " ps " + slopePs +
                                        " p " + p.toString() +
                                        " q " + q.toString() +
                                        " r " + r.toString() +
                                        " s " + s.toString()
                        );
                        collinearPoints.add(p);
                        collinearPoints.add(q);
                        collinearPoints.add(r);
                        collinearPoints.add(s);
                        if (pointsLength < j + 3) break;
                        for (int k = j + 3; k < pointsLength; k++) {
                            if (slopePq == p.slopeTo(sortedArray[k])) {
                                collinearPoints.add(sortedArray[k]);
                            }
                        }
                    }
                }
            }

            if (collinearPoints.size() > 3) {
                Collections.sort(collinearPoints);
                lineSegments.add(
                        new LineSegment(
                                collinearPoints.get(0),
                                collinearPoints.get(collinearPoints.size() - 1)));
                numSegments++;
            }
        }
    }

    private void printArrays(Point[] points) {
        for (int j = 0; j < points.length; j++) {
            System.out.print(points[j] + " ");
        }
    }

    public int numberOfSegments() {
        return numSegments;
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
