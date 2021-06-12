package bearmaps.proj2d;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bearmaps.proj2c.AStarSolver;
import bearmaps.proj2c.WeightedEdge;
import bearmaps.proj2c.streetmap.Node;

/**
 * This class acts as a helper for the RoutingAPIHandler.
 * @author Josh Hug, ______
 */
public class Router {

    /**
     * Overloaded method for shortestPath that has flexibility to specify a solver
     * and returns a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(AugmentedStreetMapGraph g, double stlon, double stlat,
                                          double destlon, double destlat) {
        long src = g.closest(stlon, stlat);
        long dest = g.closest(destlon, destlat);
        return new AStarSolver<>(g, src, dest, 20).solution();
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    // Note: slight errors in test 0 and 2, all other passes
    // AND interactive tests on mapServer looks good!
    public static List<NavigationDirection> routeDirections(AugmentedStreetMapGraph g,
                                                            List<Long> route) {
        List<Node> nodes = g.getNodes();
        // Create a map between node id and node
        Map<Long, Node> id2Node = new HashMap<>();
        for (Node n : nodes) {
            id2Node.put(n.id(), n);
        }
        // Create the return list of directions
        List<NavigationDirection> routeDirections = new ArrayList<>();

        // Create three lists to record the bearing, direction for each node in route
        List<Double> bearingArray = new ArrayList<>();
        List<Integer> directionArray = new ArrayList<>();
        // We use the tempIndex to record the index when we need to add new element into
        // routeDirections
        // For example when way name/direction changes, but when we are still on the same way,
        // we don't need to create new element in routeDirections.
        int tempIndex = 0;
        for (int i = 0; i < route.size() - 1; i++) {
            Node v = id2Node.get(route.get(i));
            Node w = id2Node.get(route.get(i + 1));
            // bearing for current node v is between v and next node w
            double bearing = NavigationDirection.bearing(v.lon(), w.lon(), v.lat(), w.lat());
            bearingArray.add(bearing);
            // Initialize direction
            int direction = 0;
            if (i >= 1) {
                direction = NavigationDirection.getDirection(bearingArray.get(i - 1), bearing);
            }
            directionArray.add(direction);
            // way name is the way between current v and next node
            String way = getRoadName(g, v.id(), w.id());
            // Create a temp direction for each node and later decide if it differs with
            // existing direction
            NavigationDirection temp = new NavigationDirection();
            temp.way = way;
            temp.direction = direction;
            // // Initialize the start NavigationDirection once we get way name
            if (i == 0) {
                routeDirections.add(temp);
            }
            // The if conditions defines when we update and add new direction into routeDirections
            // 1. we will not add when current temp equals last element in routeDirections
            // 2. we will not add when we are on the same road and direction is
            // straight/slight left/slight right, because we will still continue on the same road
            if (!temp.equals(routeDirections.get(routeDirections.size() - 1))
                    && !(way.equals(routeDirections.get(routeDirections.size() - 1).way)
                    && (direction == 2 || direction == 3 || direction == 1))) {
                updateRouteDirections(tempIndex, i, id2Node, g, routeDirections, route);
                // After update last element in routeDirections, we add current direction
                // into list and update tempIndex
                routeDirections.add(temp);
                tempIndex = i;
            }
        }
        // the for loop doesn't update distance for the last routeDirection
        // since it doesn't count last node
        updateRouteDirections(tempIndex, route.size() - 1, id2Node,
                g, routeDirections, route);

        return routeDirections;
    }

    private static String getRoadName(AugmentedStreetMapGraph g, long v, long w) {
        List<WeightedEdge<Long>> edges = g.neighbors(v);
        for (WeightedEdge e : edges) {
            if ((long) e.to() == w) {
                return e.getName();
            }
        }
        return null;
    }

    // private helper to update the routeDirections last element once got the final distance
    // we only update distance when we finally change the direction, we count distance between
    // starting point tempIndex and end point current v
    private static void updateRouteDirections(int tempIndex, int currentIndex,
                                              Map<Long, Node> id2Node,
                                              AugmentedStreetMapGraph g,
                                              List<NavigationDirection> routeDirections,
                                              List<Long> route) {
        Node last = id2Node.get(route.get(currentIndex));
        Node st = id2Node.get(route.get(tempIndex));
        double dist = g.estimatedDistanceToGoal(st.id(), last.id());
        NavigationDirection updated = routeDirections.get(routeDirections.size() - 1);
        updated.distance = dist;
        routeDirections.set(routeDirections.size() - 1, updated);
    }

    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for. This is only
     * useful for Part IV of the project.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";

        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        /** Checks that a value is between the given ranges.*/
        private static boolean numInRange(double value, double from, double to) {
            return value >= from && value <= to;
        }

        /**
         * Calculates what direction we are going based on the two bearings, which
         * are the angles from true north. We compare the angles to see whether
         * we are making a left turn or right turn. Then we can just use the absolute value of the
         * difference to give us the degree of turn (straight, sharp, left, or right).
         * @param prevBearing A double in [0, 360.0]
         * @param currBearing A double in [0, 360.0]
         * @return the Navigation Direction type
         */
        private static int getDirection(double prevBearing, double currBearing) {
            double absDiff = Math.abs(currBearing - prevBearing);
            if (numInRange(absDiff, 0.0, 15.0)) {
                return NavigationDirection.STRAIGHT;

            }
            if ((currBearing > prevBearing && absDiff < 180.0)
                    || (currBearing < prevBearing && absDiff > 180.0)) {
                // we're going right
                if (numInRange(absDiff, 15.0, 30.0) || absDiff > 330.0) {
                    // bearmaps.proj2d.example of high abs diff is prev = 355 and curr = 2
                    return NavigationDirection.SLIGHT_RIGHT;
                } else if (numInRange(absDiff, 30.0, 100.0) || absDiff > 260.0) {
                    return NavigationDirection.RIGHT;
                } else {
                    return NavigationDirection.SHARP_RIGHT;
                }
            } else {
                // we're going left
                if (numInRange(absDiff, 15.0, 30.0) || absDiff > 330.0) {
                    return NavigationDirection.SLIGHT_LEFT;
                } else if (numInRange(absDiff, 30.0, 100.0) || absDiff > 260.0) {
                    return NavigationDirection.LEFT;
                } else {
                    return NavigationDirection.SHARP_LEFT;
                }
            }
        }


        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }

        /**
         * Returns the initial bearing (angle) between vertices v and w in degrees.
         * The initial bearing is the angle that, if followed in a straight line
         * along a great-circle arc from the starting point, would take you to the
         * end point.
         * Assumes the lon/lat methods are implemented properly.
         * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
         * @param lonV  The longitude of the first vertex.
         * @param latV  The latitude of the first vertex.
         * @param lonW  The longitude of the second vertex.
         * @param latW  The latitude of the second vertex.
         * @return The initial bearing between the vertices.
         */
        public static double bearing(double lonV, double lonW, double latV, double latW) {
            double phi1 = Math.toRadians(latV);
            double phi2 = Math.toRadians(latW);
            double lambda1 = Math.toRadians(lonV);
            double lambda2 = Math.toRadians(lonW);

            double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
            double x = Math.cos(phi1) * Math.sin(phi2);
            x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
            return Math.toDegrees(Math.atan2(y, x));
        }
    }
}
