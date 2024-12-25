package ch.neukom.advent2024.day24;

import ch.neukom.advent2024.util.inputreaders.InputResourceReader;
import ch.neukom.advent2024.util.splitter.Splitters;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ch.neukom.advent2024.day24.Util.Connection;
import static ch.neukom.advent2024.day24.Util.GateSystem;
import static java.util.stream.Collectors.*;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        String input = reader.readInput().collect(joining("\n"));
        List<String> paragraphs = Splitters.PARAGRAPH_SPLITTER.splitToList(input);
        Map<String, Boolean> initialValues = Splitters.NEWLINE_SPLITTER.splitToStream(paragraphs.getFirst())
            .map(line -> line.split(": "))
            .collect(toMap(split -> split[0], split -> "1".equals(split[1])));
        Set<Connection> connections = Splitters.NEWLINE_SPLITTER.splitToStream(paragraphs.getLast())
            .map(Util::parseConnection)
            .collect(toSet());
        GateSystem gateSystem = new GateSystem(connections, initialValues);
        gateSystem.runToCompletion();
        BigInteger value = gateSystem.getOutputValue();
        System.out.printf("The value on the output wires is %s", value);
    }
}
