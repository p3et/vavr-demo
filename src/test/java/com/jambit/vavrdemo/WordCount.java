package com.jambit.vavrdemo;

import org.junit.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.stream.Collectors;

@SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
public class WordCount {

    @Test
    public void benchmark() throws IOException {
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

        System.out.printf("Java: %d\tVavr: %d%n", between - before, after - between);
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


}
