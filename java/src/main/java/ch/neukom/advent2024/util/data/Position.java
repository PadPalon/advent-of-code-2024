package ch.neukom.advent2024.util.data;

public record Position(int x, int y) {
    public Position move(Direction direction) {
        return switch (direction) {
            case NORTH -> new Position(x(), y() - 1);
            case EAST -> new Position(x() + 1, y());
            case SOUTH -> new Position(x(), y() + 1);
            case WEST -> new Position(x() - 1, y());
        };
    }

    public boolean isInside(int width, int height) {
        return x() >= 0
            && y() >= 0
            && x() < width
            && y() < height;
    }
}
