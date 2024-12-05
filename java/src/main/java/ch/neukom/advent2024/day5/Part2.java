package ch.neukom.advent2024.day5;

import ch.neukom.advent2024.util.InputResourceReader;
import com.google.common.base.Splitter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class Part2 {
    private static final Splitter COMMA_SPLITTER = Splitter.on(',').omitEmptyStrings().trimResults();

    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
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

        List<List<Long>> unorderedUpdates = Lists.newArrayList();
        for (List<Long> update : updates) {
            for (int index = 0; index < update.size(); index++) {
                Long currentPage = update.get(index);
                if (!doesFollowRules(update, index, rules.get(currentPage))) {
                    unorderedUpdates.add(update);
                    break;
                }
            }
        }

        List<List<Long>> orderedUpdates = Lists.newArrayList();
        for (List<Long> unorderedUpdate : unorderedUpdates) {
            for (Long startingPage : unorderedUpdate) {
                List<Long> availablePages = Lists.newCopyOnWriteArrayList(unorderedUpdate);
                availablePages.remove(startingPage);
                List<Long> newOrdering = Lists.newArrayList();
                newOrdering.add(startingPage);

                Long currentPage = startingPage;
                while (!availablePages.isEmpty()) {
                    Optional<Long> nextPageOptional = rules.get(currentPage)
                        .stream()
                        .filter(availablePages::contains)
                        .findAny();
                    if (nextPageOptional.isPresent()) {
                        Long nextPage = nextPageOptional.get();
                        newOrdering.add(nextPage);
                        availablePages.remove(nextPage);
                        currentPage = nextPage;
                    }
                }

                if (newOrdering.size() == updates.size()) {
                    orderedUpdates.add(newOrdering);
                }
            }
        }

        System.out.println();
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
