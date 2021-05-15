package bearmaps;

import java.util.List;

public class NaivePointSet implements PointSet {

    private List<Point> pointList;
    private int size;

    // Constructor
    public NaivePointSet(List<Point> points) {
        pointList = points;
        size = points.size();
    }

    @Override
    public Point nearest(double x, double y) {
        Point input = new Point(x, y);
        Point nearest = null;
        double dist = Double.POSITIVE_INFINITY;
        for (Point p : pointList) {
            double temp = Point.distance(p, input);
            if (temp < dist) {
                dist = temp;
                nearest = p;
            }
        }
        return nearest;
    }

    public static void main(String[] args) {
        Point p1 = new Point(1.1, 2.2); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);

        NaivePointSet nn = new NaivePointSet(List.of(p1, p2, p3));
        Point ret = nn.nearest(3.0, 4.0); // returns p2
        System.out.println(ret.getX());
        System.out.println(ret.getY());
    }
}
