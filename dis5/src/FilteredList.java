import java.util.List;
import java.util.function.Predicate;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class FilteredList<T> implements Iterable<T> {

    public List<T> items;
    public Predicate<T> f;

    public FilteredList(List<T> L, Predicate<T> filter) {
        items = L;
        f = filter;
    }

    @Override
    public Iterator<T> iterator() {
        return new FilteredListIterator(items, f);
    }

    private class FilteredListIterator implements Iterator<T> {
        public List<T> items;
        public Predicate<T> f;
        int index;

        // constructor shows how we instantiate the iterator with the two inputs
        // we got when FilteredList<Integer> flist = new FilteredList<>(nums, filter);
        // since FilteredList is Iterable, it calls iterator() once we print the elements
        public FilteredListIterator(List<T> L, Predicate<T> filter) {
            items = L;
            f = filter;
            index = 0;
        }

        @Override
        public boolean hasNext() {
            // we iterate until we find one item x pass test
            while (index < items.size() && !f.test(items.get(index))) {
                index += 1;
            }
            return index < items.size();
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("we run out of items");
            }
            T filtered = items.get(index);
            index += 1;
            return filtered;
        }
    }

    public static void main(String[] args) {
        List<Integer> nums = List.of(2, 6, 1, 5, 6, 7);
        Predicate<Integer> filter = new FilteredPredicate<>();
        FilteredList<Integer> flist = new FilteredList<>(nums, filter);
        for (int x : flist) {
            System.out.println(x);
        }
    }

}
