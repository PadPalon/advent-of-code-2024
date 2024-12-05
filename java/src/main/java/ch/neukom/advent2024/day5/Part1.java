package ch.neukom.advent2024.day5;

import ch.neukom.advent2024.util.InputResourceReader;
import com.google.common.base.Splitter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class Part1 {
    private static final Splitter COMMA_SPLITTER = Splitter.on(',').omitEmptyStrings().trimResults();

    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        Multimap<Long, Long> rules = HashMultimap.create();
        List<List<Long>> updates = Lists.newArrayList();
        reader.readInput()
            .forEach(line -> {
                if (line.contains("|")) {
                    String[] parts = line.split("\\|");
                    rules.put(Long.valueOf(parts[0]), Long.valueOf(parts[1]));
                } else if (!line.isEmpty()) {
                    updates.add(COMMA_SPLITTER.splitToStream(line).map(Long::valueOf).toList());
                }
            });

        List<Long> middlePages = Lists.newArrayList();
        for (List<Long> update : updates) {
            boolean isOrdered = true;
            for (int index = 0; index < update.size(); index++) {
                Long currentPage = update.get(index);
                if (!doesFollowRules(update, index, rules.get(currentPage))) {
                    isOrdered = false;
                    break;
                }
            }
            if (isOrdered) {
                middlePages.add(update.get(update.size() / 2));
            }
        }

        long sum = middlePages.stream().mapToLong(Long::longValue).sum();
        System.out.printf("The sum of middle pages is %s", sum);
    }

    private static boolean doesFollowRules(List<Long> update,
                                           int index,
                                           Collection<Long> nextPages) {
        return nextPages
            .stream()
            .map(update::indexOf)
            .filter(nextIndex -> nextIndex >= 0)
            .allMatch(nextIndex -> nextIndex > index);
    }
}
