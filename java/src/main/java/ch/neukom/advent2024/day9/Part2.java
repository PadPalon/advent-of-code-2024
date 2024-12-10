package ch.neukom.advent2024.day9;

import ch.neukom.advent2024.util.inputreaders.InputResourceReader;
import ch.neukom.advent2024.util.list.ListAccessHelper;
import ch.neukom.advent2024.util.streams.StreamUtil;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static ch.neukom.advent2024.day9.Util.*;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        List<Block> initialFileSystem = getInitialFileSystem(reader);

        Set<Long> handledFiles = Sets.newHashSet();
        while (true) {
            Optional<Block> movingBlockOptional = getBlockToMove(initialFileSystem, handledFiles);
            if (movingBlockOptional.isEmpty()) {
                break;
            }

            Block movingBlock = movingBlockOptional.get();
            int movingIndex = initialFileSystem.indexOf(movingBlock);
            handledFiles.add(movingBlock.file());

            // does empty block with enough space to host moving block exist left of moving block?
            Optional<Block> emptyBlockOptional = StreamUtil.filterWithIndex(
                    initialFileSystem.stream(),
                    (block, index) -> index <= movingIndex
                )
                .filter(Block::isEmpty)
                .filter(block -> block.space() >= movingBlock.space())
                .findFirst();
            if (emptyBlockOptional.isEmpty()) {
                continue;
            }

            Block emptyBlock = emptyBlockOptional.get();
            int emptyIndex = initialFileSystem.indexOf(emptyBlock);

            initialFileSystem.remove(movingIndex);
            initialFileSystem.remove(emptyIndex);
            long spaceDifference = movingBlock.space() - emptyBlock.space();
            if (spaceDifference < 0) {
                initialFileSystem.add(emptyIndex, movingBlock);
                initialFileSystem.add(emptyIndex + 1, Block.empty(Math.abs(spaceDifference)));
                initialFileSystem.add(movingIndex, Block.empty(movingBlock.space()));
            } else if (spaceDifference == 0) {
                initialFileSystem.add(emptyIndex, movingBlock);
                initialFileSystem.add(movingIndex, emptyBlock);
            }
        }

        long checksum = calculateChecksum(initialFileSystem);
        System.out.printf("The checksum of the defragged filesystem is %s", checksum);
    }

    private static Optional<Block> getBlockToMove(List<Block> initialFileSystem, Set<Long> handledFiles) {
        return ListAccessHelper.getLastMatching(initialFileSystem, block -> block.isFilled() && !handledFiles.contains(block.file()));
    }
}
