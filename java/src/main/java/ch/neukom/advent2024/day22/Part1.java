package ch.neukom.advent2024.day22;

import ch.neukom.advent2024.util.inputreaders.InputResourceReader;

import java.io.IOException;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        long sum = reader.readInput()
            .mapToLong(Long::parseLong)
            .map(value -> Util.calculateNthSecretNumber(value, 2000))
            .sum();
        System.out.printf("The sum of the 2000th secret numbers is %s", sum);
    }
}
