package ch.neukom.advent2024.day6;

import ch.neukom.advent2024.util.InputArrayReader;

import java.io.IOException;
import java.util.Arrays;

import static ch.neukom.advent2024.day6.Util.getFinishedPatrol;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputArrayReader reader = new InputArrayReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputArrayReader reader) {
        Boolean[][] facilityMap = reader.readIntoArray(symbol -> symbol.symbol() == '#', Boolean.class);

        Boolean[][] patrolMap = getFinishedPatrol(reader, facilityMap);

        long visitedPositions = Arrays.stream(patrolMap)
            .flatMap(Arrays::stream)
            .filter(a -> a)
            .count();
        System.out.printf("The guard visited %s positions", visitedPositions);
    }
}
