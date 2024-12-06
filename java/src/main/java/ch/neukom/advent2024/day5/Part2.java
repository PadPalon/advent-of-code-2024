package ch.neukom.advent2024.day5;

import ch.neukom.advent2024.util.inputreaders.InputResourceReader;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import java.io.IOException;
import java.util.List;

import static ch.neukom.advent2024.day5.Util.*;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        ParsedInput parsedInput = parseInput(reader);
        List<List<Long>> updates = parsedInput.updates();
        Multimap<Long, Long> rules = parsedInput.rules();

        List<List<Long>> unorderedUpdates = Lists.newArrayList();
        for (List<Long> update : updates) {
            boolean isOrdered = doesFollowRules(update, rules);
            if (!isOrdered) {
                unorderedUpdates.add(update);
            }
        }

        long sum = unorderedUpdates.stream()
            .map(update -> findOrderedUpdate(update, rules))
            .mapToLong(update -> update.get(update.size() / 2))
            .sum();

        System.out.printf("The sum of middle pages is %s", sum);
    }

    private static List<Long> findOrderedUpdate(List<Long> update, Multimap<Long, Long> rules) {
        return update.stream()
            .sorted((left, right) -> {
                if (rules.get(left).contains(right)) {
                    return -1;
                }
                if (rules.get(right).contains(left)) {
                    return 1;
                } else {
                    return 0;
                }
            })
            .toList();
    }
}
