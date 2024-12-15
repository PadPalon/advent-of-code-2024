package ch.neukom.advent2024.day15;

import ch.neukom.advent2024.util.data.Direction;
import ch.neukom.advent2024.util.data.Position;
import ch.neukom.advent2024.util.inputreaders.InputResourceReader;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ch.neukom.advent2024.day15.Util.*;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        Warehouse warehouse = readScaledWarehouse(reader);
        Map<Position, WarehouseElement> warehouseMap = warehouse.map();
        List<Direction> movements = readMovements(reader);

        Position robotPosition = warehouseMap
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() instanceof Robot)
            .map(Map.Entry::getKey)
            .findAny()
            .orElseThrow();

        for (Direction move : movements) {
            Position newPosition = robotPosition.move(move);
            WarehouseElement neighbour = warehouseMap.get(newPosition);
            switch (neighbour) {
                case Box box -> {
                    List<Position> boxStack = getBoxesUntilEmpty(newPosition, move, warehouse);
                    if (boxStack.isEmpty()) {
                        // do nothing
                    } else {
                        // calculate where boxes will be based on current state, and only afterwards actually move them
                        List<Runnable> boxSetters = boxStack.stream()
                            .map(boxPosition -> {
                                Box currentBox = (Box) warehouseMap.get(boxPosition);
                                Direction otherPart = currentBox.otherPart();
                                return (Runnable) () -> {
                                    warehouseMap.put(
                                        boxPosition.move(move),
                                        new Box(otherPart)
                                    );
                                };
                            })
                            .toList();
                        List<Runnable> emptySetters = boxStack.stream()
                            .map(boxPosition -> (Runnable) () -> warehouseMap.put(
                                boxPosition,
                                new Empty()
                            ))
                            .toList();
                        emptySetters.forEach(Runnable::run);
                        boxSetters.forEach(Runnable::run);

                        warehouseMap.put(newPosition, new Robot());
                        warehouseMap.put(robotPosition, new Empty());
                        robotPosition = newPosition;
                    }
                }
                case Empty empty -> {
                    warehouseMap.put(newPosition, new Robot());
                    warehouseMap.put(robotPosition, new Empty());
                    robotPosition = newPosition;
                }
                case Robot robot -> throw new IllegalStateException();
                case Wall wall -> {
                    // do nothing
                }
            }
        }

        long gpsSum = warehouse.calculateGpsSum();
        System.out.printf("The sum of gps coordinates is %s", gpsSum);
    }

    private static List<Position> getBoxesUntilEmpty(Position position,
                                                     Direction move,
                                                     Warehouse warehouse) {
        List<Position> stack = Lists.newArrayList();
        Set<Position> handledPositions = Sets.newHashSet();
        Deque<Position> toHandle = Queues.newArrayDeque();
        toHandle.push(position);

        while (!toHandle.isEmpty()) {
            Position currentPosition = toHandle.pop();
            if (handledPositions.contains(currentPosition)) {
                continue;
            } else {
                handledPositions.add(currentPosition);
            }
            if (currentPosition.isInside(warehouse.width(), warehouse.height())) {
                WarehouseElement currentElement = warehouse.map().get(currentPosition);
                if (currentElement instanceof Box box) {
                    stack.add(currentPosition);
                    toHandle.push(currentPosition.move(move));
                    toHandle.push(currentPosition.move(box.otherPart()));
                } else if (currentElement instanceof Wall) {
                    return List.of();
                }
            } else {
                return List.of();
            }
        }
        return stack;
    }
}
