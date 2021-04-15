import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by hug.
 */
public class TestRedBlackFloorSet {
    private static double DELTA = 1e-6;
    @Test
    public void randomizedTest() {
        AListFloorSet aListFloorSet = new AListFloorSet();
        RedBlackFloorSet redBlackFloorSet = new RedBlackFloorSet();
        // generate two lists
        for (int i = 0; i < 1000000; i++) {
            Double num = StdRandom.uniform(-5000.0, 5000.0);
            aListFloorSet.add(num);
            redBlackFloorSet.add(num);
        }

        // compare results to verify redBlackFloorSet
        for (int i = 0; i < 100000; i++) {
            Double num = StdRandom.uniform(-5000.0, 5000.0);
            Double expected = aListFloorSet.floor(num);
            Double actual = redBlackFloorSet.floor(num);
            assertEquals(expected, actual, DELTA);
        }
    }
}
