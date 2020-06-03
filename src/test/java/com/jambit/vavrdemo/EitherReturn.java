package com.jambit.vavrdemo;

import io.vavr.control.Either;
import org.junit.Test;

import static org.junit.Assert.*;

public class EitherReturn {

    @Test(expected = ArithmeticException.class)
    public void division() {
        final Either<String, Integer> validDivision = divide(4, 2);
        final Either<String, Integer> divisionByZero = divide(2, 0);

        assertTrue(validDivision.isRight());
        assertFalse(divisionByZero.isRight());

        assertFalse(validDivision.isEmpty());
        assertTrue(divisionByZero.isEmpty());

        assertEquals(Integer.valueOf(2), validDivision.getOrElse(0));
        assertEquals(Integer.valueOf(0), divisionByZero.getOrElse(0));

        divisionByZero.getOrElseThrow(left -> new ArithmeticException(left));
    }

    private Either<String, Integer> divide(final Integer dividend, final Integer divisor) {

        return divisor == 0 ? Either.left("Division by ZERO is not possible")
                            : Either.right(dividend / divisor);
    }
}
