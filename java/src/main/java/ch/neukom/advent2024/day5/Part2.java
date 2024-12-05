package ch.neukom.advent2024.day5;

import ch.neukom.advent2024.util.InputResourceReader;
import com.google.common.base.Splitter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
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

        long sum = unorderedUpdates.stream()
            .map(update -> findOrderedUpdate(update, rules))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .mapToLong(update -> update.get(update.size() / 2))
            .sum();

        System.out.printf("The sum of middle pages is %s", sum);
    }

    private static boolean doesFollowRules(List<Long> update, Multimap<Long, Long> rules) {
        boolean isOrdered = true;
        for (int index = 0; index < update.size(); index++) {
            Long currentPage = update.get(index);
            if (!doesFollowRules(update, index, rules.get(currentPage))) {
                isOrdered = false;
                break;
            }
        }
        return isOrdered;
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

    private static Optional<List<Long>> findOrderedUpdate(List<Long> update, Multimap<Long, Long> rules) {
        if (update.size() <= 1) {
            return Optional.of(update);
        } else if (doesFollowRules(update, rules)) {
            return Optional.of(update);
        } else {
            Optional<List<Long>> any = update.stream()
                .map(current -> findPossibleUpdates(update, List.of(current), rules))
                .filter(o -> o.isPresent())
                .map(o -> o.get())
                .findAny();

            return Optional.empty();
//            return update.stream()
//                .map(startingPage -> tryMutation(update, rules, startingPage))
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .findAny();
        }
    }

    private static Optional<List<Long>> findPossibleUpdates(List<Long> update, List<Long> soFar, Multimap<Long, Long> rules) {
        List<List<Long>> list = rules.get(soFar.getFirst())
            .stream()
            .filter(n -> update.contains(n))
            .filter(n -> !soFar.contains(n))
            .map(n -> (List<Long>) ImmutableList.<Long>builder()
                .addAll(soFar)
                .add(n)
                .build())
            .toList();
        for (List<Long> longs : list) {
            if (longs.size() == update.size()) {
                return Optional.of(longs);
            } else {
                return findPossibleUpdates(update, longs, rules);
            }
        }
        return Optional.empty();
    }

    private static Optional<List<Long>> tryMutation(List<Long> update, Multimap<Long, Long> rules, Long startingPage) {
        int size = update.size();

        List<Long> availablePages = copyListRemoveElement(update, startingPage);
        return rules.get(startingPage)
            .stream()
            .filter(availablePages::contains)
            .map(nextPage -> {
                ImmutableList.Builder<Long> builder = ImmutableList.<Long>builder()
                    .add(nextPage);
                availablePages.stream().filter(page -> !page.equals(nextPage)).forEach(builder::add);
                return builder.build();
            })
            .map(newUpdate -> findOrderedUpdate(newUpdate, rules))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(newUpdate -> {
                ImmutableList.Builder<Long> builder = ImmutableList.<Long>builder()
                    .add(startingPage);
                newUpdate.forEach(builder::add);
                return (List<Long>) builder.build();
            })
            .filter(newUpdate -> doesFollowRules(newUpdate, rules))
            .findAny();
    }

    private static List<Long> copyListRemoveFirst(List<Long> source) {
        List<Long> result = Lists.newCopyOnWriteArrayList(source);
        result.removeFirst();
        return result;
    }

    private static List<Long> copyListRemoveElement(List<Long> source, Long element) {
        List<Long> result = Lists.newCopyOnWriteArrayList(source);
        result.remove(element);
        return result;
    }
}
