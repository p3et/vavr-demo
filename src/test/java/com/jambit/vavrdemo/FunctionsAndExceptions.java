package com.jambit.vavrdemo;

import io.vavr.CheckedFunction1;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FunctionsAndExceptions {

    @Test
    public void function() {
        final Function1<Integer, Integer> square = i -> {
            try {
                return OldLibrary.squareExcept4(i);
            } catch (final OldLibrary.CheckedException e) {
                return -1;
            }
        };

        final Integer result = square.apply(2);

        assertEquals(Integer.valueOf(4), result);
    }

    @Test
    public void checkedFunction() {
        final CheckedFunction1<Integer, Integer> square = i -> OldLibrary.squareExcept4(i);

        Integer result;
        try {
            result = square.apply(2);
        } catch (final Throwable throwable) {
            result = -1;
        }

        assertEquals(Integer.valueOf(4), result);
    }

    @Test
    public void checkedLifting() {
        final CheckedFunction1<Integer, Integer> square = i -> OldLibrary.squareExcept4(i);

        final Function1<Integer, Option<Integer>> safeSquare = CheckedFunction1.lift(square);

        final Option<Integer> result = safeSquare.apply(2);

        result.peek(i -> assertEquals(Integer.valueOf(4), i));
    }

    @Test
    public void uncheckedLifting() {
        final Function2<Integer, Integer, Integer> divide = (i, j) -> i / j;

        final Function2<Integer, Integer, Option<Integer>> safeDivide = Function2.lift(divide);

        final Option<Integer> result = safeDivide.apply(2, 0);

        assertTrue(result.isEmpty());
    }

    @Test
    public void tryLifting() {
        final Function2<Integer, Integer, Integer> divide = (i, j) -> i / j;

        final Function2<Integer, Integer, Try<Integer>> safeDivide = Function2.liftTry(divide);

        final Try<Integer> result = safeDivide.apply(2, 0);

        assertTrue(result.isFailure());
    }
}
