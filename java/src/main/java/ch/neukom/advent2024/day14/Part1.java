package ch.neukom.advent2024.day14;

import ch.neukom.advent2024.util.inputreaders.InputResourceReader;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Part1 {
    public static final int TIME = 100;
    public static final int WIDTH = 101;
    public static final int HEIGHT = 103;

    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        int safetyFactor = reader.readInput()
            .map(Util::parseRobot)
            .map(robot -> robot.move(TIME, WIDTH, HEIGHT))
            .collect(Collectors.groupingBy(robot -> robot.getQuadrant(WIDTH, HEIGHT)))
            .entrySet()
            .stream()
            .filter(entry -> entry.getKey() > 0)
            .map(Map.Entry::getValue)
            .mapToInt(List::size)
            .reduce(1, (left, right) -> left * right);
        System.out.printf("The safety factor after %s seconds is %s", TIME, safetyFactor);
    }
}
