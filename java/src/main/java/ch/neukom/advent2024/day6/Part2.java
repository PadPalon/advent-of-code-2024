package ch.neukom.advent2024.day6;

import ch.neukom.advent2024.util.InputArrayReader;
import ch.neukom.advent2024.util.data.Position;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static ch.neukom.advent2024.day6.Util.*;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputArrayReader reader = new InputArrayReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputArrayReader reader) {
        Boolean[][] facilityMap = reader.readIntoArray(symbol -> symbol.symbol() == '#', Boolean.class);
        Boolean[][] patrolMap = getFinishedPatrol(reader, facilityMap);

        int possibleObstructions = 0;
        for (int y = 0; y < patrolMap.length; y++) {
            Boolean[] line = patrolMap[y];
            for (int x = 0; x < line.length; x++) {
                Boolean visited = line[x];
                if (visited && tryObstruction(x, y, reader, facilityMap)) {
                    possibleObstructions++;
                }
            }
        }

        System.out.printf("There are %s possible obstructions to place", possibleObstructions);
    }

    private static boolean tryObstruction(int x,
                                          int y,
                                          InputArrayReader reader,
                                          Boolean[][] facilityMap) {
        Position initialGuardPosition = getInitialGuardPosition(reader);
        if (initialGuardPosition.equals(new Position(x, y))) {
            return false;
        }
        facilityMap[y][x] = true;
        boolean hasLoop = hasLoop(initialGuardPosition, facilityMap);
        facilityMap[y][x] = false;
        return hasLoop;
    }

    private static boolean hasLoop(Position initialGuardPosition,
                                   Boolean[][] facilityMap) {
        int height = facilityMap.length;
        int width = facilityMap[0].length;

        Position guardPosition = initialGuardPosition;
        Direction guardDirection = Direction.NORTH;

        Set<PatrolStep> steps = new HashSet<>();
        steps.add(new PatrolStep(guardPosition, guardDirection));

        while (positionInsideMap(guardPosition, width, height)) {
            Position nextPosition = getNextPosition(guardPosition, guardDirection);
            if (positionInsideMap(nextPosition, width, height)) {
                Boolean hasObstacle = facilityMap[nextPosition.y()][nextPosition.x()];
                if (hasObstacle) {
                    guardDirection = turnRight(guardDirection);
                } else {
                    guardPosition = nextPosition;
                    if (steps.contains(new PatrolStep(guardPosition, guardDirection))) {
                        return true;
                    } else {
                        steps.add(new PatrolStep(guardPosition, guardDirection));
                    }
                }
            } else {
                return false;
            }
        }

        return false;
    }

}
