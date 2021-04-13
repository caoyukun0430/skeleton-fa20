import java.util.Iterator;
import java.util.NoSuchElementException;

public class OHIterator implements Iterator<OHRequest> {
    OHRequest curr;

    public OHIterator(OHRequest queue) {
        curr = queue;
    }

    public boolean isGood(String description) {
        return description != null && description.length() > 5;
    }

    // if the list itself is not null and we haven't hit the good desc,
    // we iterate to find next
    @Override
    public boolean hasNext() {
        while (curr != null && !isGood(curr.description)) {
            curr = curr.next;
        }
        return curr != null;
    }

    @Override
    public OHRequest next() {
        if (!hasNext()) {
            throw new NoSuchElementException("we run out of office hour requests");
        }
        OHRequest nextItem = curr;
        curr = curr.next;
        return nextItem;
    }

    public static void main(String[] args) {
        OHRequest s5 = new OHRequest("I deleted all of my files", "Alex", null);
        OHRequest s4 = new OHRequest("conceptual: what is Java", "Omar", s5);
        OHRequest s3 = new OHRequest("git: I never did lab 1", "Connor", s4);
        OHRequest s2 = new OHRequest("help", "Hug", s3);
        OHRequest s1 = new OHRequest("no I haven't tried stepping through", "Itai", s2);

        OHIterator ohIter = new OHIterator(s1);
        String str = "";
        while (ohIter.hasNext()) {
            str += ohIter.next().name;
        }
    }
}
