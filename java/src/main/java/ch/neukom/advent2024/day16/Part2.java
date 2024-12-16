package ch.neukom.advent2024.day16;

import ch.neukom.advent2024.util.data.Direction;
import ch.neukom.advent2024.util.data.Position;
import ch.neukom.advent2024.util.inputreaders.InputMapReader;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.util.Arrays;
import java.util.Deque;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static ch.neukom.advent2024.day16.Util.*;
import static java.util.stream.Collectors.toMap;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputMapReader reader = new InputMapReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputMapReader reader) {
        Map<Position, MazeElement> maze = readMaze(reader);
        Start start = maze.values().stream().filter(Start.class::isInstance).map(Start.class::cast).findAny().orElseThrow();
        End end = maze.values().stream().filter(End.class::isInstance).map(End.class::cast).findAny().orElseThrow();

        Map<MazeElement, Move> startScores = calculateRoutes(s -> s.put(start, new Move(Direction.EAST, 0)), maze);
        Map<Direction, Map<MazeElement, Move>> endScores = Arrays.stream(Direction.values())
            .map(direction -> calculateRoutes(scores -> scores.put(end, new Move(direction, 0)), maze))
            .collect(toMap(scores -> scores.get(end).direction(), Function.identity()));

        Integer shortestScore = maze.values()
            .stream()
            .filter(End.class::isInstance)
            .map(End.class::cast)
            .findAny()
            .map(startScores::get)
            .map(Move::score)
            .orElseThrow();

        /*
         I have no clue if this generalizes to all possible mazes and paths, but I've spent way too long on this day
         to do more
        */
        Deque<MazeElement> toHandle = Queues.newArrayDeque();
        Set<MazeElement> handled = Sets.newHashSet();
        toHandle.push(start);
        toHandle.push(end);
        Set<MazeElement> partOfBest = Sets.newHashSet();
        while (!toHandle.isEmpty()) {
            MazeElement current = toHandle.pop();
            handled.add(current);
            if (!startScores.containsKey(current)) {
                continue;
            }

            Integer startScore = startScores.get(current).score();
            boolean isOnShortestPath = endScores.values()
                .stream()
                .filter(endScore -> endScore.containsKey(current))
                .map(endScore -> endScore.get(current))
                .map(Move::score)
                .anyMatch(endScore -> startScore + endScore == shortestScore);

            if (isOnShortestPath) {
                partOfBest.add(current);
                Position currentPosition = current.position();
                Arrays.stream(Direction.values())
                    .map(currentPosition::move)
                    .map(maze::get)
                    .filter(neighbour -> !handled.contains(neighbour))
                    .forEach(toHandle::addLast);
            }
        }
        System.out.printf("%s tiles are part of the shortest paths", partOfBest.size());
    }
}
