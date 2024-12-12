package ch.neukom.advent2024.day12;

import ch.neukom.advent2024.util.data.Direction;
import ch.neukom.advent2024.util.data.Position;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.function.Predicate;

public class Util {
    private Util() {
    }

    public static Set<Region> findPlantRegions(Map<Position, Character> gardenMap) {
        Set<Region> regions = Sets.newHashSet();
        Set<Position> handledPositions = Sets.newHashSet();
        for (Map.Entry<Position, Character> entry : gardenMap.entrySet()) {
            Position plot = entry.getKey();
            if (handledPositions.contains(plot)) {
                continue;
            } else {
                handledPositions.add(plot);
            }

            Character plant = entry.getValue();
            Set<Position> regionPlots = findNeighbours(position -> gardenMap.get(position) == plant, plot);
            handledPositions.addAll(regionPlots);
            regions.add(new Region(plant, regionPlots));
        }
        return regions;
    }

    public static Set<Position> findNeighbours(Predicate<Position> predicate,
                                               Position plot) {
        Deque<Position> toHandle = Queues.newArrayDeque();
        toHandle.add(plot);
        Set<Position> regionPlots = Sets.newHashSet();
        while (!toHandle.isEmpty()) {
            Position current = toHandle.pop();
            if (predicate.test(current) && !regionPlots.contains(current)) {
                regionPlots.add(current);

                Arrays.stream(Direction.values())
                    .map(current::move)
                    .forEach(toHandle::push);
            }
        }
        return regionPlots;
    }

    public record Region(Character plant, Set<Position> plots) {
        public long calculateFenceCountPrice(Map<Position, Character> gardenMap) {
            long fenceCount = plots.stream()
                .flatMap(plot -> Arrays.stream(Direction.values())
                    .map(plot::move)
                    .filter(neighbour -> gardenMap.get(neighbour) != plant)
                )
                .count();
            return fenceCount * plots.size();
        }

        public long calculateFenceSidePrice(Map<Position, Character> gardenMap) {
            Set<Fence> handledPositions = Sets.newHashSet();
            long fenceSideCount = 0;
            for (Position plot : plots) {
                for (Direction direction : Direction.values()) {
                    if (handledPositions.contains(new Fence(plot, direction))) {
                        continue;
                    } else {
                        handledPositions.add(new Fence(plot, direction));
                    }

                    if (hasFence(plot, direction, gardenMap)) {
                        List<Fence> fences = findNeighbours(position -> hasFence(position, direction, gardenMap), plot)
                            .stream()
                            .map(neighbour -> new Fence(neighbour, direction))
                            .toList();
                        handledPositions.addAll(fences);
                        fenceSideCount++;
                    }
                }
            }
            return fenceSideCount * plots.size();
        }

        private boolean hasFence(Position plot, Direction direction, Map<Position, Character> gardenMap) {
            return plots.contains(plot) && gardenMap.get(plot) != gardenMap.get(plot.move(direction));
        }
    }

    private record Fence(Position plot, Direction direction) {
    }
}
