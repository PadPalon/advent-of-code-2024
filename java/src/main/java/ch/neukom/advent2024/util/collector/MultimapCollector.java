package ch.neukom.advent2024.util.collector;

import com.google.common.collect.Multimap;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class MultimapCollector<M extends Multimap<K, V>, I, K, V> implements Collector<I, M, M> {
    private final BiConsumer<M, I> accumulator;
    private final Supplier<M> mapCreator;

    public MultimapCollector(BiConsumer<M, I> accumulator, Supplier<M> mapCreator) {
        this.accumulator = accumulator;
        this.mapCreator = mapCreator;
    }

    @Override
    public Supplier<M> supplier() {
        return mapCreator;
    }

    @Override
    public BiConsumer<M, I> accumulator() {
        return accumulator;
    }

    @Override
    public BinaryOperator<M> combiner() {
        return (left, right) -> {
            M finalMap = supplier().get();
            finalMap.putAll(left);
            finalMap.putAll(right);
            return finalMap;
        };
    }

    @Override
    public Function<M, M> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(
            Characteristics.IDENTITY_FINISH,
            Characteristics.UNORDERED
        );
    }
}
