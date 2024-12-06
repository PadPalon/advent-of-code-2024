package ch.neukom.advent2024.day2;

import ch.neukom.advent2024.util.inputreaders.InputResourceReader;

import java.io.IOException;
import java.util.Arrays;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        long safeReports = reader.readInput()
            .map(line -> Arrays.stream(line.split(" ")).mapToLong(Long::valueOf).toArray())
            .filter(Part1::isSafe)
            .count();
        System.out.printf("%s reports are safe", safeReports);
    }

    private static boolean isSafe(long[] line) {
        int length = line.length;
        boolean increasing = line[0] < line[1];
        for (int i = 0; i < length - 1; i++) {
            long current = line[i];
            long lower = increasing ? current + 1 : current - 3;
            long upper = increasing ? current + 3 : current - 1;
            long next = line[i + 1];
            if (next < lower || next > upper) {
                return false;
            }
        }
        return true;
    }
}
