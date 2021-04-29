import java.util.HashSet;
import java.util.Set;

public class CTCI {

    // Private helper for set to array convertion
    private static int[] set2Array(Set<Integer> set) {
        int size = set.size();
        int[] arr = new int[size];
        int index = 0;
        for (int i : set) {
            arr[index] = i;
            index++;
        }
        return arr;
    }

    public static int[] union(int[] A, int[] B) {
        Set<Integer> unionSet = new HashSet<>();
        for (int i : A) {
            unionSet.add(i);
        }
        for (int j : B) {
            unionSet.add(j);
        }

        return set2Array(unionSet);
    }

    public static int[] intersect(int[] A, int[] B) {
        // Try to check if set B contains element in set A
        Set<Integer> setA = new HashSet<>();
        Set<Integer> intersectSet = new HashSet<>();
        for (int i : A) {
            setA.add(i);
        }
        for (int i : B) {
            if (setA.contains(i)) {
                intersectSet.add(i);
            }
        }

        return set2Array(intersectSet);
    }

}
