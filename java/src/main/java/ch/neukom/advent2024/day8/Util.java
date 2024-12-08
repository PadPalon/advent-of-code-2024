package ch.neukom.advent2024.day8;

import ch.neukom.advent2024.util.data.Position;
import ch.neukom.advent2024.util.inputreaders.InputMapReader;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Util {
    private Util() {
    }

    public static Set<Position> findAntinodes(InputMapReader reader, AntinodeCalculator handler) {
        Map<Character, Set<Position>> groupedAntennas = getGroupedAntennas(reader);
        Set<Position> antinodePositions = Sets.newHashSet();
        Function<Position, Boolean> antinodeConsumer = getAntinodeConsumer(reader, antinodePositions);
        groupedAntennas.values()
            .stream()
            .map(positions -> Sets.combinations(positions, 2))
            .flatMap(Collection::stream)
            .forEach(antennaCombinations -> {
                List<Position> sortedAntennas = antennaCombinations.stream().toList();
                Position firstAntenna = sortedAntennas.getFirst();
                Position secondAntenna = sortedAntennas.getLast();
                handleAntennaCombination(firstAntenna, secondAntenna, antinodeConsumer, handler);
            });
        return antinodePositions;
    }

    private static Map<Character, Set<Position>> getGroupedAntennas(InputMapReader reader) {
        Map<Position, Character> antennaMap = reader.readIntoMap();
        return antennaMap.entrySet()
            .stream()
            .filter(entry -> entry.getValue() != '.')
            .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toSet())));
    }

    private static Function<Position, Boolean> getAntinodeConsumer(InputMapReader reader, Set<Position> antinodePositions) {
        int width = reader.getFirstLine().length();
        int height = (int) reader.readInput().count();
        return position -> {
            if (position.isInside(width, height)) {
                antinodePositions.add(position);
                return true;
            } else {
                return false;
            }
        };
    }

    private static void handleAntennaCombination(Position firstAntenna,
                                                 Position secondAntenna,
                                                 Function<Position, Boolean> antinodeConsumer,
                                                 AntinodeCalculator handler) {
        int horizontalDistance = Math.abs(firstAntenna.x() - secondAntenna.x());
        int verticalDistance = Math.abs(firstAntenna.y() - secondAntenna.y());
        if (firstAntenna.x() < secondAntenna.x() && firstAntenna.y() < secondAntenna.y()) {
            handler.handle(firstAntenna, horizontalDistance, verticalDistance, false, false, antinodeConsumer);
            handler.handle(secondAntenna, horizontalDistance, verticalDistance, true, true, antinodeConsumer);
        } else if (firstAntenna.x() < secondAntenna.x() && firstAntenna.y() > secondAntenna.y()) {
            handler.handle(firstAntenna, horizontalDistance, verticalDistance, false, true, antinodeConsumer);
            handler.handle(secondAntenna, horizontalDistance, verticalDistance, true, false, antinodeConsumer);
        } else if (firstAntenna.x() > secondAntenna.x() && firstAntenna.y() < secondAntenna.y()) {
            handler.handle(firstAntenna, horizontalDistance, verticalDistance, true, false, antinodeConsumer);
            handler.handle(secondAntenna, horizontalDistance, verticalDistance, false, true, antinodeConsumer);
        } else if (firstAntenna.x() > secondAntenna.x() && firstAntenna.y() > secondAntenna.y()) {
            handler.handle(firstAntenna, horizontalDistance, verticalDistance, true, true, antinodeConsumer);
            handler.handle(secondAntenna, horizontalDistance, verticalDistance, false, false, antinodeConsumer);
        }
    }

    public static Boolean collectAntinode(Position sourceAntenna,
                                          int horizontalDistance,
                                          int verticalDistance,
                                          boolean right,
                                          boolean bottom,
                                          Function<Position, Boolean> antinodeConsumer) {
        Position antinode = new Position(
            sourceAntenna.x() + horizontalDistance * (right ? 1 : -1),
            sourceAntenna.y() + verticalDistance * (bottom ? 1 : -1)
        );
        return antinodeConsumer.apply(antinode);
    }

    public static void collectRepeatedAntinodes(Position sourceAntenna,
                                                int horizontalDistance,
                                                int verticalDistance,
                                                boolean right,
                                                boolean bottom,
                                                Function<Position, Boolean> antinodeConsumer) {
        boolean valid = true;
        int index = 0;
        while (valid) {
            valid = collectAntinode(
                sourceAntenna,
                horizontalDistance * index,
                verticalDistance * index,
                right,
                bottom,
                antinodeConsumer
            );
            index++;
        }
    }

    @FunctionalInterface
    public interface AntinodeCalculator {
        void handle(Position sourceAntenna,
                    int horizontalDistance,
                    int verticalDistance,
                    boolean right,
                    boolean bottom,
                    Function<Position, Boolean> antinodeConsumer);
    }
}
