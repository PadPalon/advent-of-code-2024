package ch.neukom.advent2024.day1;

import ch.neukom.advent2024.util.InputResourceReader;

import java.util.Arrays;
import java.util.Collection;

public class Util {
    private Util() {
    }

    public static void loadValues(InputResourceReader reader,
                                  Collection<Long> leftValues,
                                  Collection<Long> rightValues) {
        reader.readInput()
            .map(line -> line.split(" "))
            .map(parts -> Arrays.stream(parts).filter(part -> !part.isEmpty()).map(Long::valueOf).toList())
            .map(parts -> new Tuple(parts.getFirst(), parts.getLast()))
            .forEach(tuple -> {
                leftValues.add(tuple.left());
                rightValues.add(tuple.right());
            });
    }

    private record Tuple(Long left, Long right) {
    }
}
