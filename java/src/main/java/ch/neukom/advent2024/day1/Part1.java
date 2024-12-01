package ch.neukom.advent2024.day1;

import ch.neukom.advent2024.util.InputResourceReader;
import com.google.common.collect.Streams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static ch.neukom.advent2024.day1.Util.loadValues;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        List<Long> leftValues = new ArrayList<>();
        List<Long> rightValues = new ArrayList<>();
        loadValues(reader, leftValues, rightValues);
        leftValues.sort(Comparator.naturalOrder());
        rightValues.sort(Comparator.naturalOrder());
        long sum = Streams.zip(
            leftValues.stream(),
            rightValues.stream(),
            (left, right) -> Math.abs(left - right)
        ).reduce(0L, Long::sum);
        System.out.printf("The sum of differences is %s", sum);
    }
}
