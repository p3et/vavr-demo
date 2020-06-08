package com.jambit.vavrdemo;

import org.junit.Test;

import java.util.List;

import static io.vavr.API.*;
import static io.vavr.Predicates.isIn;
import static org.junit.Assert.assertEquals;

public class PatternMatching {

    @Test
    public void matchSimpleValue() {
        assertEquals("special", classifyClassic(42));
        assertEquals("less than zero", classifyClassic(-1));
        assertEquals("small prime", classifyClassic(3));
        assertEquals("other", classifyClassic(10));

        assertEquals("special", classifyByMatch(42));
        assertEquals("less than zero", classifyByMatch(-1));
        assertEquals("small prime", classifyByMatch(3));
        assertEquals("other", classifyByMatch(10));
    }

    private String classifyClassic(final Integer integer) {

        final String clazz;
        if (integer == 42) {
            clazz = "special";
        } else if (integer < 0) {
            clazz = "less than zero";
        } else if (List.of(2, 3, 5, 7).contains(integer)) {
            clazz = "small prime";
        } else {
            clazz = "other";
        }

        return clazz;
    }

    private String classifyByMatch(final Integer integer) {
        return Match(integer).of(Case($(42), "special"),
                                 Case($(i -> i < 0), "less than zero"),
                                 Case($(isIn(2, 3, 5, 7)), "small prime"),
                                 Case($(), "other"));
    }

}
