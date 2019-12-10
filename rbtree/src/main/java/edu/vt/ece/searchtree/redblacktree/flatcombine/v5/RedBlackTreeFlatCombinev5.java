package edu.vt.ece.searchtree.redblacktree.flatcombine.v5;

import edu.vt.ece.searchtree.redblacktree.SearchTree;
import edu.vt.ece.searchtree.redblacktree.SearchTreeNode;
import edu.vt.ece.searchtree.redblacktree.flatcombine.ThreadID;
import edu.vt.ece.searchtree.redblacktree.queue.BoundedLockFreeQueue;
import edu.vt.ece.searchtree.redblacktree.queue.Queue;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class RedBlackTreeFlatCombinev5 <Key extends Comparable<Key>, Value> implements SearchTree<Key, Value> {

    public int MAX_SIZE = 1000;
    public volatile RBTBatch<Key, Value> actualTree = new RBTBatch<>();

    /**
     * This v3 will not wait for GET threads, BACKOFF wait in DELETE,PUT
     */

    public final ThreadLocal<Operator> threadLocalOperation = new ThreadLocal<Operator>() {
        protected Operator initialValue() {

            ThreadStatus threadStatus = new ThreadStatus(ThreadID.get(), Thread.currentThread());
            Operator operator = new Operator(OperationType.NULL, null,null, ThreadID.get(), threadStatus);
            return operator;
        }
    };



    static class ThreadStatus{
        int pid;
        AtomicBoolean aboutToSleep;
        Thread thread;
        public ThreadStatus(int pid, Thread t){
            this.pid = pid;
            thread = t;
            aboutToSleep = new AtomicBoolean(false);
        }

        public ThreadStatus(){
            this.pid = 0;
            thread = null;
        }
    }

    static enum OperationType{
        PUT,
        GET,
        DELETE,
        NULL
    }

    class Operator{
        OperationType operationType;
        Key key;
        Value value;
        int pid;
        ThreadStatus status;
        boolean res;
        boolean softOperation = false;

        Operator(OperationType operationType, Key key, Value value, int pid, ThreadStatus status){
            this.operationType = operationType;
            this.key = key;
            this.value = value;
            this.pid = pid;
            this.status = status;
        }

        public void set(OperationType put, Key key, Value val) {
            this.operationType = put;
            this.key = key;
            this.value = val;
        }
    }


    public ThreadStatus[] waitingMonitors ; // Thread monitors.
    AtomicInteger numberOfPendingOperations = new AtomicInteger(0); // Tracks whether all get are served.
    public Queue<Operator> operationsQueue = null; // Tracks operations.
    int threadNumber = 0;
    RedBlackTreeFlatCombineWorkerv5 worker = null;
    AtomicBoolean timeToFinish = new AtomicBoolean(false);
    AtomicInteger size = new AtomicInteger(0);

    public RedBlackTreeFlatCombinev5(int threadNumber, int maxSize){
        MAX_SIZE = maxSize;
        this.threadNumber = threadNumber;
        //threadStatus = new ThreadStatus[threadNumber];
        waitingMonitors = new ThreadStatus[threadNumber];
        for(int i = 0 ; i < threadNumber ; i++){
            waitingMonitors[i] = new ThreadStatus();
        }
        this.operationsQueue = new BoundedLockFreeQueue<Operator>(threadNumber * 3);
        worker = new RedBlackTreeFlatCombineWorkerv5(this);
        worker.start(); // Start worker thread.
    }
    @Override
    public boolean putV2(Key key, Value val) {

        Operator op = threadLocalOperation.get();
        op.set(OperationType.PUT, key, val);

        //System.out.println(String.format("Thread[%s] PUTS and goes to SLEEP", ThreadID.get()));

        AtomicBoolean aboutToSleep = op.status.aboutToSleep;
        aboutToSleep.set(true);

        operationsQueue.enq(op);

        while(aboutToSleep.get()); //Spinning

        if(op.softOperation){ // Caller does the job in case the key already exists and this is an update only.

            op.softOperation = false; // Reset for next time.
            actualTree.softPut(key, val); // Marks node as Not deleted.
            this.numberOfPendingOperations.decrementAndGet();
            return true;

        }

        return true;
    }



    public boolean putV2_util(Key key, Value val) {
        return actualTree.putV2(key, val);
    }

    @Override
    public Value get(Key key) {

        Operator op = threadLocalOperation.get();
        op.set(OperationType.GET, key, null);

        //System.out.println(String.format("Thread[%s] GETS and goes to SLEEP", ThreadID.get()));

        AtomicBoolean aboutToSleep = op.status.aboutToSleep;
        aboutToSleep.set(true);

        operationsQueue.enq(op);

        while(aboutToSleep.get()); //Spinning

        Value v = actualTree.get(key);

        int va = numberOfPendingOperations.decrementAndGet();

        //System.out.println(String.format("Thread[%s] finish GET numberOfPendingGet = %d", ThreadID.get(), va));

        return v;
    }

    @Override
    public boolean delete(Key key) {
        Operator op = threadLocalOperation.get();
        op.set(OperationType.DELETE, key, null);

        //System.out.println(String.format("Thread[%s] DELETES and goes to SLEEP", ThreadID.get()));

        AtomicBoolean aboutToSleep = op.status.aboutToSleep;
        aboutToSleep.set(true);

        operationsQueue.enq(op);

        while(aboutToSleep.get()); //Spinning


        if(op.softOperation){ // Caller does the job in case the key already exists and this is an update only.
            op.softOperation = false; // Reset for next time.
            actualTree.softDelete(key);
            this.numberOfPendingOperations.decrementAndGet();
            return true;
        }

        return true;
    }

    @Override
    public void end() {
        timeToFinish.set(true);
    }

    @Override
    public int maxDepth() {
        return 0;
    }

    @Override
    public SearchTreeNode<Key, Value> getRootV2() {
        return null;
    }

    public boolean delete_util(Key key) {
        return actualTree.delete(key);
    }





}


