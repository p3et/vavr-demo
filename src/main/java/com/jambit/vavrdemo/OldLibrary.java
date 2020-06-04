package com.jambit.vavrdemo;

public class OldLibrary {

    static Integer square(final Integer i) throws CheckedException {
        if (i % 4 == 0) {
            throw new CheckedException();
        } else {
            return i * i;
        }
    }

    static Integer byTwo(final Integer i) {
        if (i % 10 == 0) {
            throw new RuntimeException();
        } else {
            return 2 * i;
        }
    }

    static class CheckedException extends Throwable {
    }
}
