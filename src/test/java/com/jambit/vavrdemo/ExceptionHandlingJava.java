package com.jambit.vavrdemo;


import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ExceptionHandlingJava {

    private Integer execute(final Integer input) throws OldLibrary.CheckedException {

        final Integer squared = OldLibrary.square(input);
        final Integer oneAdded = squared + 1;
        final Integer byTwo = OldLibrary.byTwo(oneAdded);

        return byTwo;
    }

    @Test
    public void collectionExceptions() {
        final var resultMsg = IntStream.range(0, 10)
                                       .mapToObj(integer -> {
                                           try {
                                               execute(integer);
                                               return new Pair<>("success", integer);
                                           } catch (final Exception | OldLibrary.CheckedException e) {
                                               return new Pair<>(e.getClass().getSimpleName(), integer);
                                           }
                                       })
                                       .map(pair -> new Pair<>(pair.left, List.of(pair.right)))
                                       .collect(Collectors.toUnmodifiableMap(pair -> pair.left,
                                                                             pair -> pair.right,
                                                                             (l1, l2) -> Stream.concat(l1.stream(), l2.stream())
                                                                                               .collect(Collectors.toList())))
                                       .entrySet()
                                       .stream()
                                       .sorted(Comparator.comparingInt(entry -> entry.getKey().length()))
                                       .map(entry -> entry.getKey() + "," + entry.getValue()
                                                                                 .stream()
                                                                                 .map(Objects::toString)
                                                                                 .collect(Collectors.joining(",")))
                                       .collect(Collectors.joining("\n"));

        System.out.println(resultMsg);
    }

    private static class Pair<L, R> {
        public final L left;
        public final R right;

        private Pair(final L left, final R right) {
            this.left = left;
            this.right = right;
        }
    }
}
