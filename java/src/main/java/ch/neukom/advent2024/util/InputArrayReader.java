package ch.neukom.advent2024.util;

import com.google.common.collect.Streams;

import java.lang.reflect.Array;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class InputArrayReader extends InputResourceReader {
    public InputArrayReader(Class<?> clazz) {
        super(clazz);
    }

    public InputArrayReader(Class<?> clazz, String filename) {
        super(clazz, filename);
    }

    public <T> T[][] readIntoArray(Function<Symbol, T> creator, Class<T> type) {
        return buildArray(creator, type, this::readInput, (int) getLineCount(), getFirstLine().length());
    }

    public static <T> T[][] buildArray(Function<Symbol, T> creator, Class<T> type,
                                       Supplier<Stream<String>> inputSupplier,
                                       int lineCount,
                                       int columnCount) {
        T[][] array = (T[][]) Array.newInstance(type, lineCount, columnCount);
        Streams.mapWithIndex(
                inputSupplier.get(),
                (line, lineIndex) -> Streams.mapWithIndex(
                    line.chars(),
                    (symbol, columnIndex) -> new Symbol((char) symbol, (int) lineIndex, (int) columnIndex)
                )
            )
            .flatMap(i -> i)
            .forEach(symbol -> array[symbol.lineIndex()][symbol.columnIndex()] = creator.apply(symbol));
        return array;
    }

    public record Symbol(char symbol, int lineIndex, int columnIndex) {
    }
}
