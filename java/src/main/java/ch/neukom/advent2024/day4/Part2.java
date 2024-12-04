package ch.neukom.advent2024.day4;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import ch.neukom.advent2024.util.InputResourceReader;
import com.google.common.collect.Maps;
import com.google.common.collect.Streams;

import javax.annotation.Nullable;

import static java.util.stream.Collectors.*;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class, "test")) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        Map<Position, Character> characterMap = Maps.newHashMap();
        List<String> lines = reader.readInput().toList();
        int height = lines.size();
        int width = lines.getFirst().length();
        for (int y = 0; y < height; y++) {
            String line = lines.get(y);
            for (int x = 0; x < width; x++) {
                Character character = line.charAt(x);
                characterMap.put(new Position(x, y), character);
            }
        }

        List<Position> topLeftBottomRightDiagonalRoot = Streams.zip(
            IntStream.range(0, width).boxed(),
            IntStream.range(0, height).boxed(),
            Position::new
        ).toList();
        Stream<List<Position>> topLeftBottomRightDiagonalPositions = IntStream.range(-height, height)
            .boxed()
            .map(offset -> topLeftBottomRightDiagonalRoot.stream()
                .map(position -> new Position(position.x() + offset, position.y()))
                .toList()
            );

        List<Position> bottomLeftTopRightDiagonalRoot = Streams.zip(
            IntStream.range(0, width).boxed(),
            IntStream.range(0, height).boxed(),
            (x, y) -> new Position(x, height - 1 - y)
        ).toList();
        Stream<List<Position>> bottomLeftTopRightDiagonalPositions = IntStream.range(-height, height)
            .boxed()
            .map(offset -> bottomLeftTopRightDiagonalRoot.stream()
                .map(position -> new Position(position.x() + offset, position.y()))
                .toList()
            );

        long xmasCount = Streams.concat(
                topLeftBottomRightDiagonalPositions,
                bottomLeftTopRightDiagonalPositions
            ).map(
                positions -> positions.stream()
                    .map(position -> new PositionCharacter(position, characterMap.get(position)))
                    .filter(positionCharacter -> positionCharacter.character() != null)
                    .toList()
            )
            .filter(positionCharacters -> positionCharacters.size() >= 3)
            .peek(p -> System.out.println(p))
            .flatMap(positionCharacters -> Stream.of(positionCharacters, positionCharacters.reversed()))
            .mapMulti(Part2::getMASIndexes)
            .map(Part2::getCrossPosition)
            .map(crossPositions -> buildPositionString(crossPositions, characterMap))
            .filter(string -> string.equals("MAS") || string.equals("SAM"))
            .count();
        System.out.printf("The puzzles contains %s X-MAS strings", xmasCount);
    }

    private static String buildPositionString(List<Position> positions, Map<Position, Character> characterMap) {
        return positions.stream()
            .map(characterMap::get)
            .filter(Objects::nonNull)
            .map(String::valueOf)
            .collect(Collectors.joining());
    }

    private static void getMASIndexes(List<PositionCharacter> positionCharacters, Consumer<List<PositionCharacter>> consumer) {
        String string = positionCharacters.stream().map(PositionCharacter::character).map(String::valueOf).collect(joining());
        int currentIndex = 0;
        while (currentIndex >= 0) {
            currentIndex = string.indexOf("MAS", currentIndex);
            if (currentIndex >= 0) {
                consumer.accept(List.of(
                    positionCharacters.get(currentIndex),
                    positionCharacters.get(currentIndex + 1),
                    positionCharacters.get(currentIndex + 2)
                ));
                currentIndex++;
            } else {
                return;
            }
        }
    }

    private static List<Position> getCrossPosition(List<PositionCharacter> positionCharacters) {
        Position left = positionCharacters.stream().map(PositionCharacter::position).min(Comparator.comparing(Position::x)).orElseThrow();
        Position right = positionCharacters.stream().map(PositionCharacter::position).max(Comparator.comparing(Position::x)).orElseThrow();
        Position middle = positionCharacters.stream().map(PositionCharacter::position).filter(p -> p != left && p != right).findAny().orElseThrow();
        return List.of(new Position(left.x(), right.y()), middle, new Position(right.x(), left.y()));
    }

    private record Position(int x, int y) {
    }

    private record PositionCharacter(Position position, @Nullable Character character) {
    }
}
