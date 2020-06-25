package com.jambit.vavrdemo;

import org.junit.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
public class CollectionBenchmark {

    @Test
    public void run() throws IOException {
        final String[] input = getInput();

        final var javaList = java.util.List.of(input);
        final var vavrList = io.vavr.collection.List.of(input);

        run(
            "map-filter-collect",

            () -> javaList.stream()
                          .map(String::trim)
                          .filter(s -> s.length() > 1)
                          .collect(Collectors.toUnmodifiableList()),

            () -> vavrList.map(String::trim)
                          .filter(s -> s.length() > 1)
        );

        run(
            "distinct",

            () -> javaList.stream()
                          .distinct()
                          .collect(Collectors.toUnmodifiableList()),

            () -> vavrList.distinct()
        );

        run(
            "group by",

            () -> javaList.stream()
                          .collect(Collectors.groupingBy(s -> s.substring(0, 1),
                                                         Collectors.toUnmodifiableList())),

            () -> vavrList.groupBy(s -> s.substring(0, 1))
        );

        run(
            "word count",

            () -> javaList.stream()
                          .collect(Collectors.groupingBy(word -> word, Collectors.counting()))
                          .entrySet()
                          .stream()
                          .sorted((a, b) -> Long.compare(a.getValue(), b.getValue()) * -1)
                          .collect(Collectors.toUnmodifiableList()),

            () -> vavrList.groupBy(word -> word)
                          .mapValues(words -> words.length())
                          .toList()
                          .sortBy(tuple2 -> tuple2._2 * -1)
        );

        run(
            "folding",

            () -> javaList.stream()
                          .reduce((a, b) -> a + " " + b)
                          .get(),

            () -> vavrList.reduce((a, b) -> a + " " + b)
        );
    }

    private void run(final String name, final Runnable java, final Runnable vavr) {
        System.out.println(String.format("%s\t%s\t%s", name, measure(java), measure(vavr)));
    }

    private long measure(final Runnable implementation) {
        final long before = Instant.now().toEpochMilli();

        IntStream.range(0, 10)
                 .forEach(i -> implementation.run());

        final long after = Instant.now().toEpochMilli();

        return after - before;
    }

    private String[] getInput() throws IOException {
        final String content = new String(getClass().getClassLoader()
                                                    .getResourceAsStream("mobydick.txt")
                                                    .readAllBytes());

        return content.split(" ");
    }

}
