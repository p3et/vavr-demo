package com.jambit.vavrdemo;

import io.vavr.collection.Iterator;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class IteratorExample {

    @Test
    public void combineIterators() {
        final Collection<Integer> javaCollection = java.util.List.of(1, 2, 3);

        final Iterator<Integer> javaListIterator = Iterator.ofAll(javaCollection.iterator());

        final Iterator<Integer> instantIterator = Iterator.of(4, 5, 6);

        final io.vavr.collection.List<Integer> squares = javaListIterator.concat(instantIterator)
                                                                         .map(i -> i * i)
                                                                         .toList();

        assertEquals(io.vavr.collection.List.of(1, 4, 9, 16, 25, 36), squares);
    }
}
