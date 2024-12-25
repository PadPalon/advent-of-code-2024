package ch.neukom.advent2024.day25;

import ch.neukom.advent2024.util.inputreaders.InputResourceReader;
import ch.neukom.advent2024.util.splitter.Splitters;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ch.neukom.advent2024.day25.Util.*;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        String input = reader.readInput().collect(joining("\n"));
        Map<Boolean, List<Security>> securities = Splitters.PARAGRAPH_SPLITTER.splitToStream(input)
            .map(Util::parseSecurity)
            .collect(groupingBy(Lock.class::isInstance));
        List<Lock> locks = securities.get(true).stream().map(Lock.class::cast).toList();
        List<Key> keys = securities.get(false).stream().map(Key.class::cast).toList();
        Set<Combination> validCombinations = Sets.newHashSet();
        for (Lock lock : locks) {
            for (Key key : keys) {
                if (lock.fits(key)) {
                    validCombinations.add(new Combination(lock, key));
                }
            }
        }
        System.out.printf("There are %s unique lock / key pairs that fit each other", validCombinations.size());
    }
}
