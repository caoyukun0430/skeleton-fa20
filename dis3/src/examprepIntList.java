public class examprepIntList {
    // Dis 3 exampreps
    // refer to lectureCode-fa20\lists2\IntList.java
    public int first;
    public examprepIntList rest;
    public examprepIntList (int f, examprepIntList r) {
        this.first = f;
        this.rest = r;
    }
    public static void evenOdd(examprepIntList lst) {
        // no need to reorder if there is only 0, 1, 2 elements
        if (lst == null || lst.rest == null || lst.rest.rest == null) {
            return;
        }
        // Second record the starting pos of odd list
        examprepIntList second = lst.rest;
        int index = 0;
        // only when index % 2 = 0 and it's last element(even length) or next last element(odd length) will we exit
        while (!(index % 2 == 0 && (lst.rest == null || lst.rest.rest == null))) {
            // Save location before overwrite by .next
            examprepIntList temp = lst.rest;
            lst.rest = lst.rest.rest;
            lst = temp;
            index++;
        }
        lst.rest = second;
    }

}
