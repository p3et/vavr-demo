package com.jambit.vavrdemo;

import java.time.LocalDate;

class Transaction {

    public final LocalDate date;
    public final String type;
    public final Float amount;

    private Transaction(final LocalDate date, final String type, final Float amount) {
        this.date = date;
        this.type = type;
        this.amount = amount;
    }

    public static Transaction of(final String date, final String type, final Float amount) {
        return new Transaction(LocalDate.parse(date), type, amount);
    }
}
