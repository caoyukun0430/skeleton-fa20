public class TestBody {

    private static void testForce() {
        System.out.println("Checking force self...");

        Body sun = new Body(1.0e12, 2.0e11, -999, -999, 2.0e30, "sun.gif");
        Body saturn = new Body(2.3e12, 9.5e11, -999, -999, 6.0e26, "saturn.gif");

        double forceCalc = sun.calcForceExertedBy(saturn);
        // print in scientic notations https://stackoverflow.com/questions/2944822/format-double-value-in-scientific-notation
        System.out.printf("force exerted by saturn is %6.3e, expected %6.3e\n", forceCalc, 3.6e22);
    }
    public static void main(String[] args) {
        testForce();
    }
}