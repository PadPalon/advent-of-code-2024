package ch.neukom.advent2024.day14;

import ch.neukom.advent2024.util.data.Position;
import ch.neukom.advent2024.util.inputreaders.InputResourceReader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Part2 {
    public static final int MIN_TIME = 0;
    public static final int MAX_TIME = 10000;
    public static final int WIDTH = 101;
    public static final int HEIGHT = 103;
    public static final String IMAGE_FORMAT = "png";
    public static final Color FILLED_COLOR = Color.WHITE;
    public static final Color EMPTY_COLOR = Color.BLACK;

    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        List<Util.Robot> robots = reader.readInput()
            .map(Util::parseRobot)
            .toList();
        IntStream.range(MIN_TIME, MAX_TIME)
            /*
             these numbers were discovered by looking at the generated pictures and seeing vertical and horizontal
             stripes showing up in repeating cycles
             when only printing the pictures on these cycles, the tree emerges quickly
             the final image is also where the cycles overlap, so I assume that was the way to actually solve this
            */
            .filter(seconds -> (seconds - 38) % WIDTH == 0 || (seconds - 88) % HEIGHT == 0)
            .forEach(seconds -> printRobots(seconds, robots.stream().map(r -> r.move(seconds, WIDTH, HEIGHT)).toList()));
    }

    private static void printRobots(int seconds, List<Util.Robot> robots) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Set<Position> robotPositions = robots.stream()
            .map(Util.Robot::position)
            .collect(Collectors.toSet());
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                Position position = new Position(x, y);
                if (robotPositions.contains(position)) {
                    image.setRGB(x, y, FILLED_COLOR.getRGB());
                } else {
                    image.setRGB(x, y, EMPTY_COLOR.getRGB());
                }
            }
        }
        try {
            File output = new File(("images/%s.%s").formatted(seconds, IMAGE_FORMAT));
            ImageIO.write(image, IMAGE_FORMAT, output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
