package ch.neukom.advent2024.day25;

import ch.neukom.advent2024.util.splitter.Splitters;
import com.google.common.collect.Streams;

import java.util.Arrays;
import java.util.List;

public class Util {
    private Util() {
    }

    public static Security parseSecurity(String schematic) {
        List<String> schematicLines = Splitters.NEWLINE_SPLITTER.splitToList(schematic);
        if (schematicLines.getFirst().contains(".")) {
            return parseKey(schematicLines);
        } else {
            return parseLock(schematicLines);
        }
    }

    private static Key parseKey(List<String> schematicLines) {
        Integer[] heights = getHeights(schematicLines);
        return new Key(Arrays.asList(heights));
    }

    private static Lock parseLock(List<String> schematicLines) {
        Integer[] heights = getHeights(schematicLines);
        return new Lock(Arrays.asList(heights));
    }

    private static Integer[] getHeights(List<String> schematicLines) {
        Integer[] heights = new Integer[]{-1, -1, -1, -1, -1};
        for (String schematicLine : schematicLines) {
            int index = 0;
            for (char c : schematicLine.toCharArray()) {
                if (c == '#') {
                    heights[index]++;
                }
                index++;
            }
        }
        return heights;
    }

    public sealed interface Security permits Lock, Key {
    }

    public record Lock(List<Integer> heights) implements Security {
        public boolean fits(Key key) {
            return Streams.zip(heights.stream(), key.heights().stream(), Integer::sum).allMatch(totalHeight -> totalHeight <= 5);
        }
    }

    public record Key(List<Integer> heights) implements Security {
    }

    public record Combination(Lock lock, Key key) {
    }
}
