/**
 * @author Vivant Sakore on 1/29/2020
 */
public class BuggyIntDList extends IntDList {

    /**
     * @param values creates a BuggyIntDList with ints values.
     */
    public BuggyIntDList(Integer... values) {
        super(values);
    }


    /**
     * Merge IntDList `l` into the calling IntDList
     * Assume that the two IntDLists being merged are sorted individually before merge.
     * The resulting IntDList must also be sorted in ascending order.
     *
     * @param l Sorted IntDList to merge
     */
    public void mergeIntDList(IntDList l) {
        front = sortedMerge(this.front, l.front);
    }

    /**
     * Recursively merge nodes after value comparison
     *
     * @param d1 Node 1
     * @param d2 Node 2
     * @return Nodes arranged in ascending sorted order
     */
    private DNode sortedMerge(DNode d1, DNode d2) {

        // FIXME: Two issue. sortedMerge recursive inputs are wrong. Base cases are not handled.

        // ------ WRITE ADDITIONAL CODE HERE AND ONLY HERE (IF NEEDED) ------
        // Base case and we need to update back instance variable *back* for IntDList
        if (d1 == null) {
            DNode dLast = d2;
            while (dLast.next != null) {
                dLast = dLast.next;
            }
            back = dLast;
            return d2;
        }
        if (d2 == null) {
            DNode dLast = d1;
            while (dLast.next != null) {
                dLast = dLast.next;
            }
            back = dLast;
            return d1;
        }

        // ------------------------------------------------------------------

        if (d1.val <= d2.val) {
            d1.next = sortedMerge(d1.next, d2);   // FIXME: FIXED.
            d1.next.prev = d1;
            d1.prev = null;
            return d1;
        } else {
            d2.next = sortedMerge(d1, d2.next);   // FIXME: FIXED.
            d2.next.prev = d2;
            d2.prev = null;
            return d2;
        }
    }


    /**
     * Reverses IntDList in-place (destructive). Does not create a new IntDList.
     */
    public void reverse() {

        // FIXME: Below code has multiple problems.

        DNode temp = null;
        DNode p = front;

        // HINT: What does this while loop do? Use Debugger and Java Visualizer to figure out.
        // Answer: loop over each element to reverse next and prev pointer
        while (p != null) {
            temp = p.prev;
            p.prev = p.next;
            p.next = temp;
            p = p.prev; // FIXME: Fixed, old p.next is now p.prev, iterate update p pos
        }

        // HINT: What does this if block do? Use Debugger and Java Visualizer to figure out.
        // Answer: while loop exits, temp points at the 2nd element of old p,
        // OR last 2nd element of reversed p.
        // So we need to update back and front based on temp pos
        if (temp != null) {
            // ------ WRITE ADDITIONAL CODE HERE AND ONLY HERE (IF NEEDED) -----
            DNode backpos = temp;
            while (backpos.next != null) {
                backpos = backpos.next;
            }
            back = backpos;
            // -----------------------------------------------------------------
            front = temp.prev; // FIXME: Fixed, temp is already the last 2nd element of reversed p
        }
    }
}
