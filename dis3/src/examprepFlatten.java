public class examprepFlatten {

    /** Dis 3 exampreps
     * Write a method flatten that takes in a 2-D array x and returns a 1-D array that
     * contains all of the arrays in x concatenated together.
     * For example, flatten({{1, 2, 3}, {}, {7, 8}}) should return {1, 2, 3, 7, 8}.
     * */
    public static int[] flatten(int[][] x) {
        int totalLength = 0;
        for (int[] item : x) {
            totalLength += item.length;
        }
        int[] a = new int[totalLength];
        int aIndex = 0;
        for (int[] item : x) {
            for (int i = 0; i < item.length; i++) {
                a[aIndex] = item[i];
                aIndex++;
            }
        }
        return a;
    }

    public static void main(String[] args) {
        flatten(new int[][]{{1, 2, 3}, {}, {7, 8}});
    }
}
