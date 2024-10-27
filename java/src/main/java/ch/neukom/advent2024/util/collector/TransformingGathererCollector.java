package ch.neukom.advent2024.util.collector;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class TransformingGathererCollector<I, M, O> implements Collector<I, List<M>, O> {
    private final Supplier<O> initialSupplier;
    private final Function<I, M> transformer;
    private final BiConsumer<O, M> gatherer;

    public TransformingGathererCollector(Supplier<O> initialSupplier, Function<I, M> transformer, BiConsumer<O, M> gatherer) {
        this.initialSupplier = initialSupplier;
        this.transformer = transformer;
        this.gatherer = gatherer;
    }

    public static <I, M, O> TransformingGathererCollector<I, M, O> transformAndGather(
        Supplier<O> initialSupplier,
        Function<I, M> transformer,
        BiConsumer<O, M> gatherer
    ) {
        return new TransformingGathererCollector<>(initialSupplier, transformer, gatherer);
    }

    @Override
    public Supplier<List<M>> supplier() {
        return Lists::newArrayList;
    }

    @Override
    public BiConsumer<List<M>, I> accumulator() {
        return (intermediateValues, input) -> intermediateValues.add(transformer.apply(input));
    }

    @Override
    public BinaryOperator<List<M>> combiner() {
        return (left, right) -> {
            ArrayList<M> combined = Lists.newArrayList();
            combined.addAll(left);
            combined.addAll(right);
            return combined;
        };
    }

    @Override
    public Function<List<M>, O> finisher() {
        return intermediateValues -> {
            O output = initialSupplier.get();
            intermediateValues.forEach(intermediateValue -> gatherer.accept(output, intermediateValue));
            return output;
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.UNORDERED, Characteristics.CONCURRENT);
    }
}
