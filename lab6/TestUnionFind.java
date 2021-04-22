import org.junit.Test;
import static org.junit.Assert.*;

public class TestUnionFind {
    @Test
    public void testConnect() {
        UnionFind set1 = new UnionFind(9);
        for (int i = 0; i < 8; i++) {
            assertEquals(-1, set1.parent(i));
        }
        // example from https://docs.google.com/presentation/d/1xjJvrb4coslHuDP4BTfjhKQSiwqS--jE9nXm2aDCpSo/edit#slide=id.g9c7d308d74_0_72
        set1.connect(1, 9);
        assertEquals(0, set1.parent(1));
        assertEquals(-2, set1.parent(0));

        set1.connect(3, 2);
        set1.connect(5, 4);
        set1.connect(4, 2);

        assertEquals(-4, set1.parent(2));
        assertEquals(2, set1.find(4));
        assertEquals(2, set1.find(3));
        assertEquals(4, set1.parent(5));

        int[] expected = {-2, 0, -4, 2, 2, 4, -1, -1, -1};
        assertArrayEquals(expected, set1.parent);

        set1.connect(7, 6);
        set1.connect(8, 6);
        set1.connect(5, 7);

        int[] expected2 = {-2, 0, -7, 2, 2, 4, 2, 6, 6};
        assertArrayEquals(expected2, set1.parent);

    }
}
