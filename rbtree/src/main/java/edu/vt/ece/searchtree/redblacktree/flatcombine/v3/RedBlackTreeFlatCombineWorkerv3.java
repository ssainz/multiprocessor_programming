package edu.vt.ece.searchtree.redblacktree.flatcombine.v3;

import edu.vt.ece.searchtree.redblacktree.queue.EmptyException;
import edu.vt.ece.searchtree.redblacktree.queue.Queue;
import edu.vt.ece.searchtree.redblacktree.flatcombine.v3.RedBlackTreeFlatCombinev3.Operator;
import edu.vt.ece.searchtree.redblacktree.flatcombine.v3.RedBlackTreeFlatCombinev3.ThreadStatus;

public class RedBlackTreeFlatCombineWorkerv3<Key extends Comparable<Key>, Value> extends Thread {

    RedBlackTreeFlatCombinev3 tree = null;

    RedBlackTreeFlatCombineWorkerv3(RedBlackTreeFlatCombinev3<Key, Value> tree){
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
        operator.status.aboutToSleep.set(false);

    }
}

