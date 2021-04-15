public class AListFloorSet implements Lab5FloorSet {
    AList<Double> items;

    public AListFloorSet() {
        items = new AList<>();
    }

    public void add(double x) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(x)) {
                return;
            }
        }
        items.addLast(x);
    }

    public double floor(double x) {
        double largest = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < items.size(); i++) {
            double ith = items.get(i);
            if (ith <= x && ith > largest) {
                largest = ith;
            }
        }
        return largest;
    }

    public static void main(String[] args) {
        AListFloorSet aListFloorSet = new AListFloorSet();
        aListFloorSet.add(2.0);
        aListFloorSet.add(5.0);
        aListFloorSet.add(4.0);
        aListFloorSet.add(4.0);
        System.out.println(aListFloorSet);
        System.out.println(aListFloorSet.floor(4.0));

    }
}
