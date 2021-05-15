package bearmaps;

import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.introcs.StdRandom;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class KDTreeTest {
    private static void printTimingTable(List<Integer> Ns, List<Double> times,
                                         List<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# queries", "microsec/query");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    private static KDTree buildTree() {
        Point p1 = new Point(2, 3);
        Point p2 = new Point(4, 2);
        Point p3 = new Point(4, 2);
        Point p4 = new Point(4, 5);
        Point p5 = new Point(3, 3);
        Point p6 = new Point(1, 5);
        Point p7 = new Point(4, 4);

        KDTree kd = new KDTree(List.of(p1, p2, p3, p4, p5, p6, p7));
        return kd;
    }

    @Test
    public void testNearestDemo() {
        KDTree kd = buildTree();
        Point actual = kd.nearest(0, 7);
        Point expect = new Point(1, 5);
        assertEquals(expect, actual); // Alternative is assertTrue(expect.equals(actual))
    }

    private List<Point> randomPoints(int N) {
        List<Point> points = new ArrayList<Point>();
        for (int i = 0; i < N; i++) {
            double x = StdRandom.uniform(-1000.0, 1000.0);
            double y = StdRandom.uniform(-1000.0, 1000.0);
            points.add(new Point(x, y));
        }
        return points;
    }
    @Test
    public void testNearestRandomPoints() {
        List<Point> points = randomPoints(10000);
        NaivePointSet nps = new NaivePointSet(points);
        KDTree kd = new KDTree(points);

        List<Point> queries = randomPoints(20000);
        for (Point goal : queries) {
            Point expect = nps.nearest(goal.getX(), goal.getY());
            Point actual = kd.nearest(goal.getX(), goal.getY());
//            assertEquals(expect, actual);
            assertEquals(Point.distance(expect, goal), Point.distance(actual, goal), 1e-6);
        }
    }

    // Testing nearest method performance in NaivePointSet
    @Test
    public void timeNearestNaivePointSet() {
        List<Integer> Ns = new ArrayList<>();
        List<Double> times = new ArrayList<>();
        List<Integer> opCounts = new ArrayList<>();
        for (int i = 1000; i <= 128000; i = i * 2) {
            List<Point> points = randomPoints(i);
            NaivePointSet nps = new NaivePointSet(points);
            List<Point> queries = randomPoints(10000);
            Stopwatch sw = new Stopwatch();
            for (Point goal : queries) {
                Point expect = nps.nearest(goal.getX(), goal.getY());
            }
            double timeInSeconds = sw.elapsedTime();
            Ns.add(i);
            times.add(timeInSeconds);
            opCounts.add(10000);
        }
        printTimingTable(Ns, times, opCounts);
    }

    // Testing nearest method performance in KD-tree
    @Test
    public void timeNearestKDTree() {
        List<Integer> Ns = new ArrayList<>();
        List<Double> times = new ArrayList<>();
        List<Integer> opCounts = new ArrayList<>();
        for (int i = 31250; i <= 2000000; i = i * 2) {
            List<Point> points = randomPoints(i);
            KDTree kd = new KDTree(points);
            List<Point> queries = randomPoints(1000000);
            Stopwatch sw = new Stopwatch();
            for (Point goal : queries) {
                Point expect = kd.nearest(goal.getX(), goal.getY());
            }
            double timeInSeconds = sw.elapsedTime();
            Ns.add(i);
            times.add(timeInSeconds);
            opCounts.add(1000000);
        }
        printTimingTable(Ns, times, opCounts);
    }

}
