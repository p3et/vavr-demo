package com.jambit.vavrdemo;

import io.vavr.Lazy;
import io.vavr.collection.Stream;
import org.junit.Test;

import java.util.Iterator;
import java.util.function.Supplier;

import static org.junit.Assert.*;

public class LazyEvaluation {

    @Test
    public void supplierVsLazy() {
        // VAVR-Stream = lazily linked list
        final Stream<Integer> infiniteStream = Stream.from(1)
                                                     .filter(i -> i % 2 == 0);

        final Iterator<Integer> infiniteIterator = infiniteStream.iterator();

        final Supplier<Integer> supplier = () -> infiniteIterator.next();

        assertEquals(Integer.valueOf(2), supplier.get());
        assertEquals(Integer.valueOf(4), supplier.get());

        final Lazy<Integer> lazy = Lazy.of(supplier);

        assertFalse(lazy.isEvaluated());
        assertEquals(Integer.valueOf(6), lazy.get());

        assertTrue(lazy.isEvaluated());
        assertEquals(Integer.valueOf(6), lazy.get());
    }
}
