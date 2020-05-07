package ru.myitschool.dcrawler.utils.functional;

import ru.myitschool.dcrawler.utils.functional.KeepingFunction;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class VoidFunction<T> implements KeepingFunction<T, Void> {

    Consumer<T> consumer;

    public VoidFunction(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    @Override
    public Supplier<Void> apply(T t) {
        consumer.accept(t);
        return null;
    }
}
