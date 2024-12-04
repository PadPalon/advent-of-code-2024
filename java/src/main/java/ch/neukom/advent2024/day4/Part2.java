package ch.neukom.advent2024.day4;

import ch.neukom.advent2024.util.InputResourceReader;
import ch.neukom.advent2024.util.data.Position;
import com.google.common.collect.HashMultiset;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static ch.neukom.advent2024.util.characterMap.CharacterMapUtil.LineDirection.DIAGONAL;
import static ch.neukom.advent2024.util.characterMap.CharacterMapUtil.buildCharacterMap;
import static ch.neukom.advent2024.util.characterMap.CharacterMapUtil.getLinePositions;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toCollection;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        int height = (int) reader.getLineCount();
        int width = reader.getFirstLine().length();

        Map<Position, Character> characterMap = buildCharacterMap(reader);

        long xmasCount = getLinePositions(width, height, DIAGONAL)
            .map(
                positions -> positions.stream()
                    .map(position -> new PositionCharacter(position, characterMap.get(position)))
                    .filter(positionCharacter -> positionCharacter.character() != null)
                    .toList()
            )
            .filter(positionCharacters -> positionCharacters.size() >= 3)
            .flatMap(positionCharacters -> Stream.of(positionCharacters, positionCharacters.reversed()))
            .mapMulti(Part2::getCrossCenters)
            .collect(toCollection(HashMultiset::create))
            .entrySet()
            .stream()
            .filter(entry -> entry.getCount() == 2)
            .count();
        System.out.printf("The puzzles contains %s X-MAS strings", xmasCount);
    }

    private static void getCrossCenters(List<PositionCharacter> positionCharacters, Consumer<PositionCharacter> consumer) {
        String string = positionCharacters.stream().map(PositionCharacter::character).map(String::valueOf).collect(joining());
        int currentIndex = 0;
        while (currentIndex >= 0) {
            currentIndex = string.indexOf("MAS", currentIndex);
            if (currentIndex >= 0) {
                consumer.accept(positionCharacters.get(currentIndex + 1));
                currentIndex++;
            } else {
                return;
            }
        }
    }

    private record PositionCharacter(Position position, @Nullable Character character) {
    }
}
