package com.jambit.vavrdemo;

import io.vavr.control.Option;
import org.junit.Test;

import java.util.Optional;
import java.util.function.Function;

import static org.junit.Assert.*;

public class OptionalComparison {

    @Test
    public void flatMap() {
        final Option<Float> vavr = Option.of(0)
                                         .flatMap(i -> i == 0 ? Option.none()
                                                              : Option.of((float) 1 / i));

        final Optional<Float> java = Optional.of(0)
                                             .flatMap(i -> i == 0 ? Optional.empty()
                                                                  : Optional.of((float) 1 / i));

        assertTrue(vavr.isEmpty());
        assertTrue(vavr instanceof Option.None);

        assertTrue(java.isEmpty());
    }

    @Test
    public void map() {
        final Function<Integer, Float> divideOneByNumber = i -> i == 0 ? null
                                                                       : (float) 1 / i;

        final Option<Float> vavr = Option.of(0)
                                         .map(divideOneByNumber);

        final Optional<Float> java = Optional.of(0)
                                             .map(divideOneByNumber);

        assertFalse(vavr.isEmpty());
        assertTrue(vavr instanceof Option.Some);
        vavr.peek(i -> assertNull(i));

        assertTrue(java.isEmpty());
    }
}
