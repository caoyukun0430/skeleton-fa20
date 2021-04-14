import java.util.function.Predicate;

public class FilteredPredicate<T> implements Predicate<T> {

    @Override
    public boolean test(T t) {
        Integer refer = 3;
        return (Integer) t > refer;
    }
}
