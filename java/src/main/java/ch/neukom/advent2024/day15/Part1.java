package ch.neukom.advent2024.day15;

import ch.neukom.advent2024.util.data.Direction;
import ch.neukom.advent2024.util.data.Position;
import ch.neukom.advent2024.util.inputreaders.InputResourceReader;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static ch.neukom.advent2024.day15.Util.*;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        Warehouse warehouse = readWarehouse(reader);
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
                        boxStack.forEach(boxPosition -> warehouseMap.put(
                            boxPosition.move(move),
                            new Box(null)
                        ));
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
        List<Position> stack = Lists.newArrayList(position);
        Position currentPosition = position;
        while (true) {
            currentPosition = currentPosition.move(move);
            if (currentPosition.isInside(warehouse.width(), warehouse.height())) {
                WarehouseElement currentElement = warehouse.map().get(currentPosition);
                if (currentElement instanceof Box) {
                    stack.add(currentPosition);
                } else if (currentElement instanceof Empty) {
                    return stack;
                } else if (currentElement instanceof Wall) {
                    return List.of();
                }
            } else {
                return List.of();
            }
        }
    }
}
