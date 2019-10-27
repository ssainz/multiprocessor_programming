package edu.vt.ece.hw4.barriers;

import edu.vt.ece.hw4.locks.TTASLock;

import java.util.concurrent.atomic.AtomicInteger;

public class TTASBarrier implements Barrier {
    public TTASLock lock = new TTASLock();
    public AtomicInteger counter = new AtomicInteger(0);
    public int totalThread = 0;

    public TTASBarrier(int threadNumber){
        totalThread = threadNumber;
    }

    @Override
    public void enter(int threadId) {
        lock.lock();
        counter.getAndIncrement();
        lock.unlock();

        while(counter.get() < totalThread){}
    }

    @Override
    public void reset() {
        counter.set(0);
    }
}
