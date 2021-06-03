package bearmaps.proj2c.integerhoppuzzle;

import bearmaps.proj2c.AStarSolver;
import bearmaps.proj2c.LazySolver;
import bearmaps.proj2c.ShortestPathsSolver;
import bearmaps.proj2c.SolutionPrinter;

/**
 * Showcases how the AStarSolver can be used for solving integer hop puzzles.
 * NOTE: YOU MUST REPLACE LazySolver WITH AStarSolver OR THIS DEMO WON'T WORK!
 * Created by hug.
 */
public class DemoIntegerHopPuzzleSolution {
    public static void main(String[] args) {
        int start = 17;
        int goal = 111;
        // Interesting results:
        // Solution was of length 9, and had total weight 26.0:
        //258 => 257 => 256 => 65536 => 0 => 1 => 2 => 3 => 4
//        int start = 258;
//        int goal = 4;

        IntegerHopGraph ahg = new IntegerHopGraph();

        ShortestPathsSolver<Integer> solver = new AStarSolver<>(ahg, start, goal, 10);
        SolutionPrinter.summarizeSolution(solver, " => ");

    }
}
