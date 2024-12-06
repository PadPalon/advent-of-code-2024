package ch.neukom.advent2024.day3;

import ch.neukom.advent2024.util.inputreaders.InputResourceReader;

import java.io.IOException;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Part1 {
    private static final Pattern MUL_PATTERN = Pattern.compile("mul\\([0-9]+,[0-9]+\\)");

    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        String input = reader.readInput().collect(Collectors.joining());
        long sum = MUL_PATTERN.matcher(input)
            .results()
            .map(MatchResult::group)
            .map(group -> group.substring("mul(".length(), group.length() - 1))
            .map(digits -> digits.split(","))
            .mapToLong(digits -> Long.parseLong(digits[0]) * Long.parseLong(digits[1]))
            .sum();
        System.out.printf("The sum of all multiplications is %s", sum);
    }
}
