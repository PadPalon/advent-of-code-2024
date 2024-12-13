package ch.neukom.advent2024.day13;

import ch.neukom.advent2024.util.inputreaders.InputResourceReader;

import java.io.IOException;
import java.util.Optional;

import static ch.neukom.advent2024.day13.Util.ClawMachine;
import static ch.neukom.advent2024.day13.Util.readClawMachines;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        long sum = readClawMachines(reader)
            .stream()
            .map(ClawMachine::getCost)
            .filter(Optional::isPresent)
            .mapToLong(Optional::get)
            .sum();
        System.out.printf("It costs %s tokes to win all possible prizes", sum);
    }
}
