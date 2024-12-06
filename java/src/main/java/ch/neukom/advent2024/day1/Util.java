package ch.neukom.advent2024.day1;

import ch.neukom.advent2024.util.inputreaders.InputResourceReader;
import ch.neukom.advent2024.util.splitter.Splitters;

import java.util.function.Consumer;

public class Util {
    private Util() {
    }

    public static void loadValues(InputResourceReader reader,
                                  Consumer<Long> leftConsumer,
                                  Consumer<Long> rightConsumer) {
        reader.readInput()
            .map(line -> Splitters.WHITESPACE_SPLITTER.splitToStream(line).mapToLong(Long::valueOf).toArray())
            .map(numbers -> new Tuple(numbers[0], numbers[1]))
            .forEach(tuple -> {
                leftConsumer.accept(tuple.left());
                rightConsumer.accept(tuple.right());
            });
    }

    private record Tuple(Long left, Long right) {
    }
}
