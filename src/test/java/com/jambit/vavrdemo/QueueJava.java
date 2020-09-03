package com.jambit.vavrdemo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

public class QueueJava {

    @Test
    public void test() {
        final Queue<List<String>> queue = new LinkedList<>(List.of(List.of("alice@jambit.com", "bob@jambit.com"),
                                                                   List.of("carol@jambit.com", "dave@jambit.com", "eve@jambit.com"),
                                                                   List.of("frank@jambit.com")));

        final var expectedOrder = List.of("alice@jambit.com",
                                          "carol@jambit.com",
                                          "frank@jambit.com",
                                          "bob@jambit.com",
                                          "dave@jambit.com",
                                          "eve@jambit.com");

        final var actualOrder = getNotificationOrder(queue);

        assertEquals(expectedOrder, actualOrder);
    }

    private List<String> getNotificationOrder(final Queue<List<String>> queue) {
        final List<String> order = new ArrayList<>();

        List<String> current;
        while ((current = queue.poll()) != null) {

            final String head = current.get(0);
            order.add(head);

            if (current.size() > 1) {
                final List<String> tail = current.subList(1, current.size());
                queue.add(tail);
            }
        }

        return order;
    }


}
