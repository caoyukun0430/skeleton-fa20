import java.util.ArrayList;
import java.util.List;

/**
 * Class that collects timing information about AList construction.
 */
public class TimeAList {
    private static void printTimingTable(List<Integer> Ns, List<Double> times, List<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        List<Integer> Ns = new ArrayList<>();
        List<Double> times = new ArrayList<>();
        List<Integer> opCounts = new ArrayList<>();

        for (int i = 1000; i <= 128000; i = i * 2) {
            Stopwatch sw = new Stopwatch();
            AList<Integer> aList = new AList<>();

            for (int ops = 0; ops < i; ops++) {
                aList.addLast(ops);
            }

            double timeInSeconds = sw.elapsedTime();
            Ns.add(i);
            times.add(timeInSeconds);
            opCounts.add(i);
        }

        printTimingTable(Ns, times, opCounts);
    }

}
