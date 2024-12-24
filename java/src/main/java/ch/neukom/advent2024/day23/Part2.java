package ch.neukom.advent2024.day23;

import ch.neukom.advent2024.util.collector.MultimapCollector;
import ch.neukom.advent2024.util.inputreaders.InputResourceReader;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        HashMultimap<String, String> connections = reader.readInput()
            .map(line -> line.split("-"))
            .collect(new MultimapCollector<>(
                (map, value) -> {
                    map.put(value[0], value[1]);
                    map.put(value[1], value[0]);
                },
                HashMultimap::create
            ));
        String password = findBiggestNetwork(connections, Sets.newHashSet(), connections.keySet(), Sets.newHashSet())
            .stream()
            .flatMap(Collection::stream)
            .sorted()
            .collect(Collectors.joining(","));
        System.out.printf("The password to the lan party network is %s", password);
    }

    /**
     * do not take me for a very smart person, this is just an implementation of the most basic Bron-Kerbosch algorithm
     * but discarding any networks that are smaller than previously found ones
     */
    private static Optional<Set<String>> findBiggestNetwork(HashMultimap<String, String> connections, Set<String> all, Set<String> some, Set<String> none) {
        if (some.isEmpty() && none.isEmpty()) {
            return Optional.of(all);
        }

        Optional<Set<String>> biggestClique = Optional.empty();
        Set<String> remaining = Sets.newHashSet(some);
        for (String node : Iterables.unmodifiableIterable(some)) {
            Optional<Set<String>> result = findBiggestNetwork(
                connections,
                ImmutableSet.<String>builder().addAll(all).add(node).build(),
                Sets.newHashSet(Sets.intersection(remaining, connections.get(node))),
                Sets.newHashSet(Sets.intersection(none, connections.get(node)))
            );
            Integer currentCliqueSize = biggestClique.map(Set::size).orElse(0);
            Set<String> currentBiggestClique = biggestClique.orElse(null);
            biggestClique = result.filter(clique -> currentCliqueSize < clique.size())
                .or(() -> Optional.ofNullable(currentBiggestClique));
            remaining.remove(node);
            none.add(node);
        }
        return biggestClique;
    }
}
