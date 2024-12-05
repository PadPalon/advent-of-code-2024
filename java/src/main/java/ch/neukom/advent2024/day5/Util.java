package ch.neukom.advent2024.day5;

import ch.neukom.advent2024.util.InputResourceReader;
import com.google.common.base.Splitter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.List;

public class Util {
    private static final Splitter COMMA_SPLITTER = Splitter.on(',').omitEmptyStrings().trimResults();

    private Util() {
    }

    public static ParsedInput parseInput(InputResourceReader reader) {
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
        return new ParsedInput(rules, updates);
    }

    public static boolean doesFollowRules(List<Long> update, Multimap<Long, Long> rules) {
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

    public static boolean doesFollowRules(List<Long> update,
                                          int index,
                                          Collection<Long> nextPages) {
        return nextPages
            .stream()
            .map(update::indexOf)
            .filter(nextIndex -> nextIndex >= 0)
            .allMatch(nextIndex -> nextIndex > index);
    }

    public record ParsedInput(Multimap<Long, Long> rules, List<List<Long>> updates) {
    }
}
