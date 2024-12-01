package ch.neukom.advent2024.day1;

import ch.neukom.advent2024.util.InputResourceReader;
import com.google.common.collect.Streams;

import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Queue;

import static ch.neukom.advent2024.day1.Util.loadValues;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        Queue<Long> leftValues = new PriorityQueue<>();
        Queue<Long> rightValues = new PriorityQueue<>();
        loadValues(reader, leftValues::add, rightValues::add);
        long sum = Streams.zip(
            leftValues.stream(),
            rightValues.stream(),
            (left, right) -> Math.abs(left - right)
        ).reduce(0L, Long::sum);
        System.out.printf("The sum of differences is %s", sum);
    }
}
