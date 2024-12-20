package ch.neukom.advent2024.day20;

import ch.neukom.advent2024.util.data.Position;
import ch.neukom.advent2024.util.inputreaders.InputMapReader;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static ch.neukom.advent2024.day20.Util.*;

/**
 * this can actually also solve part 1
 */
public class Part2 {
    public static final int MAX_CHEAT_DISTANCE = 220;
    public static final int EXPECTED_TIME_SAVE = 100;

    public static void main(String[] args) throws IOException {
        try (InputMapReader reader = new InputMapReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputMapReader reader) {
        Map<Position, MazeElement> maze = readMaze(reader);

        Start start = maze.values().stream().filter(Start.class::isInstance).map(Start.class::cast).findAny().orElseThrow();
        End end = maze.values().stream().filter(End.class::isInstance).map(End.class::cast).findAny().orElseThrow();

        Map<MazeElement, Move> startScores = calculateRoutes(s -> s.put(start, new Move(0, List.of())), maze);
        Map<MazeElement, Move> endScores = calculateRoutes(s -> s.put(end, new Move(0, List.of())), maze);

        Integer scoreToBeat = startScores.get(end).score();
        List<Position> optimalPath = startScores.get(end).predecessors();
        List<List<Position>> combinations = Lists.newArrayList();
        for (int i = 0; i < optimalPath.size(); i++) {
            Position firstPosition = optimalPath.get(i);
            for (int j = i + 1; j < optimalPath.size(); j++) {
                Position secondPosition = optimalPath.get(j);
                combinations.add(List.of(firstPosition, secondPosition));
            }
            combinations.add(List.of(firstPosition, end.position()));
        }
        long cheatCount = combinations
            .stream()
            .filter(positions -> getManhattanDistance(positions) <= MAX_CHEAT_DISTANCE)
            .filter(positions -> getCheatDistance(positions, maze, startScores, endScores) + EXPECTED_TIME_SAVE <= scoreToBeat)
            .count();

        System.out.printf("There are %s cheats that save at least %s picoseconds", cheatCount, EXPECTED_TIME_SAVE);
    }

    private static long getManhattanDistance(List<Position> positions) {
        Position firstPosition = positions.getFirst();
        Position secondPosition = positions.getLast();
        long xDistance = Math.abs(firstPosition.x() - secondPosition.x());
        long yDistance = Math.abs(firstPosition.y() - secondPosition.y());
        return xDistance + yDistance;
    }

    private static long getCheatDistance(List<Position> positions,
                                         Map<Position, MazeElement> maze,
                                         Map<MazeElement, Move> startScores,
                                         Map<MazeElement, Move> endScores) {
        Position firstPosition = positions.getFirst();
        MazeElement firstElement = maze.get(firstPosition);
        Integer firstScore = startScores.get(firstElement).score();
        Position secondPosition = positions.getLast();
        MazeElement secondElement = maze.get(secondPosition);
        Integer secondScore = endScores.get(secondElement).score();

        long manhattanDistance = getManhattanDistance(positions);
        return firstScore + manhattanDistance + secondScore;
    }
}
