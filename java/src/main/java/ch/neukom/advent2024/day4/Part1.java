package ch.neukom.advent2024.day4;

import java.io.IOException;
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

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
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

        Stream<List<Position>> horizontalPositions = IntStream.range(0, height)
            .boxed()
            .map(x -> IntStream.range(0, width).mapToObj(y -> new Position(x, y)).toList());
        Stream<List<Position>> verticalPositions = IntStream.range(0, width)
            .boxed()
            .map(y -> IntStream.range(0, height).mapToObj(x -> new Position(x, y)).toList());

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
                horizontalPositions,
                verticalPositions,
                topLeftBottomRightDiagonalPositions,
                bottomLeftTopRightDiagonalPositions
            ).map(
                positions -> buildPositionString(positions, characterMap)
            )
            .filter(string -> string.length() >= 3)
            .flatMap(string -> Stream.of(string, new StringBuilder(string).reverse().toString()))
            .filter(string -> string.contains("XMAS"))
            .mapMulti(Part1::getXMASIndexes)
            .count();
        System.out.printf("The puzzles contains %s XMAS strings", xmasCount);
    }

    private static String buildPositionString(List<Position> positions, Map<Position, Character> characterMap) {
        return positions.stream()
            .map(characterMap::get)
            .filter(Objects::nonNull)
            .map(String::valueOf)
            .collect(Collectors.joining());
    }

    private static void getXMASIndexes(String string, Consumer<Integer> consumer) {
        int currentIndex = 0;
        while (currentIndex >= 0) {
            currentIndex = string.indexOf("XMAS", currentIndex);
            if (currentIndex >= 0) {
                consumer.accept(currentIndex);
                currentIndex++;
            } else {
                return;
            }
        }
    }

    private record Position(int x, int y) {
    }
}
