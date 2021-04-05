/** An Integer tester created by Flik Enterprises.
 * @author Josh Hug
 * */
public class Flik {
    /** @param a Value 1
     *  @param b Value 2
     *  @return Whether a and b are the same */
    public static boolean isSameNumber(Integer a, Integer b) {
        return a == b;
        // reason is https://stackoverflow.com/questions/3637936/java-integer-equals-vs
        // https://javarevisited.blogspot.com/2010/10/what-is-problem-while-using-in.html
        // Java cache int only -128 to 127, using a.equals(b)
    }
}
