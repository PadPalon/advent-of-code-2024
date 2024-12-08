package ch.neukom.advent2024.day8;

import ch.neukom.advent2024.util.data.Position;
import ch.neukom.advent2024.util.inputreaders.InputMapReader;

import java.io.IOException;
import java.util.Set;

import static ch.neukom.advent2024.day8.Util.findAntinodes;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputMapReader reader = new InputMapReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputMapReader reader) {
        Set<Position> antinodePositions = findAntinodes(reader, Util::collectAntinode);
        System.out.printf("There are %s unique antinode positions", antinodePositions.size());
    }
}
