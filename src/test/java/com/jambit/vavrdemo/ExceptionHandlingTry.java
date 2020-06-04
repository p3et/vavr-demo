package com.jambit.vavrdemo;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Stream;
import io.vavr.control.Try;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ExceptionHandlingTry {

    private Try<Integer> executeInTry(final Integer input) {
        return Try.of(() -> input)
                  .mapTry(OldLibrary::square)
                  .map(i -> i + 1)
                  .map(OldLibrary::byTwo);
    }

    @Test
    public void unobtrusiveExceptionHandling() {
        final Try<Integer> tryTwi = executeInTry(2);
        final Try<Integer> tryThree = executeInTry(3);
        final Try<Integer> tryFour = executeInTry(4);

        assertTrue(tryTwi.isSuccess());

        assertTrue(tryThree.isFailure());
        assertTrue(tryFour.isFailure());

        assertEquals(RuntimeException.class, handleExceptions(tryThree).getClass());
        assertEquals(OldLibrary.CheckedException.class, handleExceptions(tryFour).getClass());
    }

    private Throwable handleExceptions(final Try<Integer> tryN) {
        return tryN.onFailure(OldLibrary.CheckedException.class, throwable -> System.out.println("Checked exception of OldLibrary was thrown!"))
                   .onFailure(RuntimeException.class, throwable -> System.out.println("OldLibrary threw a runtime exception!"))
                   .onFailure(throwable -> System.out.println(throwable))
                   .getCause();
    }

    @Test
    public void collectionExceptions() {
        final var resultMsg = Stream.range(0, 10)
                                    .map(integer -> Tuple.of(integer, executeInTry(integer)))
                                    .map(integerTry -> integerTry.map2(t -> t.isSuccess() ? "success"
                                                                                          : t.getCause().getClass().getSimpleName()))
                                    .groupBy(Tuple2::_2)
                                    .mapValues(integerResultStream -> integerResultStream.map(Tuple2::_1)
                                                                                         .map(Objects::toString)
                                                                                         .intersperse(",")
                                                                                         .foldLeft(new StringBuilder(), StringBuilder::append)
                                                                                         .toString())
                                    .toList()
                                    .sortBy(resultIntegers -> resultIntegers._1.length())
                                    .map(resultIntegers -> resultIntegers._1 + ":" + resultIntegers._2)
                                    .intersperse("\n")
                                    .foldLeft(new StringBuilder(), StringBuilder::append)
                                    .toString();

        System.out.println(resultMsg);
    }
}
