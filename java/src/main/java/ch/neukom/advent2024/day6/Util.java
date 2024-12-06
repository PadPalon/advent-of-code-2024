package ch.neukom.advent2024.day6;

import ch.neukom.advent2024.util.InputArrayReader;
import ch.neukom.advent2024.util.data.Position;

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
        while (positionInsideMap(guardPosition, width, height)) {
            Position nextPosition = getNextPosition(guardPosition, guardDirection);
            if (positionInsideMap(nextPosition, width, height)) {
                Boolean hasObstacle = facilityMap[nextPosition.y()][nextPosition.x()];
                if (hasObstacle) {
                    guardDirection = turnRight(guardDirection);
                } else {
                    guardPosition = nextPosition;
                    patrolMap[guardPosition.y()][guardPosition.x()] = true;
                }
            } else {
                guardPosition = nextPosition;
            }
        }
        return patrolMap;
    }

    public static Util.Direction turnRight(Util.Direction direction) {
        return switch (direction) {
            case NORTH -> Util.Direction.EAST;
            case EAST -> Util.Direction.SOUTH;
            case SOUTH -> Util.Direction.WEST;
            case WEST -> Util.Direction.NORTH;
        };
    }

    public static Position getNextPosition(Position currentPosition, Util.Direction direction) {
        return switch (direction) {
            case NORTH -> new Position(currentPosition.x(), currentPosition.y() - 1);
            case EAST -> new Position(currentPosition.x() + 1, currentPosition.y());
            case SOUTH -> new Position(currentPosition.x(), currentPosition.y() + 1);
            case WEST -> new Position(currentPosition.x() - 1, currentPosition.y());
        };
    }

    public static boolean positionInsideMap(Position position, int width, int height) {
        return position.x() >= 0
            && position.y() >= 0
            && position.x() < width
            && position.y() < height;
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

    public enum Direction {
        NORTH, EAST, SOUTH, WEST
    }

    public record PatrolStep(Position position, Direction direction) {
    }
}
