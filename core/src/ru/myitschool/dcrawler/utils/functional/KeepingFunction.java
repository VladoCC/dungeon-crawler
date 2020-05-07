package ru.myitschool.dcrawler.utils.functional;

import ru.myitschool.dcrawler.ai.pathfinding.graph.NodeConnection;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Represents a function that accepts one argument and produces a supplier with result.
 * This function can work with void return type. See {@link VoidFunction}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Object)}.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 */
public interface KeepingFunction<T, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result in supplier
     */
    Supplier<R> apply(T t);
}
