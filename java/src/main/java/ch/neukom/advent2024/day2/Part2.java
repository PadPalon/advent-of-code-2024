package ch.neukom.advent2024.day2;

import ch.neukom.advent2024.util.InputResourceReader;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        long safeReports = reader.readInput()
            .map(line -> Arrays.stream(line.split(" ")).map(Long::valueOf).toList())
            .filter(line -> isSafe(line, false))
            .count();
        System.out.printf("%s reports are safe", safeReports);
    }

    private static boolean isSafe(List<Long> report, boolean problemDampened) {
        int size = report.size();

        if (size <= 1) {
            return true;
        }
        if (!problemDampened && isSafe(report.subList(1, size), true)) {
            return true;
        }

        boolean increasing = report.get(0) < report.get(1);
        for (int i = 0; i < size - 1; i++) {
            long current = report.get(i);
            long lower = increasing ? current + 1 : current - 3;
            long upper = increasing ? current + 3 : current - 1;
            long next = report.get(i + 1);
            if (next < lower || next > upper) {
                if (problemDampened) {
                    return false;
                } else {
                    // would be nicer to go from current index backwards
                    return IntStream.rangeClosed(0, i)
                        .mapToObj(j -> getDampenedReport(report, j))
                        .anyMatch(d -> isSafe(d, true));
                }
            }
        }
        return true;
    }

    private static ImmutableList<Long> getDampenedReport(List<Long> report, int indexToRemove) {
        int size = report.size();
        ImmutableList.Builder<Long> dampenedBuilder = ImmutableList.<Long>builderWithExpectedSize(size - 1)
            .addAll(report.subList(0, indexToRemove + 1));
        if (indexToRemove < size - 2) {
            dampenedBuilder.addAll(report.subList(indexToRemove + 2, size));
        }
        ImmutableList<Long> dampenedReport = dampenedBuilder.build();
        if (dampenedReport.size() + 1 != size) {
            throw new IllegalStateException();
        }
        return dampenedReport;
    }
}
