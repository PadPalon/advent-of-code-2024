package ch.neukom.advent2024.util.inputreaders;

import java.io.*;
import java.util.stream.Stream;

public class InputResourceReader implements Closeable, AutoCloseable {
    public static final String DEFAULT_FILE = "input";

    private final Class<?> clazz;
    private final String filename;

    private BufferedReader reader;

    public InputResourceReader(Class<?> clazz) {
        this(clazz, DEFAULT_FILE);
    }

    public InputResourceReader(Class<?> clazz, String filename) {
        this.clazz = clazz;
        this.filename = filename;
    }

    public Stream<String> readInput() {
        return readInput(filename);
    }

    public Stream<String> readInput(String filename) {
        openReader(filename);

        if (reader == null) {
            return Stream.empty();
        } else {
            return reader.lines();
        }
    }

    public String getFirstLine() {
        return getFirstLine(filename);
    }

    public String getFirstLine(String filename) {
        return readInput(filename).findFirst().orElseThrow();
    }

    public long getLineCount() {
        return getLineCount(filename);
    }

    public long getLineCount(String filename) {
        return readInput(filename).count();
    }

    private void openReader(String filename) {
        if (reader != null) {
            try {
                close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        InputStream inputStream = clazz.getResourceAsStream(filename);
        if (inputStream == null) {
            System.err.println("Could not read input");
        } else {
            reader = new BufferedReader(new InputStreamReader(inputStream));
        }
    }

    @Override
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
    }
}
