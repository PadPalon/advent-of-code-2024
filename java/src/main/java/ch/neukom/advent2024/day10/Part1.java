package ch.neukom.advent2024.day10;

import ch.neukom.advent2024.util.characters.CharacterConversionUtil;
import ch.neukom.advent2024.util.data.Position;
import ch.neukom.advent2024.util.inputreaders.InputMapReader;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputMapReader reader = new InputMapReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputMapReader reader) {
        Map<Position, Integer> topoMap = reader.readIntoMap(CharacterConversionUtil::toInteger);
        int width = reader.getFirstLine().length();
        int height = (int) reader.readInput().count();
        long sum = topoMap.entrySet()
            .stream()
            .filter(entry -> entry.getValue() == 0)
            .map(entry -> new Util.TrailFinder(entry.getKey(), topoMap, width, height))
            .map(Util.TrailFinder::findTrails)
            .mapToLong(trails -> trails.stream().map(List::getFirst).distinct().count())
            .sum();
        System.out.printf("The sum of scores of the trailheads is %s", sum);
    }
}
