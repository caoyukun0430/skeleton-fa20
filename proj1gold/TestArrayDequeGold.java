import static org.junit.Assert.*;
import org.junit.Test;

import java.security.PublicKey;

/** @source StudentArrayDequeLauncher.java */
public class TestArrayDequeGold {
    public String msgStr = "";
    public void DequeAdd (StudentArrayDeque<Integer> stuAD, ArrayDequeSolution<Integer> solAD, int addedVal, double locator) {
        if (locator < 0.5) {
            stuAD.addLast(addedVal);
            solAD.addLast(addedVal);
            msgStr += "addLast(" + addedVal + ")\n";
        } else {
            stuAD.addFirst(addedVal);
            solAD.addFirst(addedVal);
            msgStr += "addFirst(" + addedVal + ")\n";
        }
    }

    public void DequeRemove (StudentArrayDeque<Integer> stuAD, ArrayDequeSolution<Integer> solAD, double locator) {
        Integer actual;
        Integer expected;
        if (locator < 0.5) {
            actual = stuAD.removeLast();
            expected = solAD.removeLast();
            msgStr += "removeLast()\n";
        } else {
            actual = stuAD.removeFirst();
            expected = solAD.removeFirst();
            msgStr += "removeFirst()\n";
        }
        assertEquals(msgStr, expected, actual);
    }

    @Test
    public void testStudentArrayDeque () {
        StudentArrayDeque<Integer> stuAD = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solAD = new ArrayDequeSolution<>();
        int numRun = 100;

        // Given two parameters, double methodSelection to randomly choose to add or remove
        // double locator to select to add(remove) at first or last
        // if we have an empty ArrayDeque, always perform add
        while (numRun > 0) {
            if (stuAD.size() == 0 || solAD.size() == 0) {
                double locator = StdRandom.uniform();
                DequeAdd(stuAD, solAD, numRun, locator);
            } else {
                double methodSelection = StdRandom.uniform();
                double locator = StdRandom.uniform();
                if (methodSelection < 0.5) {
                    DequeAdd(stuAD, solAD, numRun, locator);
                } else {
                    DequeRemove(stuAD, solAD, locator);
                }
            }
            numRun -= 1;
        }
    }
}
