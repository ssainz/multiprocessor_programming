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

        //System.out.println(String.format("lock:[%s]a",qnode.myThread));

        if (pred != null) {
            qnode.locked = true;
            pred.next = qnode;

            //System.out.println(String.format("lock:[%s]b",qnode.myThread));
            if(nodeNumber > maxSpin){
                synchronized (qnode.myThread ){
                    try {
                        qnode.myThread.wait();
                    } catch (InterruptedException e) {
                        // Interrupt.
                    }
                }
            }
            //System.out.println(String.format("lock:[%s]c",qnode.myThread));
            while (qnode.locked) {
            }     // spin
            //System.out.println(String.format("lock:[%s]d",qnode.myThread));
        }
    }

    @Override
    public void unlock() {
        numberOfNodes.getAndDecrement();
        QNode qnode = myNode.get();
        //System.out.println(String.format("unlock:[%s]a",qnode.myThread));
        if (qnode.next == null) {
            if (queue.compareAndSet(qnode, null))
                return;
            while (qnode.next == null) {
            } // spin
        }
        //System.out.println(String.format("unlock:[%s]b",qnode.myThread));
        //Awake other threads ahead:
        int counter = 0;
        QNode iter = qnode.next;
        for(int i = 0 ; i < maxSpin ; i++){

            if(iter == null){
                break;
            }
            //System.out.println(String.format("unlock:[%s]awakes[%s]c",qnode.myThread,iter.myThread));
            if(iter.myThread.getState() == Thread.State.WAITING){
                synchronized (iter.myThread){
                    iter.myThread.notifyAll();
                }

            }
            iter = iter.next;
        }

        // Potential problem if the next node is WAITING but it did not set as WAITING up until now. Say its pred is set but has not called WAITING...
        // So far that has not happened :P

        //System.out.println(String.format("unlock:[%s]d:sets node[%s].locked as true",qnode.myThread, qnode.next.myThread));

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
