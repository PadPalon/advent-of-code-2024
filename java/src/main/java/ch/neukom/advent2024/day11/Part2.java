package ch.neukom.advent2024.day11;

import ch.neukom.advent2024.util.inputreaders.InputResourceReader;
import ch.neukom.advent2024.util.math.MathHelper;
import ch.neukom.advent2024.util.splitter.Splitters;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

public class Part2 {
    private static final int MAX_BLINKS = 75;

    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        List<Long> initialArrangement = Splitters.WHITESPACE_SPLITTER.splitToStream(reader.getFirstLine())
            .map(Long::parseLong)
            .toList();

        Map<CacheKey, Long> cache = Maps.newHashMap();
        long stoneCount = 0;
        for (Long stone : initialArrangement) {
            stoneCount += blink(stone, 0, cache);
        }
        System.out.printf("There are %s stones after %s blinks", stoneCount, MAX_BLINKS);
    }

    private static Long blink(Long stone, int blinks, Map<CacheKey, Long> cache) {
        CacheKey cacheKey = new CacheKey(stone, blinks);
        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        } else {
            int digitCount = MathHelper.digitCount(stone);
            Long count = 0L;
            if (blinks == MAX_BLINKS) {
                count = 1L;
            } else if (stone == 0) {
                count = blink(1L, blinks + 1, cache);
            } else if (digitCount % 2 == 0) {
                BigDecimal scaledValue = BigDecimal.valueOf(stone).movePointLeft(digitCount / 2);
                long left = scaledValue.setScale(0, RoundingMode.FLOOR).longValue();
                count += blink(left, blinks + 1, cache);
                long right = scaledValue.subtract(BigDecimal.valueOf(left)).movePointRight(digitCount / 2).longValue();
                count += blink(right, blinks + 1, cache);
            } else {
                count = blink(stone * 2024, blinks + 1, cache);
            }
            cache.put(cacheKey, count);
            return count;
        }
    }

    private record CacheKey(long stone, int turn) {
    }
}
