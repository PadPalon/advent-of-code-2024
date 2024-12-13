package ch.neukom.advent2024.day6;

import ch.neukom.advent2024.util.data.Direction;
import ch.neukom.advent2024.util.data.Position;
import ch.neukom.advent2024.util.inputreaders.InputArrayReader;

import java.util.List;

public class Util {
    private Util() {
    }

    public static Boolean[][] getFinishedPatrol(InputArrayReader reader,
                                                Boolean[][] facilityMap) {
        int height = (int) reader.getLineCount();
        int width = reader.getFirstLine().length();

        Position guardPosition = getInitialGuardPosition(reader);
        Direction guardDirection = Direction.NORTH;

        Boolean[][] patrolMap = reader.readIntoArray(symbol -> symbol.symbol() == '^', Boolean.class);
        while (guardPosition.isInside(width, height)) {
            Position nextPosition = guardPosition.move(guardDirection);
            if (nextPosition.isInside(width, height)) {
                Boolean hasObstacle = facilityMap[(int) nextPosition.y()][(int) nextPosition.x()];
                if (hasObstacle) {
                    guardDirection = guardDirection.turnRight();
                } else {
                    guardPosition = nextPosition;
                    patrolMap[(int) guardPosition.y()][(int) guardPosition.x()] = true;
                }
            } else {
                guardPosition = nextPosition;
            }
        }
        return patrolMap;
    }

    public static Position getInitialGuardPosition(InputArrayReader reader) {
        Position guardPosition = null;
        List<String> lines = reader.readInput().toList();
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            int guardIndex = line.indexOf('^');
            if (guardIndex >= 0) {
                guardPosition = new Position(guardIndex, y);
                break;
            }
        }
        if (guardPosition == null) {
            throw new IllegalStateException();
        }
        return guardPosition;
    }

    public record PatrolStep(Position position, Direction direction) {
    }
}
