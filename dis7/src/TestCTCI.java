import org.junit.Test;

public class TestCTCI {

    @Test
    public void testUnion() {
        int[] A = new int[10];
        int[] B = new int[10];
        for (int i = 0; i < 7; i++) {
            A[i] = StdRandom.uniform(10);
            B[i] = StdRandom.uniform(10);
        }
        // Order is not guaranteed
        // https://stackoverflow.com/questions/9345651/ordering-of-elements-in-java-hashset
        int[] c = CTCI.union(A, B);
        for (int i : c) {
            System.out.println(i);
        }
    }

    @Test
    public void testIntersect() {
        int[] A = new int[10];
        int[] B = new int[10];
        for (int i = 0; i < 7; i++) {
            A[i] = StdRandom.uniform(10);
            B[i] = StdRandom.uniform(10);
        }

        int[] c = CTCI.intersect(A, B);
        for (int i : c) {
            System.out.println(i);
        }
    }
}
