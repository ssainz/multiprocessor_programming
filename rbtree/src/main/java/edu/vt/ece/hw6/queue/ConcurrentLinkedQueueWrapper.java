package edu.vt.ece.hw6.queue;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentLinkedQueueWrapper<T> implements Queue<T> {

    private final ConcurrentLinkedQueue<T> queue = new ConcurrentLinkedQueue<>();

    @Override
    public void enq(T val) {
        queue.add(val);
    }

    @Override
    public T deq() throws EmptyException {

        T item = queue.poll();

        if(item == null){
            throw new EmptyException();
        }

        return item;
    }
}
