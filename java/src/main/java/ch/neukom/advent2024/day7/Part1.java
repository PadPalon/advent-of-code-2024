package ch.neukom.advent2024.day7;

import ch.neukom.advent2024.util.inputreaders.InputResourceReader;

import java.io.IOException;
import java.math.BigInteger;

import static ch.neukom.advent2024.day7.Util.Operator;
import static ch.neukom.advent2024.day7.Util.findSolution;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        BigInteger sum = findSolution(reader, Operator.PLUS, Operator.MULTIPLY);
        System.out.printf("The total calibration result is %s", sum);
    }
}