package edu.vt.ece.hw6.queue;

public interface Queue<T> {

    void enq(T val);

    T deq() throws EmptyException;

    long size();
}
