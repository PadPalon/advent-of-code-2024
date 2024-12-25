package ch.neukom.advent2024.day24;

import com.google.common.collect.Maps;

import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.joining;

public class Util {
    private Util() {
    }

    public static class GateSystem {
        private final Map<String, Boolean> values = Maps.newHashMap();
        private final Set<Connection> connections;

        public GateSystem(Set<Connection> connections, Map<String, Boolean> initialValues) {
            this.connections = connections;
            this.values.putAll(initialValues);
        }

        public Map<String, Boolean> getValues() {
            return values;
        }

        @SuppressWarnings("StatementWithEmptyBody")
        public void runToCompletion() {
            while (runCalculations()) {
                // nothing to do
            }
        }

        public BigInteger getOutputValue() {
            return values.entrySet()
                .stream()
                .filter(entry -> entry.getKey().startsWith("z"))
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .toList()
                .reversed()
                .stream()
                .map(bit -> bit ? "1" : "0")
                .collect(collectingAndThen(joining(), bitString -> new BigInteger(bitString, 2)));
        }

        public boolean runCalculations() {
            boolean valuesChanged = false;
            for (Connection connection : connections) {
                Optional<Boolean> left = getWireValue(connection.incomingLeft());
                Optional<Boolean> right = getWireValue(connection.incomingRight());
                Optional<Boolean> output = getWireValue(connection.outgoing());

                if (left.isPresent() && right.isPresent() && output.isEmpty()) {
                    boolean result = connection.type().apply(left.get(), right.get());
                    values.put(connection.outgoing(), result);
                    valuesChanged = true;
                }
            }
            return valuesChanged;
        }

        private Optional<Boolean> getWireValue(String wire) {
            return Optional.ofNullable(values.get(wire));
        }
    }

    public static Connection parseConnection(String line) {
        String[] inputOutput = line.split(" -> ");
        String[] inputSplit = inputOutput[0].split(" ");
        return new Connection(inputSplit[0], inputSplit[2], ConnectionType.valueOf(inputSplit[1]), inputOutput[1]);
    }

    public record Connection(String incomingLeft, String incomingRight, ConnectionType type, String outgoing) {
    }

    public enum ConnectionType {
        AND((left, right) -> left && right),
        OR((left, right) -> left || right),
        XOR((left, right) -> left ^ right);

        private final BiPredicate<Boolean, Boolean> predicate;

        ConnectionType(BiPredicate<Boolean, Boolean> predicate) {
            this.predicate = predicate;
        }

        private boolean apply(Boolean left, Boolean right) {
            return predicate.test(left, right);
        }
    }
}
