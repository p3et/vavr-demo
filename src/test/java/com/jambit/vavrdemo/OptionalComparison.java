package com.jambit.vavrdemo;

import io.vavr.control.Option;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class OptionalComparison {

    @Test
    public void correctNone() {

        final var vavrOption = Option.of("foo")
                                     .filter(s -> false);

        assertTrue(vavrOption.isEmpty());

        final var javaOptional = Optional.of("foo")
                                         .filter(s -> false);

        assertTrue(javaOptional.isEmpty());
    }

    @Test
    public void incorrectNone() {

        final var vavrOption = Option.of("foo")
                                     .map(f -> (String) null);

        assertFalse(vavrOption.isEmpty());
        assertNull(vavrOption.get());

        final var javaOptional = Optional.of("foo")
                                         .map(f -> (String) null);

        assertTrue(javaOptional.isEmpty());
    }

}
