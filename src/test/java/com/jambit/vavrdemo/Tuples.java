package com.jambit.vavrdemo;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Tuples {

    @Test
    public void inAction() {
        final Tuple2<String, Integer> foo = Tuple.of("foo", 1);
        final Tuple3<String, Integer, Boolean> bar = Tuple.of("bar", 2, true);

        final var result = foo.swap()
                              .concat(bar)
                              .map4(i -> i * i)
                              .append(false)
                              .update1(2);

        assertEquals(Tuple.of(2, "foo", "bar", 4, true, false), result);
    }

}
