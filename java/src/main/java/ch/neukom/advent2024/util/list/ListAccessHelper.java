package ch.neukom.advent2024.util.list;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ListAccessHelper {
    private ListAccessHelper() {
    }

    public static <R> Optional<R> getLastMatching(List<R> list, Predicate<R> predicate) {
        for (int index = list.size() - 1; index > 0; index--) {
            R element = list.get(index);
            if (predicate.test(element)) {
                return Optional.of(element);
            }
        }
        return Optional.empty();
    }

    public static <R> int getLastIndexMatching(List<R> list, Predicate<R> predicate) {
        for (int index = list.size() - 1; index > 0; index--) {
            R element = list.get(index);
            if (predicate.test(element)) {
                return index;
            }
        }
        return -1;
    }
}
