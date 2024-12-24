package ch.neukom.advent2024.day23;

import ch.neukom.advent2024.util.collector.MultimapCollector;
import ch.neukom.advent2024.util.inputreaders.InputResourceReader;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        Multimap<String, String> connections = reader.readInput()
            .map(line -> line.split("-"))
            .collect(new MultimapCollector<>(
                (map, value) -> {
                    map.put(value[0], value[0]);
                    map.put(value[0], value[1]);
                    map.put(value[1], value[0]);
                    map.put(value[1], value[1]);
                },
                HashMultimap::create
            ));
        Set<String> potentialChiefComputers = connections.keySet()
            .stream()
            .filter(computer -> computer.startsWith("t"))
            .collect(Collectors.toSet());
        long possibleNetworks = Sets.combinations(connections.keySet(), 3)
            .stream()
            .filter(neighbours -> neighbours.stream().anyMatch(potentialChiefComputers::contains))
            .filter(neighbours -> neighbours.stream().allMatch(neighbour -> connections.get(neighbour).containsAll(neighbours)))
            .count();
        System.out.printf("There are %s computer networks containing three computers, one of which starts with a 't'", possibleNetworks);
    }
}
