package ch.neukom.advent2024.day9;

import ch.neukom.advent2024.util.inputreaders.InputResourceReader;
import ch.neukom.advent2024.util.list.ListAccessHelper;

import java.io.IOException;
import java.util.List;

import static ch.neukom.advent2024.day9.Util.*;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        List<Block> initialFileSystem = getInitialFileSystem(reader);

        while (!isDefragged(initialFileSystem)) {
            Block movingBlock = getBlockToMove(initialFileSystem);
            int movingIndex = initialFileSystem.indexOf(movingBlock);
            initialFileSystem.remove(movingIndex);

            Block emptyBlock = initialFileSystem.stream()
                .filter(Block::isEmpty)
                .findFirst()
                .orElseThrow();
            int emptyIndex = initialFileSystem.indexOf(emptyBlock);
            initialFileSystem.remove(emptyIndex);

            long spaceDifference = movingBlock.space() - emptyBlock.space();
            if (spaceDifference > 0) {
                initialFileSystem.add(emptyIndex, new Block(emptyBlock.space(), movingBlock.file()));
                initialFileSystem.add(movingIndex, new Block(spaceDifference, movingBlock.file()));
                initialFileSystem.add(movingIndex + 1, emptyBlock);
            } else if (spaceDifference < 0) {
                initialFileSystem.add(emptyIndex, movingBlock);
                initialFileSystem.add(emptyIndex + 1, Block.empty(Math.abs(spaceDifference)));
                initialFileSystem.add(Block.empty(movingBlock.space()));
            } else {
                initialFileSystem.add(emptyIndex, movingBlock);
                initialFileSystem.add(emptyBlock);
            }
        }

        long checksum = calculateChecksum(initialFileSystem);
        System.out.printf("The checksum of the defragged filesystem is %s", checksum);
    }

    private static Block getBlockToMove(List<Block> initialFileSystem) {
        return ListAccessHelper.getLastMatching(initialFileSystem, Block::isFilled)
            .orElseThrow(() -> new IllegalStateException("Could not find block to move"));
    }
}
