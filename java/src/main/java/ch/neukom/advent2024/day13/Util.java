package ch.neukom.advent2024.day13;

import ch.neukom.advent2024.util.data.Position;
import ch.neukom.advent2024.util.inputreaders.InputResourceReader;
import ch.neukom.advent2024.util.splitter.Splitters;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Util {
    private static final Pattern BUTTON_PATTERN = Pattern.compile(".*X\\+(\\d+), Y\\+(\\d+)");
    private static final Pattern PRIZE_PATTERN = Pattern.compile(".*X=(\\d+), Y=(\\d+)");

    private Util() {
    }

    public static List<ClawMachine> readClawMachines(InputResourceReader reader) {
        return Splitters.PARAGRAPH_SPLITTER.splitToStream(reader.readInput().collect(Collectors.joining("\n")))
            .map(Splitters.NEWLINE_SPLITTER::splitToList)
            .map(lines -> new ClawMachine(
                getButton(lines.get(0)),
                getButton(lines.get(1)),
                getPrize(lines.get(2))
            ))
            .toList();
    }

    private static Button getButton(String line) {
        Matcher matcher = BUTTON_PATTERN.matcher(line);
        if (matcher.find()) {
            return new Button(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
        } else {
            throw new IllegalStateException();
        }
    }

    private static Position getPrize(String line) {
        Matcher matcher = PRIZE_PATTERN.matcher(line);
        if (matcher.find()) {
            return new Position(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
        } else {
            throw new IllegalStateException();
        }
    }

    public record ClawMachine(Button a, Button b, Position prize) {
        public Optional<Long> getCost() {
            /*
             do not try to understand these calculations logically, look up how to solve a system of equations instead
             the system to solve is:
             aCount * a.x() + bCount * b.x() = prize.x()
             aCount * a.y() + bCount * b.y() = prize.y()
            */
            long bCount = (a.x() * prize.y() - prize.x() * a.y()) / (a.x() * b.y() - b.x() * a.y());
            long aCount = (prize.x() - bCount * b.x()) / a.x();

            if (
                aCount * a.x() + bCount * b.x() != prize.x()
                    || aCount * a.y() + bCount * b.y() != prize.y()
            ) {
                return Optional.empty();
            }

            return Optional.of(aCount * 3 + bCount);
        }

        public ClawMachine addToPrize(long addition) {
            return new ClawMachine(
                a,
                b,
                new Position(prize.x() + addition, prize.y() + addition)
            );
        }
    }

    public record Button(long x, long y) {
    }
}
