package com.jambit.vavrdemo;

import org.junit.Test;

import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ListComparison {

    @Test
    public void mutableList() {
        final var initial = new LinkedList<>();
        initial.add(1);
        initial.add(2);
        initial.add(3);

        final var prepended = new LinkedList<>(initial);
        prepended.addFirst(0);

        // sublist creates new list object
        final var tail = prepended.subList(1, prepended.size());
        assertTrue(initial.equals(tail));
        assertFalse(initial == tail);
    }

    @Test
    public void immutableList() {
        final var initial = java.util.List.of(1, 2, 3);

        // appending Java's immutable list is not very elegant
        final var prepended = Stream.concat(Stream.of(0), initial.stream())
                                    .collect(Collectors.toUnmodifiableList());

        // sublist creates new list object
        final var tail = prepended.subList(1, prepended.size());
        assertTrue(initial.equals(tail));
        assertFalse(initial == tail);
    }

    @Test
    public void vavrList() {
        final var initial = io.vavr.collection.List.of(1, 2, 3);
        final var prepended = initial.prepend(0);

        // VAVR's provides a tail() method
        final var tail = prepended.tail();
        assertTrue(initial.equals(tail));
        assertTrue(initial == tail);
    }


}
