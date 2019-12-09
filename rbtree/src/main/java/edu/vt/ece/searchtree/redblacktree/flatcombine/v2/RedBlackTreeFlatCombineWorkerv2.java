package edu.vt.ece.searchtree.redblacktree.flatcombine.v2;

import edu.vt.ece.searchtree.redblacktree.queue.EmptyException;
import edu.vt.ece.searchtree.redblacktree.queue.Queue;
import edu.vt.ece.searchtree.redblacktree.flatcombine.v2.RedBlackTreeFlatCombinev2.Operator;
import edu.vt.ece.searchtree.redblacktree.flatcombine.v2.RedBlackTreeFlatCombinev2.ThreadStatus;

public class RedBlackTreeFlatCombineWorkerv2<Key extends Comparable<Key>, Value> extends Thread {

    RedBlackTreeFlatCombinev2 tree = null;

    RedBlackTreeFlatCombineWorkerv2(RedBlackTreeFlatCombinev2<Key, Value> tree){
        this.tree = tree;
    }

    public void run(){

        Queue<Operator> queue = tree.operationsQueue;

        while(true){
            try {

                Operator operator = queue.deq(tree.timeToFinish);
                if(operator == null) return; // is the end

                //System.out.println(String.format("Thread[%s] gets operator %s", ThreadID.get(), operator.operationType));
                switch(operator.operationType){
                    case PUT:

                        while(tree.numberOfPendingGet.get() > 0); // Wait for pending get to finish.

                        operator.res = tree.putV2_util(operator.key, operator.value);

                        //System.out.println(String.format("Thread[%s] wakes up %s, PUT", ThreadID.get(), operator.pid));

                        wakeUp(operator);

                        break;
                    case DELETE:

                        while(tree.numberOfPendingGet.get() > 0); // Wait for pending get to finish.



                        operator.res = tree.delete_util(operator.key);

                        //System.out.println(String.format("Thread[%s] wakes up %s, DELETE", ThreadID.get(), operator.pid));

                        wakeUp(operator);

                        break;
                    case GET:

                        int va = tree.numberOfPendingGet.incrementAndGet();

                        //System.out.println(String.format("Thread[%s] wakes up %s, GET, numberOfPendingGet=%d ", ThreadID.get(), operator.pid, va));

                        //wakeUp(operator);

                        operator.status.aboutToSleep.set(false);

                        break;

                    default:
                        break;
                }
                //System.out.println(String.format("Thread[%s] finish processing operator", ThreadID.get()));

            } catch (EmptyException e) {
            }
        }

    }

    private void wakeUp(Operator operator) {
        ThreadStatus threadStatus = operator.status;

        while(threadStatus.thread.getState() != State.WAITING); // Wait until thread is sleeping.

        synchronized (threadStatus.thread){
            threadStatus.aboutToSleep.set(false);
            //System.out.println(String.format("Thread[%s] wakes up %s, GET, thread STATE = %s ", ThreadID.get(), operator.pid, threadStatus.thread.getState()));
            threadStatus.thread.notifyAll(); // Awake thread.
        }

    }
}
