package ch.neukom.advent2024.day5;

import ch.neukom.advent2024.util.inputreaders.InputResourceReader;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import java.io.IOException;
import java.util.List;

import static ch.neukom.advent2024.day5.Util.*;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        ParsedInput parsedInput = parseInput(reader);
        List<List<Long>> updates = parsedInput.updates();
        Multimap<Long, Long> rules = parsedInput.rules();

        List<Long> middlePages = Lists.newArrayList();
        for (List<Long> update : updates) {
            boolean isOrdered = doesFollowRules(update, rules);
            if (isOrdered) {
                middlePages.add(update.get(update.size() / 2));
            }
        }

        long sum = middlePages.stream().mapToLong(Long::longValue).sum();
        System.out.printf("The sum of middle pages is %s", sum);
    }
}
