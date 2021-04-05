import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MyBuggyIntDListTest {
    /**
     * @author Vivant Sakore on 1/29/2020
     */

    private BuggyIntDList l, m;

    /**
     * Test mergeIntDList
     */
    @Test
    public void testMergeIntDList() {
        l = new BuggyIntDList(1, 5);
        m = new BuggyIntDList(3, 7);
        l.mergeIntDList(m);
        assertEquals("Size after merge should be 21", 4, l.size());
        assertEquals(".getFront() should be 1", 1, l.getFront());
        assertEquals(".getBack() should be 7", 7, l.getBack());
        assertEquals("1st item should be 1", 1, l.get(0));
        assertEquals("2nd item should be 3", 3, l.get(1));
        assertEquals("3rd item should be 5", 5, l.get(2));
        assertEquals("4th item should be 5", 7, l.get(3));
    }

    /**
     * Test reverse
     */
    @Test
    public void testReverse() {
        l = new BuggyIntDList(1, 2, 3);
        l.reverse();
        assertEquals("Size after reversal should be 5", 3, l.size());
        assertEquals(".getFront() after reversal should be 56", 3, l.getFront());
        assertEquals(".getBack() after reversal should be 12", 1, l.getBack());
    }
}
