package com.jambit.vavrdemo;

import io.vavr.concurrent.Future;
import org.junit.Test;

public class FunctionalFuture {

    @Test
    public void basics() throws InterruptedException {
        final Future<Integer> future = Future.of(() -> {
            Thread.sleep(2000);
            System.out.println("--- other thread --");
            return 42;
        });

        future.onSuccess(i -> System.out.println(i * 2));

        System.out.println("--- main thread ----");

        print(future);

        Thread.sleep(3000);
        System.out.println("--- main thread after waiting ---");

        print(future);

        System.out.println("-----");
    }

    @Test
    public void blocking() throws InterruptedException {
        final Future<Integer> future = Future.of(() -> {
            Thread.sleep(2000);
            System.out.println("--- other thread --");
            return 42;
        });

        future.onSuccess(i -> System.out.println(i * 2));
        future.await();
        Thread.sleep(100); // give onSuccess computation a chance to output first

        System.out.println("--- main thread ----");

        print(future);

        System.out.println("-----");
    }

    @Test
    public void failing() {
        final Future<Integer> future = Future.of(() -> {
            throw new OldLibrary.CheckedException();
        });

        future.onSuccess(i -> System.out.println(i * 2));
        future.await();

        System.out.println("--- main thread ----");

        print(future);

        System.out.println("-----");
    }

    @Test
    public void cancel() throws InterruptedException {
        final Future<Integer> future = Future.of(() -> {
            Thread.sleep(2000);
            System.out.println("--- other thread --");
            return 42;
        });

        future.onSuccess(i -> System.out.println(i * 2));

        System.out.println("--- main thread ----");

        print(future);

        Thread.sleep(1000);
        System.out.println("--- main thread after waiting ---");

        future.cancel();

        print(future);

        System.out.println("-----");
    }

    private void print(final Future<Integer> future) {
        System.out.println(future.isCompleted());
        System.out.println(future.isSuccess());
        System.out.println(future.getValue());
    }

}
