package ch.neukom.advent2024.util.data;

public enum Direction {
    NORTH, EAST, SOUTH, WEST;

    public Direction turnRight() {
        return switch (this) {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }
}
