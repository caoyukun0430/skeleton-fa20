package bearmaps.proj2c;

import bearmaps.proj2ab.ArrayHeapMinPQ;
import bearmaps.proj2ab.MyHashMap;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.List;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {

    // instances constructor needs
    private MyHashMap<Vertex, Vertex> edgeTo;
    private MyHashMap<Vertex, Double> distTo;
    // In this new version of A*, the algorithm can
    // theoretically revisit the same vertex many times, so
    // we don't check is vertex is visited
    private MyHashMap<Vertex, Boolean> marked;
    private ArrayHeapMinPQ<Vertex> fringePQ;
    private AStarGraph<Vertex> graph;
    private Vertex goal;
    private Vertex source;

    // instances methods need
    private SolverOutcome outcome;
    private double solutionWeight;
    // Solution is the vertices list to the goal, But no the solution of Dijkstra,
    // which contains all vertices in the graph
    private ArrayList<Vertex> solution;
    private double timeSpent;
    private double timeout;
    private int numStatesExplored;

    private void relax(WeightedEdge<Vertex> e) {
        Vertex to = e.to();
        Vertex from = e.from();
        double weight = e.weight();
        // if q is visited, returns directly
        if (marked.get(to)) {
            return;
        }
        double newDist = distTo.get(from) + weight;
        double oldDist = distTo.get(to);
        if (newDist < oldDist) {
            distTo.put(to, newDist);
            edgeTo.put(to, from);
            if (fringePQ.contains(to)) {
                fringePQ.changePriority(to, newDist + graph.estimatedDistanceToGoal(to, goal));
            } else {
                fringePQ.add(to, newDist + graph.estimatedDistanceToGoal(to, goal));
            }
        }

    }
    // Constructor
    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        // two instances needed by relax().
        graph = input;
        goal = end;
        source = start;
        // Initialize PQ, distTo, edgeTo with the source vertex.
        distTo = new MyHashMap<>();
        edgeTo = new MyHashMap<>();
        marked = new MyHashMap<>();
        fringePQ = new ArrayHeapMinPQ<>();
        distTo.put(start, 0.0);
        edgeTo.put(start, start);
        fringePQ.add(start, input.estimatedDistanceToGoal(start, end));
        // Initialize the solution list.
        solution = new ArrayList<>();
        // Starting count the time
        Stopwatch sw = new Stopwatch();
        timeSpent = sw.elapsedTime();
        this.timeout = timeout;
        // Repeat until the PQ is empty, PQ.getSmallest() is the goal, or timeout is exceeded
        while (!(fringePQ.size() == 0 || fringePQ.getSmallest().equals(end) || timeSpent > timeout)) {
            Vertex p = fringePQ.removeSmallest();
            //  only “mark” a vertex when it is dequeued from the PQ
            marked.put(p, true);
            numStatesExplored += 1;
            timeSpent = sw.elapsedTime();
            // check to see if you have run out of time every time you dequeue.
            if (timeSpent > timeout) {
                break;
            }
            List<WeightedEdge<Vertex>> neighborEdges = input.neighbors(p);
            for (WeightedEdge<Vertex> e : neighborEdges) {
                // if the e.to() is not in distTo yet, we initialize before relax()
                if (!distTo.containsKey(e.to())) {
                    distTo.put(e.to(), Double.POSITIVE_INFINITY);
                    edgeTo.put(e.to(), null);
                    marked.put(e.to(), false);
                }
                relax(e);
            }
        }
        timeSpent = sw.elapsedTime();
        // If algo is solvable, we form the solution
        if (fringePQ.getSmallest().equals(goal)) {
            solution = solutionHelper(goal);
        }
    }
    /**
     * @Return: one of SolverOutcome.SOLVED, SolverOutcome.TIMEOUT,
     * or SolverOutcome.UNSOLVABLE.
     *  Should be SOLVED if the AStarSolver was able to complete all work in the time given.
     *  UNSOLVABLE if the priority queue became empty. TIMEOUT if the solver ran out of time.
     * */
    @Override
    public SolverOutcome outcome() {
        if (timeSpent > this.timeout) {
            outcome = SolverOutcome.TIMEOUT;
        } else if (fringePQ.getSmallest().equals(goal)) {
            outcome = SolverOutcome.SOLVED;
        } else {
            outcome = SolverOutcome.UNSOLVABLE;
        }
        return outcome;
    }

    // Recursion helper to get the solution list from source to goal
    private ArrayList<Vertex> solutionHelper(Vertex v) {
        if(v.equals(source)){
            solution.add(v);
            return solution;
        }
        solutionHelper(edgeTo.get(v));
        solution.add(v);
        return solution;
    }

    /**
     * Returns a list of vertices corresponding to a solution.
     * */
    @Override
    public List<Vertex> solution() {
        if (outcome() == SolverOutcome.TIMEOUT || outcome() == SolverOutcome.UNSOLVABLE) {
            return null;
        }
//        System.out.println(solution);
        return solution;
    }

    /**
     * The total weight of the given solution, taking into account edge weights.
     * Should be 0 if result was TIMEOUT or UNSOLVABLE.
     * */
    @Override
    public double solutionWeight() {
        if (outcome() == SolverOutcome.TIMEOUT || outcome() == SolverOutcome.UNSOLVABLE) {
            solutionWeight = 0.0;
        } else {
            solutionWeight = distTo.get(goal);
        }
        return solutionWeight;
    }

    /**
     * The total number of priority queue dequeue operations.
     * */
    @Override
    public int numStatesExplored() {
        return numStatesExplored;
    }

    /**
     * The total time spent in seconds by the constructor.
     * */
    @Override
    public double explorationTime() {
        return timeSpent;
    }

}
