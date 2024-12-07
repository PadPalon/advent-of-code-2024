package ch.neukom.advent2024.day7;

import ch.neukom.advent2024.util.inputreaders.InputResourceReader;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static ch.neukom.advent2024.util.splitter.Splitters.COLON_SPLITTER;
import static ch.neukom.advent2024.util.splitter.Splitters.WHITESPACE_SPLITTER;
import static java.util.stream.Collectors.toSet;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        List<Calculation> sourceCalculations = Lists.newArrayList();
        reader.readInput()
            .forEach(line -> {
                List<String> split = COLON_SPLITTER.splitToList(line);
                String result = split.getFirst();
                List<BigInteger> parts = WHITESPACE_SPLITTER.splitToStream(split.getLast())
                    .map(BigInteger::new)
                    .toList();
                sourceCalculations.add(new Calculation(new BigInteger(result), parts, List.of()));
            });

        BigInteger sum = sourceCalculations
            .stream()
            .map(entry -> findCorrectCalculation(entry.result(), entry.parts()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(Calculation::result)
            .reduce(BigInteger.ZERO, BigInteger::add);

        System.out.printf("The total calibration result is %s", sum);
    }

    private static Optional<Calculation> findCorrectCalculation(BigInteger result, List<BigInteger> parts) {
        return getPossibleOperators(parts.size() - 1)
            .stream()
            .map(operators -> new Calculation(result, parts, operators))
            .filter(Calculation::isCorrect)
            .findAny();
    }

    private static Set<List<Operator>> getPossibleOperators(int size) {
        if (size == 1) {
            return Set.of(
                List.of(Operator.PLUS),
                List.of(Operator.MULTIPLY),
                List.of(Operator.CONCATENATION)
            );
        } else if (size > 1) {
            return getPossibleOperators(size - 1)
                .stream()
                .flatMap(operators -> {
                    List<Operator> withPlus = Lists.newCopyOnWriteArrayList(operators);
                    withPlus.add(Operator.PLUS);
                    List<Operator> withMultiply = Lists.newCopyOnWriteArrayList(operators);
                    withMultiply.add(Operator.MULTIPLY);
                    List<Operator> withConcatenation = Lists.newCopyOnWriteArrayList(operators);
                    withConcatenation.add(Operator.CONCATENATION);
                    return Stream.of(withPlus, withMultiply, withConcatenation);
                })
                .collect(toSet());
        } else {
            return Set.of();
        }
    }

    private record Calculation(BigInteger result, List<BigInteger> parts, List<Operator> operators) {
        public boolean isCorrect() {
            BigInteger calculatedResult = parts.getFirst();
            for (int index = 0; index < operators.size(); index++) {
                BigInteger nextPart = parts.get(index + 1);
                Operator operator = operators.get(index);
                calculatedResult = switch (operator) {
                    case PLUS -> calculatedResult.add(nextPart);
                    case MULTIPLY -> calculatedResult.multiply(nextPart);
                    case CONCATENATION -> new BigInteger(calculatedResult.toString() + nextPart.toString());
                };
            }
            return calculatedResult.compareTo(result) == 0;
        }
    }

    private enum Operator {
        PLUS,
        MULTIPLY,
        CONCATENATION
    }
}
