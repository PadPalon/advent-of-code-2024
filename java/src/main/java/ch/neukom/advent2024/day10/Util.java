package ch.neukom.advent2024.day10;

import ch.neukom.advent2024.util.data.Direction;
import ch.neukom.advent2024.util.data.Position;
import com.google.common.collect.Lists;

import java.util.*;
import java.util.stream.Collectors;

public class Util {
    private Util() {
    }

    public static class TrailFinder {
        private final Position position;
        private final Map<Position, Integer> topoMap;
        private final int width;
        private final int height;

        private final List<Position> visitedPositions = Lists.newArrayList();

        public TrailFinder(Position position,
                           Map<Position, Integer> topoMap,
                           int width,
                           int height) {
            this.position = position;
            this.topoMap = topoMap;
            this.width = width;
            this.height = height;
            this.visitedPositions.add(position);
        }

        public TrailFinder(Position position,
                           Map<Position, Integer> topoMap,
                           int width,
                           int height,
                           List<Position> visitedPositions) {
            this(position, topoMap, width, height);
            this.visitedPositions.addAll(visitedPositions);
        }

        public Set<List<Position>> findTrails() {
            Integer currentElevation = topoMap.get(position);
            if (currentElevation == 9) {
                return Set.of(visitedPositions);
            }
            return Arrays.stream(Direction.values())
                .map(position::move)
                .filter(p1 -> p1.isInside(width, height))
                .filter(p1 -> !visitedPositions.contains(p1))
                .filter(p1 -> topoMap.get(p1) == currentElevation + 1)
                .map(this::getNextFinder)
                .map(TrailFinder::findTrails)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        }

        private TrailFinder getNextFinder(Position nextPosition) {
            return new TrailFinder(nextPosition, topoMap, width, height, visitedPositions);
        }
    }
}
