package ch.neukom.advent2024.day4;

import ch.neukom.advent2024.util.InputResourceReader;
import ch.neukom.advent2024.util.data.Position;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ch.neukom.advent2024.util.characterMap.CharacterMapUtil.LineDirection.*;
import static ch.neukom.advent2024.util.characterMap.CharacterMapUtil.buildCharacterMap;
import static ch.neukom.advent2024.util.characterMap.CharacterMapUtil.getLinePositions;

public class Part1 {
    public static final String SEARCH_STRING = "XMAS";

    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        int height = (int) reader.getLineCount();
        int width = reader.getFirstLine().length();

        Map<Position, Character> characterMap = buildCharacterMap(reader);

        long xmasCount = getLinePositions(width, height, VERTICAL, HORIZONTAL, DIAGONAL)
            .map(positions -> buildPositionString(positions, characterMap))
            .filter(string -> string.length() >= 3)
            .flatMap(string -> Stream.of(string, new StringBuilder(string).reverse().toString()))
            .filter(string -> string.contains(SEARCH_STRING))
            .mapMulti(Part1::getSearchIndexes)
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

    private static void getSearchIndexes(String string, Consumer<Integer> consumer) {
        int currentIndex = 0;
        while (currentIndex >= 0) {
            currentIndex = string.indexOf(SEARCH_STRING, currentIndex);
            if (currentIndex >= 0) {
                consumer.accept(currentIndex);
                currentIndex++;
            } else {
                return;
            }
        }
    }
}
