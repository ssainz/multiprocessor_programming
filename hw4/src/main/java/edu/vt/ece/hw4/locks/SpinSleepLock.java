package edu.vt.ece.hw4.locks;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SpinSleepLock implements Lock {
    AtomicReference<QNode> queue;
    ThreadLocal<QNode> myNode;
    public int maxSpin = 0;

    public SpinSleepLock(int capacity, int maxSpin) {
        queue = new AtomicReference<>(null);
        // initialize thread-local variable
        myNode = ThreadLocal.withInitial(() -> new QNode());
        this.maxSpin = maxSpin;
    }



    @Override
    public void lock() {
        QNode qnode = myNode.get();
        QNode pred = queue.getAndSet(qnode);
        if (pred != null) {
            qnode.locked = true;
            pred.next = qnode;
            while (qnode.locked) {
            }     // spin
        }
    }

    @Override
    public void unlock() {
        QNode qnode = myNode.get();
        if (qnode.next == null) {
            if (queue.compareAndSet(qnode, null))
                return;
            while (qnode.next == null) {
            } // spin
        }
        qnode.next.locked = false;
        qnode.next = null;

    }

    @Override
    public boolean trylock() {
        return false;
    }

    static class QNode {     // Queue node inner class
        volatile boolean locked = false;
        volatile QNode next = null;
    }

}
