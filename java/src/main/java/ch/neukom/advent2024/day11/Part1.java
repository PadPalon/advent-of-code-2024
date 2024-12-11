package ch.neukom.advent2024.day11;

import ch.neukom.advent2024.util.inputreaders.InputResourceReader;
import ch.neukom.advent2024.util.splitter.Splitters;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;

public class Part1 {
    private static final int MAX_TURNS = 25;

    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        List<Long> currentArrangement = Splitters.WHITESPACE_SPLITTER.splitToStream(reader.getFirstLine())
            .map(Long::parseLong)
            .toList();
        for (int i = 0; i < MAX_TURNS; i++) {
            currentArrangement = blink(currentArrangement);
        }
        System.out.printf("There are %s stones after %s blinks", currentArrangement.size(), MAX_TURNS);
    }

    private static List<Long> blink(List<Long> currentArrangement) {
        List<Long> newArrangement = Lists.newArrayList();
        for (Long stone : currentArrangement) {
            String stoneString = String.valueOf(stone);
            if (stone == 0) {
                newArrangement.add(1L);
            } else if (stoneString.length() % 2 == 0) {
                newArrangement.add(Long.parseLong(stoneString.substring(0, stoneString.length() / 2)));
                newArrangement.add(Long.parseLong(stoneString.substring(stoneString.length() / 2)));
            } else {
                newArrangement.add(stone * 2024);
            }
        }
        return newArrangement;
    }
}
