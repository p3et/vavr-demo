package com.jambit.vavrdemo;

import io.vavr.Function0;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.collection.Stream;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class FunctionsAdvanced {

    /**
     * Partially copied from https://www.vavr.io/vavr-docs/#_composition
     */
    @Test
    public void compositon() {
        final Function1<Integer, Integer> plusOne = a -> a + 1;
        final Function1<Integer, Integer> multiplyByTwo = a -> a * 2;

        final Function1<Integer, Integer> add1AndMultiplyBy2 = plusOne.andThen(multiplyByTwo);

        assertEquals(Integer.valueOf(6), add1AndMultiplyBy2.apply(2));
    }

    @Test
    public void partialApplication() {
        final Function3<Integer, Integer, Integer, Integer> multiplyAndAdd = (a, b, c) -> a * b + c;

        final Function2<Integer, Integer, Integer> by2AndAdd = multiplyAndAdd.apply(2);
        final Function1<Integer, Integer> addTo6 = multiplyAndAdd.apply(2, 3);

        Stream.of(multiplyAndAdd.apply(2, 3, 4),
                  multiplyAndAdd.apply(2).apply(3).apply(4),
                  by2AndAdd.apply(3, 4),
                  addTo6.apply(4))
              .forEach(i -> assertEquals(Integer.valueOf(10), i));
    }

    @Test
    public void currying() {
        final Function3<Integer, Integer, Integer, Integer> multiplyAndAdd = (a, b, c) -> a * b + c;

        final Function1<Integer, Function1<Integer, Function1<Integer, Integer>>> curried = multiplyAndAdd.curried();

        final Function1<Integer, Function1<Integer, Integer>> by2AndAdd = curried.apply(2);
        final Function1<Integer, Integer> addTo6 = curried.apply(2).apply(3);

        Stream.of(multiplyAndAdd.apply(2, 3, 4),
                  curried.apply(2).apply(3).apply(4),
                  by2AndAdd.apply(3).apply(4),
                  addTo6.apply(4))
              .forEach(i -> assertEquals(Integer.valueOf(10), i));
    }

    @Test
    public void memoization() {
        final Function0<Double> supplier = () -> Math.random();
        final Function0<Double> memoized = supplier.memoized();

        assertNotEquals(supplier.apply(), supplier.apply());
        assertEquals(memoized.apply(), memoized.apply());
    }

    @Test
    public void dontCalculateTwice() {
        final Function1<Integer, Double> function = i -> i * Math.random();
        final Function1<Integer, Double> memoized = function.memoized();

        assertNotEquals(function.apply(2), function.apply(2));
        assertNotEquals(memoized.apply(2), memoized.apply(3));
        assertEquals(memoized.apply(2), memoized.apply(2));
    }
}
