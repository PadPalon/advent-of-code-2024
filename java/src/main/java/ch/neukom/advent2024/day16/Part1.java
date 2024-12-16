package ch.neukom.advent2024.day16;

import ch.neukom.advent2024.util.data.Direction;
import ch.neukom.advent2024.util.data.Position;
import ch.neukom.advent2024.util.inputreaders.InputMapReader;

import java.io.IOException;
import java.util.Map;

import static ch.neukom.advent2024.day16.Util.*;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputMapReader reader = new InputMapReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputMapReader reader) {
        Map<Position, MazeElement> maze = readMaze(reader);
        Start start = maze.values().stream().filter(Start.class::isInstance).map(Start.class::cast).findAny().orElseThrow();

        Map<MazeElement, Move> scores = calculateRoutes(s -> s.put(start, new Move(Direction.EAST, 0)), maze);
        Integer endScore = maze.values()
            .stream()
            .filter(End.class::isInstance)
            .map(End.class::cast)
            .findAny()
            .map(scores::get)
            .map(Move::score)
            .orElseThrow();
        System.out.printf("The lowest possible score is %s", endScore);
    }
}
