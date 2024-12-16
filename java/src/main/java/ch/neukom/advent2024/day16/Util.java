package ch.neukom.advent2024.day16;

import ch.neukom.advent2024.util.data.Direction;
import ch.neukom.advent2024.util.data.Position;
import ch.neukom.advent2024.util.inputreaders.InputMapReader;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.Comparator.comparing;

public class Util {
    private Util() {
    }

    public static Map<Position, MazeElement> readMaze(InputMapReader reader) {
        return reader.readIntoMap((position, character) -> switch (character) {
            case 'E' -> new End(position);
            case '#' -> new Wall(position);
            case '.' -> new Empty(position);
            case 'S' -> new Start(position);
            default -> throw new IllegalArgumentException();
        });
    }

    public static Map<MazeElement, Move> calculateRoutes(Consumer<Map<MazeElement, Move>> scoreInitializer, Map<Position, MazeElement> maze) {
        Map<MazeElement, Move> scores = Maps.newHashMap();
        scoreInitializer.accept(scores);
        Queue<MazeElement> unvisited = new PriorityQueue<>(comparing(key -> Optional.ofNullable(scores.get(key)).map(Move::score).orElse(Integer.MAX_VALUE)));
        unvisited.addAll(maze.values());

        MazeElement currentElement = unvisited.poll();
        while (currentElement != null && scores.containsKey(currentElement)) {
            Move currentMove = scores.get(currentElement);
            Position currentPosition = currentElement.position();
            calculateMove(currentPosition, currentMove::direction, () -> currentMove.score() + 1, maze, scores, unvisited);
            calculateMove(currentPosition, () -> currentMove.direction().turnLeft(), () -> currentMove.score() + 1001, maze, scores, unvisited);
            calculateMove(currentPosition, () -> currentMove.direction().turnRight(), () -> currentMove.score() + 1001, maze, scores, unvisited);
            currentElement = unvisited.poll();
        }

        return scores;
    }

    private static void calculateMove(Position currentPosition,
                                      Supplier<Direction> directionSupplier,
                                      Supplier<Integer> scoreSupplier,
                                      Map<Position, MazeElement> maze,
                                      Map<MazeElement, Move> scores,
                                      Queue<MazeElement> unvisited) {
        Direction direction = directionSupplier.get();
        Position nextMove = currentPosition.move(direction);
        MazeElement nextElement = maze.get(nextMove);
        if (nextElement != null && !nextElement.isBlocking() && unvisited.contains(nextElement)) {
            unvisited.remove(nextElement);
            scores.compute(nextElement, (element, move) -> {
                int newScore = scoreSupplier.get();
                if (move == null || move.score() > newScore) {
                    return new Move(direction, newScore);
                } else if (move.score() == newScore) {
                    return new Move(move.direction(), move.score());
                } else {
                    return move;
                }
            });
            unvisited.offer(nextElement);
        }
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

    public record Move(Direction direction, Integer score) {
    }
}
