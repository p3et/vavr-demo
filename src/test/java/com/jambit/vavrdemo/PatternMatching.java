package com.jambit.vavrdemo;

import org.junit.Test;

import static io.vavr.API.*;
import static org.junit.Assert.assertEquals;

public class PatternMatching {

    @Test
    public void matchSimpleValue() {
        assertEquals("green", getDisplayColorClassic(Transaction.of("2020-01-01", "cash", 100.00f)));
        assertEquals("red", getDisplayColorClassic(Transaction.of("2020-01-01", "cash", -50.0f)));
        assertEquals("black", getDisplayColorClassic(Transaction.of("2020-01-01", "cash", 0f)));

        assertEquals("green", getDisplayColor(Transaction.of("2020-01-01", "cash", 100.00f)));
        assertEquals("red", getDisplayColor(Transaction.of("2020-01-01", "cash", -50.0f)));
        assertEquals("black", getDisplayColor(Transaction.of("2020-01-01", "cash", 0f)));
    }

    private String getDisplayColorClassic(final Transaction transaction) {
        final Float amount = transaction.amount;

        final String color;
        if (amount == 0) {
            color = "black";
        } else if (amount < 0) {
            color = "red";
        } else {
            color = "green";
        }

        return color;
    }

    private String getDisplayColor(final Transaction transaction) {
        return Match(transaction.amount).of(Case($(0f), "black"),
                                            Case($(f -> f < 0), "red"),
                                            Case($(), "green"));
    }

//    @Test
//    public void customMatcher() {
//        assertEquals("FirstOfMonth", classify(Transaction.of("2020-01-01", "cash", 100.00f)));
//        assertEquals("InWinter", classify(Transaction.of("2020-01-15", "cash", 100.00f)));
//        assertEquals("Other", classify(Transaction.of("2020-06-01", "cash", 100.00f)));
//    }
//
//    private String classify(final Transaction transaction) {
//        return Match(transaction).of(Case($Day($(2020)), "FirstOfMonth"),
//                                     Case($(), "Other"));
//    }


}
