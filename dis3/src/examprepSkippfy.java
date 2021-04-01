public class examprepSkippfy {

    /** Dis 3 exampreps
     * IntList A = IntList.list(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
     * IntList B = IntList.list(9, 8, 7, 6, 5, 4, 3, 2, 1);
     * Fill in the method skippify such that the result of calling skippify on A and B
     * are as below:
     * - After calling A.skippify(), A: (1, 3, 6, 10)
     * - After calling B.skippify(), B: (9, 7, 4)
     * */
    // refer to lectureCode-fa20\lists2\IntList.java
    public int first;
    public examprepSkippfy rest;
    public examprepSkippfy (int f, examprepSkippfy r) {
        this.first = f;
        this.rest = r;
    }

    public static void skippify(examprepSkippfy lst) {
        examprepSkippfy p = lst;
        int n = 1;
        while (p != null) {
            // n is number of hops, next records each hop pos
            examprepSkippfy next = p.rest;
            for (int i = 1; i <= n; i++) {
                // if we hit next is null, we breaks and return null, so nothing after p is included
                if(next == null) {
                    break;
                }
                next = next.rest;
            }
            // include nothing after p is next is null, otherwise get the new hopped pos
            p.rest = next;
            // update p to continue from hopped pos
            p = p.rest;
            n += 1;
        }
    }
}
