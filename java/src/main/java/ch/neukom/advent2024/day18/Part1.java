package ch.neukom.advent2024.day18;

import ch.neukom.advent2024.util.data.Position;
import ch.neukom.advent2024.util.inputreaders.InputResourceReader;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static ch.neukom.advent2024.day18.Util.*;

public class Part1 {

    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        Map<Position, MazeElement> maze = readMaze(reader, DEFAULT_FALL_LIMIT);

        Start start = maze.values().stream().filter(Start.class::isInstance).map(Start.class::cast).findAny().orElseThrow();

        Map<MazeElement, Move> scores = calculateRoutes(s -> s.put(start, new Move(0, Set.of())), maze);
        Integer endScore = maze.values()
            .stream()
            .filter(End.class::isInstance)
            .map(End.class::cast)
            .findAny()
            .map(scores::get)
            .map(Move::score)
            .orElseThrow();

        System.out.printf("%s steps are required to exit the memory space%n", endScore);
    }
}
