package ch.neukom.advent2024.day9;

import ch.neukom.advent2024.util.inputreaders.InputResourceReader;
import ch.neukom.advent2024.util.list.ListAccessHelper;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.LongStream;

public class Util {
    private Util() {
    }

    public static List<Block> getInitialFileSystem(InputResourceReader reader) {
        List<Block> initialFileSystem = Lists.newArrayList();
        boolean isFile = true;
        long fileIndex = 0;
        for (char block : reader.getFirstLine().toCharArray()) {
            long size = Long.parseLong(String.valueOf(block));
            if (isFile) {
                initialFileSystem.add(new Block(size, fileIndex));
                fileIndex++;
            } else {
                initialFileSystem.add(Block.empty(size));
            }
            isFile = !isFile;
        }
        return initialFileSystem;
    }

    public static long calculateChecksum(List<Block> fileSystem) {
        long checksum = 0;
        long index = 0;
        for (Block block : fileSystem) {
            if (block.isFilled()) {
                checksum += LongStream.rangeClosed(index, block.space() - 1 + index)
                    .map(i -> i * block.file())
                    .sum();
            }
            index = index + block.space();
        }
        return checksum;
    }

    public static void printFileSystem(List<Block> initialFileSystem) {
        for (Block block : initialFileSystem) {
            String value = block.isEmpty() ? "." : String.valueOf(block.file());
            System.out.print(value.repeat((int) block.space()));
        }
        System.out.println();
    }

    public static boolean isDefragged(List<Block> fileSystem) {
        Integer firstEmptyIndex = fileSystem.stream()
            .filter(Block::isEmpty)
            .findFirst()
            .map(fileSystem::indexOf)
            .orElseThrow();
        int lastFileIndex = ListAccessHelper.getLastIndexMatching(fileSystem, Block::isFilled);
        return lastFileIndex < firstEmptyIndex;
    }

    public record Block(long space, long file) {
        public static Block empty(long space) {
            return new Block(space, -1);
        }

        public boolean isEmpty() {
            return file < 0;
        }

        public boolean isFilled() {
            return file >= 0;
        }

        @Override
        public boolean equals(Object other) {
            return this == other;
        }
    }
}
