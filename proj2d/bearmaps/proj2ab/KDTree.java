package bearmaps.proj2ab;

import java.util.List;

public class KDTree implements PointSet {

    private List<Point> pointList;
    // Trick to use two booleans to decide which level we are in 2-dim kdtree
    // HORIZONTAL means compare X-values, VERTICAL compares Y-values
    public static final boolean HORIZONTAL = false;
    public static final boolean VERTICAL = true;
    private Node root;             // root of KDtree

    // Each node contains our point and orientation decide
    // which level we are, horizontal or vertical
    private class Node {
        private Point point;
        private boolean orientation;
        private Node left, right;
        Node(Point po, boolean orient) {
            this.point = po;
            this.orientation = orient;
        }
    }

    /**
     * Inserts the specified point into the 2-dim kd-tree
     *
     * @param  p the Point inserted into tree
     * @throws IllegalArgumentException if {@code Point} is {@code null}
     */
    public void put(Point p) {
        if (p == null) {
            throw new IllegalArgumentException("calls put() with a null point");
        }
        root = put(root, p, HORIZONTAL);
    }
    /* Private HELPER for put, it's very similar to put()
    *  in BSTMap lab7*/
    private Node put(Node x, Point p, boolean orientation) {
        // if root is null yet, it's the 1st element
        // Or we reached the leaf and still match nothing
        if (x == null) {
            return new Node(p, orientation);
        }
        // Avoid duplicated point added to the tree
        if (p.equals(x.point)) {
            return x;
        }
        int cmp = compare(p, x.point, orientation);
        // If p is smaller than x.point values, put to left/down node
        // else if >= 0, put to right/up node (equals also considered to right node)
        if (cmp < 0) {
            x.left = put(x.left, p, !orientation);
        } else {
            x.right = put(x.right, p, !orientation);
        }
        return x;
    }

    // Private compare() method to decide which level based on orientation
    private int compare(Point p1, Point p2, boolean orientation) {
        if (orientation == HORIZONTAL) {
            return Double.compare(p1.getX(), p2.getX());
        } else {
            return Double.compare(p1.getY(), p2.getY());
        }
    }

    // Constructor
    public KDTree(List<Point> points) {
        pointList = points;
        for (Point p : points) {
            put(p);
        }
    }

    @Override
    public Point nearest(double x, double y) {
        Point goal = new Point(x, y);
        // Best node is initialized with root
        return nearest(root, goal, root).point;
    }

    // Private Helper returns best node
    private Node nearest(Node n, Point goal, Node best) {
        // if reach the leaf, return best
        if (n == null) {
            return best;
        }
        if (Point.distance(n.point, goal) < Point.distance(best.point, goal)) {
            best = n;
        }
        // If goal is smaller than n in its level, we define and check the good side first
        Node goodSide, badSide;
        if (compare(goal, n.point, n.orientation) < 0) {
            goodSide = n.left;
            badSide = n.right;
        } else {
            goodSide = n.right;
            badSide = n.left;
        }
        best = nearest(goodSide, goal, best);
        double bestDist = Point.distance(best.point, goal);
        // Add pruning rule to speed up checking on bad side
        if (!isPruning(bestDist, goal, n)) {
            best = nearest(badSide, goal, best);
        }
        return best;
    }

    // decide if we want to prune(aka don't check) the bad side
    // we need  to check the dist between parent node, which is n AND goal
    // only if the best possible dist(n, goal) < bestDist, we need to check bad side
    private boolean isPruning(double bestDist, Point goal, Node n) {
        if (n.orientation == HORIZONTAL) {
            return bestDist < Math.pow((goal.getX() - n.point.getX()), 2);
        } else {
            return bestDist < Math.pow((goal.getY() - n.point.getY()), 2);
        }
    }

    private static void buildTreewithDuplicates() {
        Point p1 = new Point(2, 3);
        Point p2 = new Point(2, 3);
        KDTree kd = new KDTree(List.of(p1, p2));
    }

    public static void main(String[] args) {
        Point p1 = new Point(2, 3);
        Point p2 = new Point(4, 2);
        Point p3 = new Point(4, 2);
        Point p4 = new Point(4, 5);
        Point p5 = new Point(3, 3);
        Point p6 = new Point(1, 5);
        Point p7 = new Point(4, 4);

        KDTree kd = new KDTree(List.of(p1, p2, p3, p4, p5, p6, p7));
        buildTreewithDuplicates();
        Point ret = kd.nearest(0.0, 7.0); // returns p2
        System.out.println(ret.getX());
        System.out.println(ret.getY());
    }

}
