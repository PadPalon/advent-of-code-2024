package ch.neukom.advent2024.day1;

import ch.neukom.advent2024.util.InputResourceReader;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ch.neukom.advent2024.day1.Util.loadValues;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        List<Long> leftValues = new ArrayList<>();
        Multiset<Long> rightValues = HashMultiset.create();
        loadValues(reader, leftValues, rightValues);
        Long sum = leftValues.stream()
            .map(value -> value * rightValues.count(value))
            .reduce(0L, Long::sum);
        System.out.printf("The sum of similarity scores is %s", sum);
    }
}
