package ch.neukom.advent2024.day7;

import ch.neukom.advent2024.util.inputreaders.InputResourceReader;
import com.google.common.collect.Lists;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static ch.neukom.advent2024.util.splitter.Splitters.COLON_SPLITTER;
import static ch.neukom.advent2024.util.splitter.Splitters.WHITESPACE_SPLITTER;

public class Util {
    public static BigInteger findSolution(InputResourceReader reader, Operator... availableOperators) {
        List<Calculation> sourceCalculations = getSourceCalculations(reader);

        return sourceCalculations
            .stream()
            .map(entry -> findCorrectCalculation(entry.result(), entry.parts(), availableOperators))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(Calculation::result)
            .reduce(BigInteger.ZERO, BigInteger::add);
    }

    private static List<Calculation> getSourceCalculations(InputResourceReader reader) {
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
        return sourceCalculations;
    }

    public static Optional<Calculation> findCorrectCalculation(BigInteger result,
                                                               List<BigInteger> parts,
                                                               Operator... availableOperators) {
        return getPossibleOperators(parts.size() - 1, availableOperators)
            .map(operators -> new Calculation(result, parts, operators))
            .filter(Calculation::isCorrect)
            .findAny();
    }

    public static Stream<List<Operator>> getPossibleOperators(int size, Operator... availableOperators) {
        if (size == 1) {
            return Arrays.stream(availableOperators)
                .map(List::of);
        } else if (size > 1) {
            return getPossibleOperators(size - 1, availableOperators)
                .flatMap(operators -> Arrays.stream(availableOperators)
                    .map(operator -> {
                        List<Operator> withAddedOperator = Lists.newCopyOnWriteArrayList(operators);
                        withAddedOperator.add(operator);
                        return withAddedOperator;
                    }));
        } else {
            return Stream.of();
        }
    }

    public enum Operator {
        PLUS,
        MULTIPLY,
        CONCATENATION
    }

    public record Calculation(BigInteger result, List<BigInteger> parts, List<Operator> operators) {
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
                if (calculatedResult.compareTo(result) > 0) {
                    return false;
                }
            }
            return calculatedResult.compareTo(result) == 0;
        }
    }
}
