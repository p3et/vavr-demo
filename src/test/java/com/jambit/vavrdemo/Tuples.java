package com.jambit.vavrdemo;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Tuples {

    @Test
    public void mapAndApply() {
        final Tuple2<String, String> original = Tuple.of("foo", "bar");
        final Tuple2<String, String> mapped = original.map(f1 -> f1 + f1, f2 -> f2)
                                                      .map((f1, f2) -> Tuple.of(f1 + f2, "baz"));

        assertEquals(Tuple.of("foofoobar", "baz"), mapped);

        final int length = mapped.apply((f1, f2) -> f1.length() + f2.length());

        assertEquals(12, length);
    }

    @Test
    public void concatAndUpdate() {
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
