package ch.neukom.advent2024.day15;

import ch.neukom.advent2024.util.characterMap.CharacterMapUtil;
import ch.neukom.advent2024.util.data.Direction;
import ch.neukom.advent2024.util.data.Position;
import ch.neukom.advent2024.util.inputreaders.InputResourceReader;
import ch.neukom.advent2024.util.splitter.Splitters;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

public class Util {
    private Util() {
    }

    public static Warehouse readWarehouse(InputResourceReader reader) {
        String mapData = Splitters.PARAGRAPH_SPLITTER.splitToList(reader.readInput().collect(joining("\n"))).getFirst();
        List<String> lines = Splitters.NEWLINE_SPLITTER.splitToList(mapData);
        int height = lines.size();
        int width = lines.getFirst().length();
        Map<Position, WarehouseElement> map = CharacterMapUtil.buildCharacterMap(
            lines,
            (position, character) -> switch (character) {
                case '#' -> new Wall();
                case '@' -> new Robot();
                case 'O' -> new Box(null);
                case '.' -> new Empty();
                default -> throw new IllegalArgumentException();
            }
        );
        return new Warehouse(map, height, width);
    }

    public static Warehouse readScaledWarehouse(InputResourceReader reader) {
        String mapData = Splitters.PARAGRAPH_SPLITTER.splitToList(reader.readInput().collect(joining("\n"))).getFirst();
        List<String> lines = Splitters.NEWLINE_SPLITTER.splitToStream(mapData)
            .map(String::chars)
            .map(chars -> chars.mapToObj(c -> switch (c) {
                case '#' -> "##";
                case '@' -> "@.";
                case 'O' -> "[]";
                case '.' -> "..";
                default -> throw new IllegalArgumentException();
            }))
            .map(chars -> chars.collect(joining()))
            .toList();
        int height = lines.size();
        int width = lines.getFirst().length();
        Map<Position, WarehouseElement> map = CharacterMapUtil.buildCharacterMap(
            lines,
            (position, character) -> switch (character) {
                case '#' -> new Wall();
                case '@' -> new Robot();
                case '[' -> new Box(Direction.EAST);
                case ']' -> new Box(Direction.WEST);
                case '.' -> new Empty();
                default -> throw new IllegalArgumentException();
            }
        );
        return new Warehouse(map, height, width);
    }

    public static List<Direction> readMovements(InputResourceReader reader) {
        String movementData = Splitters.PARAGRAPH_SPLITTER.splitToList(reader.readInput().collect(joining("\n"))).getLast();
        return movementData.chars()
            .mapToObj(c -> switch (c) {
                case '^' -> Direction.NORTH;
                case '>' -> Direction.EAST;
                case 'v' -> Direction.SOUTH;
                case '<' -> Direction.WEST;
                case '\n' -> null;
                default -> throw new IllegalArgumentException();
            })
            .filter(Objects::nonNull)
            .toList();
    }

    public record Warehouse(Map<Position, WarehouseElement> map, int height, int width) {
        public void print() {
            IntStream.range(0, height).forEach(y -> {
                String line = IntStream.range(0, width)
                    .mapToObj(x -> new Position(x, y))
                    .map(map::get)
                    .map(element -> switch (element) {
                        case Box box -> box.otherPart() == null ? "O" : box.otherPart() == Direction.EAST ? "[" : "]";
                        case Empty empty -> ".";
                        case Robot robot -> "@";
                        case Wall wall -> "#";
                    })
                    .collect(joining());
                System.out.println(line);
            });
            System.out.println();
        }

        public long calculateGpsSum() {
            return IntStream.range(0, height)
                .mapToObj(y -> IntStream.range(0, width).mapToObj(x -> new Position(x, y)))
                .flatMap(Function.identity())
                .filter(position -> map.get(position) instanceof Box box && box.otherPart() == Direction.EAST)
                .mapToLong(position -> position.x() + position.y() * 100)
                .sum();
        }
    }

    public sealed interface WarehouseElement permits Robot, Box, Wall, Empty {
    }

    public record Robot() implements WarehouseElement {
    }

    public record Box(Direction otherPart) implements WarehouseElement {
    }

    public record Wall() implements WarehouseElement {
    }

    public record Empty() implements WarehouseElement {
    }
}
