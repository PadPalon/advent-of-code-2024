package ch.neukom.advent2024.day12;

import ch.neukom.advent2024.util.data.Position;
import ch.neukom.advent2024.util.inputreaders.InputMapReader;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static ch.neukom.advent2024.day12.Util.Region;
import static ch.neukom.advent2024.day12.Util.findPlantRegions;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputMapReader reader = new InputMapReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputMapReader reader) {
        Map<Position, Character> gardenMap = reader.readIntoMap();
        Set<Region> regions = findPlantRegions(gardenMap);
        long totalPrice = regions.stream()
            .mapToLong(region -> region.calculateFenceSidePrice(gardenMap))
            .sum();
        System.out.printf("The total price of fences is %s", totalPrice);
    }
}
