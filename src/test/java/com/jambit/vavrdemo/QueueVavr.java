package com.jambit.vavrdemo;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Queue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QueueVavr {

    @Test
    public void test() {
        final Queue<List<String>> queue = Queue.of(List.of("alice@jambit.com", "bob@jambit.com"),
                                                   List.of("carol@jambit.com", "dave@jambit.com", "eve@jambit.com"),
                                                   List.of("frank@jambit.com"));

        final List<String> expectedOrder = List.of("alice@jambit.com",
                                                   "carol@jambit.com",
                                                   "frank@jambit.com",
                                                   "bob@jambit.com",
                                                   "dave@jambit.com",
                                                   "eve@jambit.com");

        final List<String> actualOrder = getNotificationOrder(queue);

        assertEquals(expectedOrder, actualOrder);
    }

    private List<String> getNotificationOrder(final Queue<List<String>> queue) {
        final Tuple2<List<String>, Queue<List<String>>> currentRemainingQueuePair = queue.dequeue();

        final List<String> current = currentRemainingQueuePair._1;
        final Queue<List<String>> remainingQueue = currentRemainingQueuePair._2;

        final String head = current.head();
        final List<String> tail = current.tail();

        final Queue<List<String>> updatedQueue = tail.isEmpty() ? remainingQueue
                                                                : remainingQueue.enqueue(tail);

        return updatedQueue.isEmpty() ? List.of(head)
                                      : getNotificationOrder(updatedQueue).prepend(head);
    }


}
