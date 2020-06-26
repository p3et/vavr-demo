package com.jambit.vavrdemo;

import org.junit.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
public class CollectionBenchmark {

    @Test
    public void wordCount() throws IOException {
        final String content = new String(getClass().getClassLoader()
                                                    .getResourceAsStream("mobydick.txt")
                                                    .readAllBytes());

        final String[] words = content.split(" ");

        final long before = Instant.now().toEpochMilli();

        final var javaResult = java.util.List
            .of(words)
            .stream()
            .collect(Collectors.groupingBy(word -> word,
                                           Collectors.toUnmodifiableList())) // Collectors.counting was not used by intention cause it is a special case
            .entrySet()
            .stream()
            .map(entry -> new Pair<>(entry.getKey(),
                                     entry.getValue().size()))
            .filter(pair -> pair.value > 1000)
            .sorted((a, b) -> Long.compare(a.value, b.value) * -1)
            .collect(Collectors.toUnmodifiableList());

        final long between = Instant.now().toEpochMilli();

        final var vavrResult = io.vavr.collection.List
            .of(words)
            .groupBy(word -> word)
            .mapValues(wordStream -> wordStream.length())
            .filter(tuple2 -> tuple2._2 > 1000)
            .toList()
            .sortBy(tuple2 -> tuple2._2 * -1);

        final long after = Instant.now().toEpochMilli();

        System.out.println(String.format("Java: %d\tVavr: %d", between - before, after - between));
        System.out.println(javaResult);
        System.out.println(vavrResult);
    }

    private static class Pair<K, V> {
        public final K key;
        public final V value;

        public Pair(final K key, final V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "(" + key + ", " + value + ")";
        }
    }

    @Test
    public void run() {
        final int[] intArray = IntStream.range(0, 1_000_000).toArray();

        final java.util.List<Integer> javaList = IntStream.of(intArray)
                                                          .boxed()
                                                          .collect(Collectors.toUnmodifiableList());

        final io.vavr.collection.List<Integer> vavrList = io.vavr.collection.List.ofAll(intArray);

        run(
            "map-filter-collect",

            () -> IntStream.of(intArray)
                           .map(i -> i + 1)
                           .filter(i -> i % 2 == 0)
                           .toArray(),

            () -> javaList.stream()
                          .map(i -> i + 1)
                          .filter(i -> i % 2 == 0)
                          .collect(Collectors.toUnmodifiableList()),

            () -> vavrList.map(i -> i + 1)
                          .filter(s -> s % 2 == 0)
        );

        run(
            "order by",

            () -> IntStream.of(intArray)
                           .boxed()
                           .sorted(Comparator.comparingInt(i -> i % 10))
                           .mapToInt(i -> i)
                           .toArray(),

            () -> javaList.stream()
                          .sorted(Comparator.comparingInt(i -> i % 10))
                          .collect(Collectors.toUnmodifiableList()),

            () -> vavrList.sortBy(i -> i % 10)
        );

        run(
            "distinct",

            () -> IntStream.of(intArray)
                           .map(i -> i % 2 == 0 ? i : i + 1)
                           .distinct()
                           .toArray(),

            () -> javaList.stream()
                          .map(i -> i % 2 == 0 ? i : i + 1)
                          .distinct()
                          .collect(Collectors.toUnmodifiableList()),

            () -> vavrList.map(i -> i % 2 == 0 ? i : i + 1)
                          .distinct()
        );

        run(
            "group by",

            () -> IntStream.of(intArray)
                           .boxed()
                           .collect(Collectors.groupingBy(i -> i % 10,
                                                          Collectors.toUnmodifiableList())),

            () -> javaList.stream()
                          .collect(Collectors.groupingBy(i -> i % 10,
                                                         Collectors.toUnmodifiableList())),

            () -> vavrList.groupBy(i -> i % 10)
        );

        run(
            "folding",

            () -> IntStream.of(intArray)
                           .reduce((a, b) -> a + b),

            () -> javaList.stream()
                          .reduce((a, b) -> a + b)
                          .get(),

            () -> vavrList.reduce((a, b) -> a + b) // analog zu () -> vavrList.fold(0, (a, b) -> a + b)
        );
    }

    private void run(final String name, final Runnable primitive, final Runnable java, final Runnable vavr) {
        System.out.println(String.format("%s\t%d\t%d\t%d", name, measure(primitive), measure(java), measure(vavr)));
    }

    private long measure(final Runnable implementation) {
        final long before = Instant.now().toEpochMilli();

        IntStream.range(0, 10)
                 .forEach(i -> implementation.run());

        final long after = Instant.now().toEpochMilli();

        return after - before;
    }


}
