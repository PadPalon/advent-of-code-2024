package ch.neukom.advent2024.util.characterMap;

import ch.neukom.advent2024.util.data.Position;
import ch.neukom.advent2024.util.inputreaders.InputResourceReader;
import com.google.common.collect.Maps;
import com.google.common.collect.Streams;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class CharacterMapUtil {
    private CharacterMapUtil() {
    }

    public static Stream<List<Position>> getLinePositions(int width, int height, LineDirection... lineDirections) {
        Stream.Builder<List<Position>> builder = Stream.builder();
        List<LineDirection> directionList = Arrays.asList(lineDirections);

        if (directionList.contains(LineDirection.VERTICAL)) {
            buildCardinalLines(width, height, (y) -> (x) -> new Position(x, y)).forEach(builder);
        }

        if (directionList.contains(LineDirection.HORIZONTAL)) {
            buildCardinalLines(width, height, (x) -> (y) -> new Position(x, y)).forEach(builder);
        }

        if (directionList.contains(LineDirection.DIAGONAL)) {
            buildDiagonalLines(width, height).forEach(builder);
        }

        return builder.build();
    }

    private static Stream<List<Position>> buildCardinalLines(int width,
                                                             int height,
                                                             Function<Integer, IntFunction<Position>> positionIntFunction) {
        return IntStream.range(0, width)
            .boxed()
            .map(x -> IntStream.range(0, height).mapToObj(positionIntFunction.apply(x)).toList());
    }

    private static Stream<List<Position>> buildDiagonalLines(int width, int height) {
        List<Position> topLeftBottomRightDiagonal = Streams.zip(
            LongStream.range(0, width).boxed(),
            LongStream.range(0, height).boxed(),
            Position::new
        ).toList();

        List<Position> bottomLeftTopRightDiagonal = Streams.zip(
            IntStream.range(0, width).boxed(),
            IntStream.range(0, height).boxed(),
            (x, y) -> new Position(x, height - 1 - y)
        ).toList();

        return Stream.concat(
            buildOffsetDiagonals(height, topLeftBottomRightDiagonal),
            buildOffsetDiagonals(height, bottomLeftTopRightDiagonal)
        );
    }

    private static Stream<List<Position>> buildOffsetDiagonals(int height, List<Position> sourceDiagonal) {
        return IntStream.range(-height + 1, height)
            .boxed()
            .map(offset -> sourceDiagonal.stream()
                .map(position -> new Position(position.x() + offset, position.y()))
                .toList()
            );
    }

    public static Map<Position, Character> buildCharacterMap(InputResourceReader reader) {
        return buildCharacterMap(reader, character -> character);
    }

    public static <T> Map<Position, T> buildCharacterMap(InputResourceReader reader,
                                                         Function<Character, T> transformer) {
        return buildCharacterMap(reader, ((position, character) -> transformer.apply(character)));
    }

    public static <T> Map<Position, T> buildCharacterMap(InputResourceReader reader,
                                                         BiFunction<Position, Character, T> transformer) {
        List<String> lines = reader.readInput().toList();
        return buildCharacterMap(lines, transformer);
    }

    public static <T> Map<Position, T> buildCharacterMap(List<String> lines,
                                                         BiFunction<Position, Character, T> transformer) {
        int height = lines.size();
        int width = lines.getFirst().length();
        Map<Position, T> characterMap = Maps.newHashMap();
        for (int y = 0; y < height; y++) {
            String line = lines.get(y);
            for (int x = 0; x < width; x++) {
                Character character = line.charAt(x);
                Position position = new Position(x, y);
                characterMap.put(position, transformer.apply(position, character));
            }
        }
        return characterMap;
    }

    public enum LineDirection {
        VERTICAL,
        HORIZONTAL,
        DIAGONAL
    }
}
