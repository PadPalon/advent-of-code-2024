package ch.neukom.advent2024.day19;

import ch.neukom.advent2024.util.inputreaders.InputResourceReader;
import ch.neukom.advent2024.util.splitter.Splitters;

import java.io.IOException;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        Set<String> availablePatterns = Splitters.COMMA_SPLITTER.splitToStream(reader.getFirstLine()).collect(toSet());
        long creatableDesigns = reader.readInput()
            .skip(2)
            .filter(design -> canDesignBeCreated(design, availablePatterns))
            .count();
        System.out.printf("%s of the designs can be created", creatableDesigns);
    }

    private static boolean canDesignBeCreated(String design, Set<String> availablePatterns) {
        if (availablePatterns.contains(design)) {
            return true;
        }

        return availablePatterns.stream()
            .filter(design::startsWith)
            .anyMatch(availablePattern -> canDesignBeCreated(design.substring(availablePattern.length()), availablePatterns));
    }
}
