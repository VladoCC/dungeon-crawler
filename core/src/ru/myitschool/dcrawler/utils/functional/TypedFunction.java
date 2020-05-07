package ru.myitschool.dcrawler.utils.functional;

import java.util.function.Function;
import java.util.function.Supplier;

public class TypedFunction<T, R> implements KeepingFunction<T, R> {

    Function<T, R> function;

    public TypedFunction(Function<T, R> function) {
        this.function = function;
    }

    @Override
    public Supplier<R> apply(T t) {
        return () -> function.apply(t);
    }
}
