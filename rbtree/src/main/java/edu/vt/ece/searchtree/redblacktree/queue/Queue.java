package edu.vt.ece.searchtree.redblacktree.queue;

import java.util.concurrent.atomic.AtomicBoolean;

public interface Queue<T> {

    void enq(T val);

    T deq(AtomicBoolean alert) throws EmptyException;
}
