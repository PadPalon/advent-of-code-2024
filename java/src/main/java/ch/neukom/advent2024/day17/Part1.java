package ch.neukom.advent2024.day17;

import ch.neukom.advent2024.util.inputreaders.InputResourceReader;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;

import static ch.neukom.advent2024.day17.Util.*;
import static java.util.stream.Collectors.joining;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        List<String> lines = reader.readInput().toList();
        List<Integer> output = Lists.newArrayList();
        Computer computer = new Computer(
            getRegister(lines, 0),
            getRegister(lines, 1),
            getRegister(lines, 2),
            output::add
        );
        computer.runProgram(getProgram(lines));
        String outputString = output.stream().map(String::valueOf).collect(joining(","));
        System.out.printf("The output of the program is %s", outputString);
    }
}
