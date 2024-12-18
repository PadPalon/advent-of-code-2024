package ch.neukom.advent2024.day18;

import ch.neukom.advent2024.util.data.Position;
import ch.neukom.advent2024.util.inputreaders.InputResourceReader;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static ch.neukom.advent2024.day18.Util.*;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        Optional<String> lastPlaced;
        int index = 0;
        while (true) {
            Optional<String> nextPlaced = getLastPlaced(reader, index);
            if (nextPlaced.isPresent()) {
                lastPlaced = nextPlaced;
                break;
            }
            /*
             this will eventually finish, but I used a bigger step size to find a rough place where the way is blocked
             and only then went down to 1
            */
            index += 1;
        }

        System.out.printf("The byte that blocks the way is %s", lastPlaced.orElseThrow());
    }

    private static Optional<String> getLastPlaced(InputResourceReader reader, int additionalSteps) {
        Map<Position, MazeElement> maze = readMaze(reader, DEFAULT_FALL_LIMIT + additionalSteps);

        Start start = maze.values().stream().filter(Start.class::isInstance).map(Start.class::cast).findAny().orElseThrow();

        Map<MazeElement, Move> scores = calculateRoutes(s -> s.put(start, new Move(0, Set.of())), maze);
        boolean isPossible = maze.values()
            .stream()
            .filter(End.class::isInstance)
            .map(End.class::cast)
            .findAny()
            .filter(scores::containsKey)
            .isPresent();
        if (isPossible) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(reader.readInput().toList().get(DEFAULT_FALL_LIMIT + additionalSteps - 1));
        }
    }
}
