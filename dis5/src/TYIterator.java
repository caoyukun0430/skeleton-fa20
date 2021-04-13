public class TYIterator extends OHIterator {

    public TYIterator(OHRequest queue) {
        super(queue);
    }

    @Override
    public OHRequest next() {
        OHRequest nextItem = super.next();
        // order is important, then nextItem points at last element,
        // curr returned from super.next() is already null
        if (curr != null && curr.description.contains("thank u")) {
            curr = curr.next;
        }
        return nextItem;
    }

    public static void main(String[] args) {
        OHRequest s5 = new OHRequest("I deleted all of my files", "Alex", null);
        OHRequest s4 = new OHRequest("conceptual: what is Java", "Omar", s5);
        OHRequest s3 = new OHRequest("thank u", "Connor", s4);
        OHRequest s2 = new OHRequest("thank u", "Hug", s3);
        OHRequest s1 = new OHRequest("no I haven't tried stepping through", "Itai", s2);

        OHIterator tyiIter = new TYIterator(s1);
        String str = "";
        while (tyiIter.hasNext()) {
            str += tyiIter.next().name;
        }
    }
}
