package ch.neukom.advent2024.util.streams;

import com.google.common.collect.Streams;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public class StreamUtil {
    private StreamUtil() {
    }

    public static <T> Stream<T> filterWithIndex(Stream<T> stream, BiPredicate<T, Long> predicate) {
        return Streams.mapWithIndex(stream, (element, index) -> {
                if (predicate.test(element, index)) {
                    return element;
                } else {
                    return null;
                }
            })
            .filter(Objects::nonNull);
    }
}
