package ch.neukom.advent2024.day8;

import ch.neukom.advent2024.util.data.Position;
import ch.neukom.advent2024.util.inputreaders.InputMapReader;

import java.io.IOException;
import java.util.Set;

import static ch.neukom.advent2024.day8.Util.findAntinodes;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputMapReader reader = new InputMapReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputMapReader reader) {
        Set<Position> antinodePositions = findAntinodes(reader, Util::collectRepeatedAntinodes);
        System.out.printf("There are %s unique antinode positions", antinodePositions.size());
    }
}
