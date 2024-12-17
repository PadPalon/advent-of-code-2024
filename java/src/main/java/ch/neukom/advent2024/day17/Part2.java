package ch.neukom.advent2024.day17;

import ch.neukom.advent2024.day17.Util.Computer;
import ch.neukom.advent2024.util.inputreaders.InputResourceReader;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.LongStream;

import static ch.neukom.advent2024.day17.Util.getProgram;
import static ch.neukom.advent2024.day17.Util.getRegister;
import static java.util.stream.Collectors.joining;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        List<String> lines = reader.readInput().toList();
        List<Integer> program = getProgram(lines);
        Function<Long, List<Integer>> computerSetup = initialA -> {
            List<Integer> output = Lists.newArrayList();
            Computer computer = new Computer(
                initialA,
                getRegister(lines, 1),
                getRegister(lines, 2),
                output::add
            );
            computer.runProgram(program);
            return output;
        };

        /*
         * hey there
         * if you're reading this, you're probably as lost as I was when starting this part
         *
         * I never really understood the underlying mechanisms and how to solve this "correctly", but maybe my thoughts
         * will help you
         *
         * first, I noticed that the output steadily grew in length with an increasing initial A state. checking at which
         * values the output length grew by 1, gave me the lowest and highest possible values necessary
         * see searchStart and searchEnd below
         *
         * then, I noticed that when just incrementing the initial A state from 0 up, the digits at the front of the
         * output changed way more quickly than those in the back. in fact, they clearly had some cycle, and a digit
         * only changed if the digit in front of it completed a cycle. that meant, I could step through my search space
         * in very large steps and check if the last few digits matched, which got me closer to a solution. by increasing
         * the digits being checked and reducing the step size, I got ever closer to solutions
         * see while loop below
         *
         * by searching around these promising initial states, I actually found actual solutions, but never the lowest one.
         * comparing the different solutions, I found that there are always two solutions that are 256 (which of course
         * is 2^8) apart, and then those 2 solutions are 8388352 (which is (2^15 - 1) * 256) away from the next pair of
         * solutions. then I just used those values to start looking farther backwards from my lowest known solution to
         * find the lowest possible
         * see stream part below
         *
         * good luck fellow puzzler
         */

        long searchStart = (long) Math.pow(8, program.size() - 1);
        long searchEnd = (long) Math.pow(8, program.size());

        long skip = 100000000L;
        long start = 0;
        int offset = 1;
        boolean found = false;
        List<Integer> programSub = program.subList(program.size() - offset, program.size());
        while (start + skip < searchEnd) {
            List<Integer> output = computerSetup.apply(searchStart + start);
            List<Integer> outputSub = output.subList(output.size() - offset, output.size());
            if (outputSub.equals(programSub)) {
                found = true;
                System.out.printf("%s%n", start);
                System.out.printf("%s%n", searchStart + start);
                System.out.printf("%s%n", programToString(outputSub));
                System.out.printf("%s%n", programToString(programSub));
                break;
            }
            start += skip;
        }
        if (!found) {
            throw new IllegalStateException();
        }

        long lowestKnownSolution = 164541026365117L;
        long highestKnownSolution = 164546384918965L;
        long diff = (long) ((Math.pow(2, 15) - 2) * 256);
        List<Long> validInitialStates = LongStream.range(lowestKnownSolution - diff * 10, highestKnownSolution)
            .filter(initialA -> computerSetup.apply(initialA).equals(program))
            .boxed()
            .toList();
        long neededInitialA = validInitialStates.getFirst();
        System.out.printf("The required initial value of A is %s", neededInitialA);
    }

    private static String programToString(List<Integer> neededProgram) {
        return neededProgram.stream().map(String::valueOf).collect(joining(","));
    }
}
