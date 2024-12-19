package ch.neukom.advent2024.day19;

import ch.neukom.advent2024.util.inputreaders.InputResourceReader;
import ch.neukom.advent2024.util.splitter.Splitters;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        Map<String, Long> cache = Maps.newHashMap();
        Set<String> availablePatterns = Splitters.COMMA_SPLITTER.splitToStream(reader.getFirstLine()).collect(toSet());
        long availableArrangements = reader.readInput()
            .skip(2)
            .mapToLong(design -> getArrangementPossibilities(design, availablePatterns, cache))
            .sum();
        System.out.printf("There are %s different arrangements possible", availableArrangements);
    }

    private static long getArrangementPossibilities(String design, Set<String> availablePatterns, Map<String, Long> cache) {
        if (Strings.isNullOrEmpty(design)) {
            return 0;
        }

        if (cache.containsKey(design)) {
            return cache.get(design);
        }

        long count = 0;
        for (String availablePattern : availablePatterns) {
            if (design.equals(availablePattern)) {
                count++;
            }

            if (design.startsWith(availablePattern)) {
                long subCount = getArrangementPossibilities(design.substring(availablePattern.length()), availablePatterns, cache);
                count += subCount;
            }
        }
        cache.put(design, count);
        return count;
    }
}
