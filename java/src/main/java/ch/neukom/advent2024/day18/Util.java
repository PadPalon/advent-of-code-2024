package ch.neukom.advent2024.day18;

import ch.neukom.advent2024.util.data.Direction;
import ch.neukom.advent2024.util.data.Position;
import ch.neukom.advent2024.util.inputreaders.InputResourceReader;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class Util {
    public static final int SIZE = 71;
    public static final int DEFAULT_FALL_LIMIT = 1024;

    private Util() {
    }

    public static Map<MazeElement, Move> calculateRoutes(Consumer<Map<MazeElement, Move>> scoreInitializer, Map<Position, MazeElement> maze) {
        // TODO this is basically a copy from day 16, maybe generalize and create move to general utils?
        Map<MazeElement, Move> scores = Maps.newHashMap();
        scoreInitializer.accept(scores);
        Queue<MazeElement> unvisited = new PriorityQueue<>(comparing(key -> Optional.ofNullable(scores.get(key)).map(Move::score).orElse(Integer.MAX_VALUE)));
        unvisited.addAll(maze.values());

        MazeElement currentElement = unvisited.poll();
        while (currentElement != null && scores.containsKey(currentElement)) {
            Move currentMove = scores.get(currentElement);
            Position currentPosition = currentElement.position();
            Arrays.stream(Direction.values())
                .forEach(direction -> calculateMove(currentPosition, direction, currentMove, maze, scores, unvisited));
            currentElement = unvisited.poll();
        }

        return scores;
    }

    private static void calculateMove(Position currentPosition,
                                      Direction direction,
                                      Move currentMove,
                                      Map<Position, MazeElement> maze,
                                      Map<MazeElement, Move> scores,
                                      Queue<MazeElement> unvisited) {
        Position nextPosition = currentPosition.move(direction);
        MazeElement nextElement = maze.get(nextPosition);
        if (nextElement != null && !nextElement.isBlocking() && unvisited.contains(nextElement)) {
            unvisited.remove(nextElement);
            scores.compute(nextElement, (element, move) -> {
                int newScore = currentMove.score() + 1;
                if (move == null || move.score() > newScore) {
                    return new Move(newScore, Sets.union(currentMove.predecessors(), Set.of(currentPosition)));
                } else {
                    return move;
                }
            });
            unvisited.offer(nextElement);
        }
    }

    static Map<Position, MazeElement> readMaze(InputResourceReader reader, int fallLimit) {
        Set<Position> blockedPositions = reader.readInput()
            .limit(fallLimit)
            .map(falling -> falling.split(","))
            .map(falling -> new Position(Integer.parseInt(falling[0]), Integer.parseInt(falling[1])))
            .collect(toSet());
        return IntStream.range(0, SIZE)
            .mapToObj(x -> IntStream.range(0, SIZE).mapToObj(y -> new Position(x, y)))
            .flatMap(Function.identity())
            .collect(toMap(position -> position, position -> {
                if (blockedPositions.contains(position)) {
                    return new Wall(position);
                } else if (position.x() == 0 && position.y() == 0) {
                    return new Start(position);
                } else if (position.x() == SIZE - 1 && position.y() == SIZE - 1) {
                    return new End(position);
                } else {
                    return new Empty(position);
                }
            }));
    }

    static void printMaze(Map<Position, MazeElement> maze) {
        IntStream.range(0, SIZE)
            .forEach(y -> {
                IntStream.range(0, SIZE).forEach(x -> {
                    Position position = new Position(x, y);
                    MazeElement mazeElement = maze.get(position);
                    switch (mazeElement) {
                        case Empty empty -> System.out.print(".");
                        case End end -> System.out.print("E");
                        case Start s -> System.out.print("S");
                        case Wall wall -> System.out.print("#");
                    }
                });
                System.out.println();
            });
        System.out.println();
    }

    public sealed interface MazeElement permits Wall, Start, Empty, End {
        Position position();

        boolean isBlocking();
    }

    public record Wall(Position position) implements MazeElement {
        @Override
        public boolean isBlocking() {
            return true;
        }
    }

    public record Start(Position position) implements MazeElement {
        @Override
        public boolean isBlocking() {
            return false;
        }
    }

    public record End(Position position) implements MazeElement {
        @Override
        public boolean isBlocking() {
            return false;
        }
    }

    public record Empty(Position position) implements MazeElement {
        @Override
        public boolean isBlocking() {
            return false;
        }
    }

    public record Move(Integer score, Set<Position> predecessors) {
    }
}
