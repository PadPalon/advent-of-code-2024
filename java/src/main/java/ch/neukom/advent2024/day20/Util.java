package ch.neukom.advent2024.day20;

import ch.neukom.advent2024.util.data.Direction;
import ch.neukom.advent2024.util.data.Position;
import ch.neukom.advent2024.util.inputreaders.InputMapReader;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.*;
import java.util.function.Consumer;

import static java.util.Comparator.comparing;

public class Util {
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
                    List<Position> path = Lists.newArrayList();
                    path.addAll(currentMove.predecessors());
                    path.add(currentPosition);
                    return new Move(newScore, path);
                } else {
                    return move;
                }
            });
            unvisited.offer(nextElement);
        }
    }

    static Map<Position, MazeElement> readMaze(InputMapReader reader) {
        return reader.readIntoMap((position, character) -> switch (character) {
            case 'E' -> new End(position);
            case '#' -> new Wall(position);
            case '.' -> new Empty(position);
            case 'S' -> new Start(position);
            default -> throw new IllegalArgumentException();
        });
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

    public record Move(Integer score, List<Position> predecessors) {
    }
}
