import java.util.Iterator;
public class OHQueue implements Iterable<OHRequest> {
    private OHRequest curr;
    public OHQueue(OHRequest queue) {
        curr = queue;
    }
    @Override
    public Iterator<OHRequest> iterator() {
        return new OHIterator(curr);
    }

    public static void main(String[] args) {
        OHRequest s5 = new OHRequest("I deleted all of my files", "Alex", null);
        OHRequest s4 = new OHRequest("conceptual: what is Java", "Omar", s5);
        OHRequest s3 = new OHRequest("git: I never did lab 1", "Connor", s4);
        OHRequest s2 = new OHRequest("help", "Hug", s3);
        OHRequest s1 = new OHRequest("no I haven't tried stepping through", "Itai", s2);


        OHQueue ohQueue = new OHQueue(s1);
        String str = "";
        for (OHRequest i : ohQueue) {
//            System.out.println(i.name);
            str += i.name + ", ";
        }
        System.out.println(str);

    }
}
