package ch.neukom.advent2024.day22;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.LongStream;

import static java.util.stream.Collectors.*;

public class Util {
    private Util() {
    }

    public static long calculateNthSecretNumber(long initial, int n) {
        return calculateNthSecretNumber(initial, n, v -> {});
    }

    public static long calculateNthSecretNumber(long initial, int n, Consumer<Long> consumer) {
        long last = LongStream.range(0, n)
            .reduce(initial, (left, right) -> {
                consumer.accept(left);
                return calculateNextSecretNumber(left);
            });
        consumer.accept(last);
        return last;
    }

    public static long calculateNextSecretNumber(long initial) {
        long secretNumber = initial;
        secretNumber = calculateStep(secretNumber, secretNumber * 64);
        secretNumber = calculateStep(secretNumber, secretNumber / 32);
        secretNumber = calculateStep(secretNumber, secretNumber * 2048);
        return secretNumber;
    }

    public static long calculateStep(long left, long right) {
        return prune(mix(left, right));
    }

    public static long mix(long left, long right) {
        return left ^ right;
    }

    public static long prune(long value) {
        return value % 16777216;
    }

    public static class PriceCalculator implements Consumer<Long> {
        private final Queue<Integer> slidingWindow = EvictingQueue.create(5);
        private final Map<List<Integer>, Long> profits = Maps.newHashMap();
        private final Set<List<Integer>> seenPriceDifferences = Sets.newHashSet();

        private List<Integer> currentPriceDifferences = List.of();

        public void resetSeenPriceDifferences() {
            seenPriceDifferences.clear();
        }

        public Collection<Long> getProfits() {
            return profits.values();
        }

        @Override
        public void accept(Long value) {
            int price = (int) (value % 10);
            addNextValue(price);
            if (currentPriceDifferences.size() == 4 && !seenPriceDifferences.contains(currentPriceDifferences)) {
                seenPriceDifferences.add(currentPriceDifferences);
                profits.compute(currentPriceDifferences, (key, previous) -> {
                    if (previous == null) {
                        return (long) price;
                    } else {
                        return previous + price;
                    }
                });
            }
        }

        private void addNextValue(int price) {
            slidingWindow.offer(price);
            List<Integer> priceDifferences = Lists.newArrayListWithCapacity(4);
            Integer current = null;
            for (Integer next : slidingWindow) {
                if (current != null) {
                    priceDifferences.add(next - current);
                }
                current = next;
            }
            currentPriceDifferences = priceDifferences;
        }

        @Override
        public String toString() {
            return profits.entrySet()
                .stream()
                .map(entry -> "[%s] -> %s".formatted(entry.getKey().stream().map(String::valueOf).collect(joining(", ")), entry.getValue()))
                .collect(joining("\n"));
        }
    }
}
