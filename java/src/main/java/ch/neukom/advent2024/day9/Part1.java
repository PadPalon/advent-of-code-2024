package ch.neukom.advent2024.day9;

import ch.neukom.advent2024.util.inputreaders.InputResourceReader;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class, "test")) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        String fileSystemString = reader.getFirstLine();
        List<Block> initialFileSystem = Lists.newArrayList();
        boolean isFile = true;
        long fileIndex = 0;
        for (char block : fileSystemString.toCharArray()) {
            long size = Long.parseLong(String.valueOf(block));
            if (isFile) {
                initialFileSystem.add(new Block(size, fileIndex));
                fileIndex++;
            } else {
                initialFileSystem.add(new Block(size, -1));
            }
            isFile = !isFile;
        }

        long index = 0;
        while (!isDefragged(initialFileSystem)) {
            System.out.println(index++);
            Block movingBlock = null;
            for (int i = initialFileSystem.size() - 1; i > 0; i--) {
                Block block = initialFileSystem.get(i);
                if (block.file() >= 0) {
                    movingBlock = block;
                    break;
                }
            }
            int nonEmptyIndex = initialFileSystem.indexOf(movingBlock);
            initialFileSystem.remove(nonEmptyIndex);

            Block emptyBlock = initialFileSystem.stream()
                .filter(b -> b.file() < 0)
                .findFirst()
                .orElseThrow();
            int emptyIndex = initialFileSystem.indexOf(emptyBlock);

            long spaceDifference = movingBlock.space() - emptyBlock.space();
            if (spaceDifference > 0) {
                initialFileSystem.remove(emptyIndex);
                initialFileSystem.add(emptyIndex, new Block(emptyBlock.space(), movingBlock.file()));
                initialFileSystem.add(nonEmptyIndex, new Block(spaceDifference, movingBlock.file()));
                initialFileSystem.add(nonEmptyIndex + 1, new Block(emptyBlock.space(), -1));
            } else if (spaceDifference < 0) {
                initialFileSystem.remove(emptyIndex);
                initialFileSystem.add(emptyIndex, new Block(movingBlock.space(), movingBlock.file()));
                initialFileSystem.add(emptyIndex + 1, new Block(Math.abs(spaceDifference), -1));
                initialFileSystem.add(new Block(movingBlock.space(), -1));
            } else {
                initialFileSystem.remove(emptyIndex);
                initialFileSystem.add(emptyIndex, new Block(movingBlock.space(), movingBlock.file()));
                initialFileSystem.add(new Block(emptyBlock.space(), -1));
            }
        }

//        printFileSystem(initialFileSystem);
        long checksum = calculateChecksum(initialFileSystem);
        System.out.printf("The checksum of the defragged filesystem is %s", checksum);
    }

    private static void printFileSystem(List<Block> initialFileSystem) {
        for (Block block : initialFileSystem) {
            String value = block.file() >= 0 ? String.valueOf(block.file()) : ".";
            System.out.print(value.repeat((int) block.space()));
        }
        System.out.println();
    }

    private static long calculateChecksum(List<Block> fileSystem) {
        AtomicLong checksum = new AtomicLong(0);
        AtomicLong index = new AtomicLong(0);
        for (Block block : fileSystem) {
            LongStream.rangeClosed(0, block.space() - 1)
                .map(v -> block.file())
                .forEach(file -> {
                    long i = index.getAndIncrement();
                    if (file >= 0) {
                        checksum.addAndGet(i * file);
                    }
                });
        }
        return checksum.get();
    }

    private static boolean isDefragged(List<Block> fileSystem) {
        List<Long> files = fileSystem.stream()
            .map(Block::file)
            .toList();
        int lastFileIndex = -1;
        for (int i = files.size() - 1; i > 0; i--) {
            Long file = files.get(i);
            if (file >= 0) {
                lastFileIndex = i;
                break;
            }
        }
        return lastFileIndex < files.indexOf(-1L);
    }

    private record Block(long space, long file) {
        @Override
        public boolean equals(Object other) {
            return this == other;
        }
    }
}
