package ch.neukom.advent2024.day1;

import ch.neukom.advent2024.util.InputResourceReader;
import com.google.common.base.Splitter;

import java.util.function.Consumer;

public class Util {
    private static final Splitter WHITESPACE_SPLITTER = Splitter.on(' ').trimResults().omitEmptyStrings();

    private Util() {
    }

    public static void loadValues(InputResourceReader reader,
                                  Consumer<Long> leftConsumer,
                                  Consumer<Long> rightConsumer) {
        reader.readInput()
            .map(line -> WHITESPACE_SPLITTER.splitToStream(line).mapToLong(Long::valueOf).toArray())
            .map(numbers -> new Tuple(numbers[0], numbers[1]))
            .forEach(tuple -> {
                leftConsumer.accept(tuple.left());
                rightConsumer.accept(tuple.right());
            });
    }

    private record Tuple(Long left, Long right) {
    }
}
