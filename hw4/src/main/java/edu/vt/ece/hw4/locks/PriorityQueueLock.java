package edu.vt.ece.hw4.locks;

import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

public class PriorityQueueLock implements Lock {

    BlockingQueue<QNode> queue = new PriorityBlockingQueue<QNode>();
    AtomicReference<QNode> latestWaitingQNode = null;

    ThreadLocal<QNode> myNode;
    long MAX_MILLI = 0;

    public PriorityQueueLock(long milli){
        MAX_MILLI = milli;
        latestWaitingQNode = new AtomicReference<QNode>(null);
        myNode = new ThreadLocal<QNode>(){
            protected QNode initialValue(){
                int priority = (int) (Thread.currentThread().getId() % 5) + 1;
                return new QNode(priority);
            }
        };
    }

    @Override
    public boolean trylock() {
        long start = System.currentTimeMillis();
        QNode qnode = myNode.get();
        QNode latestWaiting = latestWaitingQNode.getAndSet(qnode);
        if(latestWaiting != null){
            qnode.locked = true;
            queue.add(qnode); // Add to the heap queue
            while(qnode.locked){
                if(System.currentTimeMillis() - start > MAX_MILLI){
                    // Mark qnode as removed.
                    qnode.isAboutToBeRemoved = true;
                    while(qnode.isAboutToEnterCS){
                        if(!qnode.locked){
                            qnode.isAboutToBeRemoved = false; // In case unlock already has set locked to false, then thread enters CS
                            return true;
                        }
                    }
                    qnode.isRemoved = true;
                    return false;
                }
            } // Wait to be awaken.
        }
        return true;
    }

    @Override
    public void lock() {
        QNode qnode = myNode.get();
        qnode.reset(); // clears status flags
        QNode latestWaiting = latestWaitingQNode.getAndSet(qnode);
        if(latestWaiting != null){
            qnode.locked = true;
            queue.add(qnode); // Add to the heap queue
            while(qnode.locked){} // Spins.
        }
    }

    @Override
    public void unlock() {

        QNode qnode = myNode.get();
        if(queue.isEmpty()){
            if(latestWaitingQNode.compareAndSet(qnode,null))
                return; // No one has come in so we say no one is in the heap !
            // Else we know that someone is in the queue or about to be in the queue.
            while(queue.isEmpty()){}
        }
        while(true){
            QNode next = queue.poll();
            //We now check if it is about to be removed and if so we backoff and let it be removed:
            next.isAboutToEnterCS = true;
            if(next.isAboutToBeRemoved){ // Yield to the removal process.
                next.isAboutToEnterCS = false;
                continue; // take next qnode as this one has been removed.
            }
            next.locked = false;
            return;
        }


    }

    static class QNode implements Comparable<QNode>{     // Queue node inner class
        public volatile boolean locked = false;
        int priority = 0;
        public boolean isRemoved = false;
        public volatile boolean isAboutToBeRemoved = false;
        public volatile boolean isAboutToEnterCS = false;

        QNode(int priority){
            this.priority = priority;
        }

        @Override
        public int compareTo(QNode o) {
            if(o.priority == this.priority)
                return 0;
            int res = this.priority < o.priority ? -1 : 1;
            return res;
        }

        public void reset(){
            locked = false;
            isRemoved = false;
            isAboutToEnterCS = false;
            isAboutToBeRemoved = false;
        }
    }

}
