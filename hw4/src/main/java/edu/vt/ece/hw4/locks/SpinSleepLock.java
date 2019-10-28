package edu.vt.ece.hw4.locks;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SpinSleepLock implements Lock {
    AtomicReference<QNode> queue;
    ThreadLocal<QNode> myNode;
    public int maxSpin = 0;
    public AtomicInteger numberOfNodes ;

    public SpinSleepLock(int capacity, int maxSpin) {
        queue = new AtomicReference<>(null);
        numberOfNodes = new AtomicInteger(0);
        // initialize thread-local variable
        myNode = ThreadLocal.withInitial(() -> new QNode());
        this.maxSpin = maxSpin;
    }



    @Override
    public void lock() {
        QNode qnode = myNode.get();
        qnode.myThread = Thread.currentThread();
        QNode pred = queue.getAndSet(qnode);
        int nodeNumber = numberOfNodes.getAndIncrement();

        if(nodeNumber > maxSpin){
            synchronized (qnode.myThread ){
                try {
                    qnode.myThread.wait();
                } catch (InterruptedException e) {
                    // Interrupt.
                }
            }
        }

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

        //Awake other threads ahead:
        int counter = 0;
        QNode iter = qnode.next;
        for(int i = 0 ; i < maxSpin ; i++){

            if(iter == null){
                break;
            }

            if(iter.myThread.getState() == Thread.State.WAITING){
                synchronized (iter.myThread){
                    iter.myThread.notifyAll();
                }

            }
            iter = iter.next;
        }


        numberOfNodes.getAndDecrement();
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
        volatile Thread myThread = null;
    }

}
