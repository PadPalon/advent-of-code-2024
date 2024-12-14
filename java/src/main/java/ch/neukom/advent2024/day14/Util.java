package ch.neukom.advent2024.day14;

import ch.neukom.advent2024.util.data.Position;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static final Pattern ROBOT_PATTERN = Pattern.compile("p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)");

    private Util() {
    }

    public static Robot parseRobot(String line) {
        Matcher matcher = ROBOT_PATTERN.matcher(line);
        if (matcher.find() && matcher.groupCount() == 4) {
            return new Robot(
                new Position(
                    parseNumber(matcher.group(1)),
                    parseNumber(matcher.group(2))
                ),
                new Velocity(
                    parseNumber(matcher.group(3)),
                    parseNumber(matcher.group(4))
                )
            );
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static long parseNumber(String numberString) {
        if (numberString.startsWith("-")) {
            return Long.parseLong(numberString.substring(1)) * -1;
        } else {
            return Long.parseLong(numberString);
        }
    }

    public record Robot(Position position, Velocity velocity) {
        public Robot move(long seconds, long width, long height) {
            long xChange = (velocity.x() * seconds) % width;
            long xNew = (position.x() + xChange) % width;
            if (xNew < 0) {
                xNew += width;
            }
            long yChange = (velocity.y() * seconds) % height;
            long yNew = (position.y() + yChange) % height;
            if (yNew < 0) {
                yNew += height;
            }
            return new Robot(new Position(xNew, yNew), velocity);
        }

        public int getQuadrant(int width, int height) {
            int xMiddle = width / 2;
            int yMiddle = height / 2;
            if (position.x() < xMiddle && position.y() < yMiddle) {
                return 1;
            } else if (position.x() < xMiddle && position.y() > yMiddle) {
                return 2;
            } else if (position.x() > xMiddle && position.y() < yMiddle) {
                return 3;
            } else if (position.x() > xMiddle && position.y() > yMiddle) {
                return 4;
            } else {
                return 0;
            }
        }
    }

    public record Velocity(long x, long y) {
    }
}
