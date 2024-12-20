package ch.neukom.advent2024.day20;

import ch.neukom.advent2024.util.data.Direction;
import ch.neukom.advent2024.util.data.Position;
import ch.neukom.advent2024.util.inputreaders.InputMapReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static ch.neukom.advent2024.day20.Util.*;

public class Part1 {
    public static final int EXPECTED_TIME_SAVE = 100;

    public static void main(String[] args) throws IOException {
        try (InputMapReader reader = new InputMapReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputMapReader reader) {
        Map<Position, MazeElement> maze = readMaze(reader);

        Start start = maze.values().stream().filter(Start.class::isInstance).map(Start.class::cast).findAny().orElseThrow();
        End end = maze.values().stream().filter(End.class::isInstance).map(End.class::cast).findAny().orElseThrow();

        Map<MazeElement, Move> scores = calculateRoutes(s -> s.put(start, new Move(0, List.of())), maze);

        long cheatCount = scores.get(end)
            .predecessors()
            .stream()
            .mapToLong(p -> countCheatShortcuts(p, scores, maze))
            .sum();
        System.out.printf("There are %s cheats that save at least %s picoseconds", cheatCount, EXPECTED_TIME_SAVE);
    }

    private static long countCheatShortcuts(Position currentPosition,
                                            Map<MazeElement, Move> scores,
                                            Map<Position, MazeElement> maze) {
        Integer currentScore = scores.get(maze.get(currentPosition)).score();
        return Arrays.stream(Direction.values())
            .map(currentPosition::move)
            .filter(neighbour -> maze.get(neighbour) instanceof Wall)
            .flatMap(neighbour -> getEmptyNeighbours(neighbour, currentPosition, maze))
            .map(scores::get)
            .map(Move::score)
            .filter(cheatScore -> cheatScore > currentScore + EXPECTED_TIME_SAVE)
            .count();
    }

    private static Stream<MazeElement> getEmptyNeighbours(Position currentPosition,
                                                          Position excludedPosition,
                                                          Map<Position, MazeElement> maze) {
        return Arrays.stream(Direction.values())
            .map(currentPosition::move)
            .filter(neighbour -> neighbour != excludedPosition)
            .map(maze::get)
            .filter(Objects::nonNull)
            .filter(neighbour -> !neighbour.isBlocking());
    }
}
