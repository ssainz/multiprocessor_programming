package edu.vt.ece.hw4.barriers;

import edu.vt.ece.hw4.locks.TTASLock;

public class TTASBarrier implements Barrier {
    public TTASLock lock = new TTASLock();
    public int counter = 0;
    public int totalThread = 0;

    public TTASBarrier(int threadNumber){
        totalThread = threadNumber;
    }

    @Override
    public void enter(int threadId) {
        lock.lock();
        counter++;
        lock.unlock();

        while(counter < totalThread){}
    }

    @Override
    public void reset() {
        counter = 0;
    }
}
