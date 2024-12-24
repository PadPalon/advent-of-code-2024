package ch.neukom.advent2024.day22;

import ch.neukom.advent2024.util.inputreaders.InputResourceReader;

import java.io.IOException;
import java.util.Comparator;

import static ch.neukom.advent2024.day22.Util.PriceCalculator;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        PriceCalculator priceCalculator = new PriceCalculator();
        reader.readInput()
            .mapToLong(Long::parseLong)
            .forEach(value -> {
                priceCalculator.resetSeenPriceDifferences();
                Util.calculateNthSecretNumber(value, 2001, priceCalculator);
            });
        Long maxBananas = priceCalculator.getProfits()
            .stream()
            .max(Comparator.naturalOrder())
            .orElseThrow();
        System.out.printf("The most bananas we can get is %s", maxBananas);
    }
}
