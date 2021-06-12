package bearmaps.proj2d;

import bearmaps.proj2ab.KDTree;
import bearmaps.proj2ab.Point;
import bearmaps.proj2c.streetmap.StreetMapGraph;
import bearmaps.proj2c.streetmap.Node;
import edu.princeton.cs.algs4.TrieST;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {
    private List<Node> nodes;
    private Map<String, Node> cleanName2Node;
    private TrieST<Long> trieST;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        nodes = this.getNodes();
        // create a map from cleaned name to node for all nodes, here we don't use node id
        // since later we need to retrieve node info from the map values
        // create a tries taking the cleaned names as input
        cleanName2Node = new HashMap<>();
        trieST = new TrieST<>();
        for (Node n : nodes) {
            if (n.name() != null) {
                String key = cleanString(n.name());
                cleanName2Node.put(key, n);
                trieST.put(key, n.id());
            }
        }
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        // Initialize point set for nearest() and map between point and node id
        List<Point> pointList = new ArrayList<>();
        Map<Point, Long> point2id = new HashMap<>();
        // nearest() only considers vertices that have neighbors
        for (Node n : nodes) {
            if (neighbors(n.id()).size() != 0) {
                // Position is in coordinate(lon, lat) since X is lon, Y is lat
                Point p = new Point(n.lon(), n.lat());
                pointList.add(p);
                point2id.put(p, n.id());
            }
        }
        // Use kdtree to find the closest point and returns the id from the map
        KDTree kd = new KDTree(pointList);
        Point closest = kd.nearest(lon, lat);
        return point2id.get(closest);
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        Iterable<String> matchedKeys = trieST.keysWithPrefix(cleanString(prefix));
        System.out.println("matchedKeys" + matchedKeys);

        LinkedList<String> returnedNames = new LinkedList<>();
        // for each matched keys, we need to fetch the real name from the value, i.e. the node
        for (String s : matchedKeys) {
            String realName = cleanName2Node.get(s).name();
            returnedNames.add(realName);
        }
        System.out.println("returnedNames" + returnedNames);
        return returnedNames;
    }

//    // clean everything except characters A through Z
//    // everything is lowercase
//    private String getCleanName(String str) {
// From https://stackoverflow.com/questions/5455794/removing-whitespace-from-strings-in-java
// use "\\W" to remove Anything that isn't a word character (including punctuation etc)
//        String cleaned = str.replaceAll("\\W", "");
//        return cleaned.toLowerCase();
//    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        Iterable<String> matchedLocs = trieST.keysThatMatch(cleanString(locationName));
        System.out.println("matchedLocs" + matchedLocs);
        List<Map<String, Object>> returnedLocs =  new LinkedList<>();
        for (String s : matchedLocs) {
            // each location is a map of parameters for the Json
            Map<String, Object> locationMap = new HashMap<>();
            locationMap.put("name", cleanName2Node.get(s).name());
            locationMap.put("lat", cleanName2Node.get(s).lat());
            locationMap.put("lon", cleanName2Node.get(s).lon());
            locationMap.put("id", cleanName2Node.get(s).id());
            returnedLocs.add(locationMap);
        }
        return returnedLocs;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
