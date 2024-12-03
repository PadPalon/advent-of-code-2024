package ch.neukom.advent2024.day3;

import ch.neukom.advent2024.util.InputResourceReader;

import java.io.IOException;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Part2 {
    private static final Pattern INSTRUCTION_PATTERN = Pattern.compile("(mul\\([0-9]+,[0-9]+\\))|do\\(\\)|don't\\(\\)");

    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        String input = reader.readInput().collect(Collectors.joining());
        MulComputer computer = new MulComputer();
        INSTRUCTION_PATTERN.matcher(input)
            .results()
            .map(MatchResult::group)
            .forEach(computer::compute);
        System.out.printf("The sum of all enabled multiplications is %s", computer.getSum());
    }

    private static class MulComputer {
        private long sum = 0;
        private boolean enabled = true;

        public void compute(String instruction) {
            if (instruction.equals("do()")) {
                enabled = true;
            } else if (instruction.equals("don't()")) {
                enabled = false;
            } else if (enabled) {
                String[] digits = instruction.substring("mul(".length(), instruction.length() - 1).split(",");
                sum += Long.parseLong(digits[0]) * Long.parseLong(digits[1]);
            }
        }

        public long getSum() {
            return sum;
        }
    }
}
