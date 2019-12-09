package edu.vt.ece.searchtree.redblacktree.flatcombine.v1;

import edu.vt.ece.searchtree.redblacktree.RedBlackTreeNonThreadSafe;
import edu.vt.ece.searchtree.redblacktree.SearchTree;
import edu.vt.ece.searchtree.redblacktree.SearchTreeNode;
import edu.vt.ece.searchtree.redblacktree.flatcombine.ThreadID;
import edu.vt.ece.searchtree.redblacktree.queue.BoundedLockFreeQueue;
import edu.vt.ece.searchtree.redblacktree.queue.Queue;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class RedBlackTreeFlatCombine<Key extends Comparable<Key>, Value> implements SearchTree<Key, Value> {

    RedBlackTreeNonThreadSafe<Key, Value> actualTree = new RedBlackTreeNonThreadSafe<>();

    public static final ThreadLocal<ThreadStatus> threadLocalStatus = new ThreadLocal<ThreadStatus>() {
        protected ThreadStatus initialValue() {
            return new ThreadStatus(ThreadID.get(), Thread.currentThread());
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
        DELETE
    }

    class Operator{
        OperationType operationType;
        Key key;
        Value value;
        int pid;
        ThreadStatus status;
        boolean res;

        Operator(OperationType operationType, Key key, Value value, int pid, ThreadStatus status){
            this.operationType = operationType;
            this.key = key;
            this.value = value;
            this.pid = pid;
            this.status = status;
        }
    }

    //public ThreadStatus[] threadStatus ; // Tracks thread status.
    public ThreadStatus[] waitingMonitors ; // Thread monitors.
    AtomicInteger numberOfPendingGet = new AtomicInteger(0); // Tracks whether all get are served.
    public Queue<Operator> operationsQueue = null; // Tracks operations.
    int threadNumber = 0;
    RedBlackTreeFlatCombineWorker worker = null;
    AtomicBoolean timeToFinish = new AtomicBoolean(false);

    public RedBlackTreeFlatCombine(int threadNumber){
        this.threadNumber = threadNumber;
        //threadStatus = new ThreadStatus[threadNumber];
        waitingMonitors = new ThreadStatus[threadNumber];
        for(int i = 0 ; i < threadNumber ; i++){
            waitingMonitors[i] = new ThreadStatus();
        }
        this.operationsQueue = new BoundedLockFreeQueue<Operator>(threadNumber * 3);
        worker = new RedBlackTreeFlatCombineWorker(this);
        worker.start(); // Start worker thread.
    }
    @Override
    public boolean putV2(Key key, Value val) {

        Operator op = new Operator(OperationType.PUT, key, val, ThreadID.get(), threadLocalStatus.get());

        //System.out.println(String.format("Thread[%s] PUTS and goes to SLEEP", ThreadID.get()));

        operationsQueue.enq(op);

        goToSleep();

        return op.res;
    }

    public boolean putV2_util(Key key, Value val) {

        return actualTree.putV2(key, val);

    }

    private void goToSleep() {

        //threadStatus[pid] = status;

        synchronized (waitingMonitors[ ThreadID.get() % threadNumber ]){

            try {
                waitingMonitors[ThreadID.get() % threadNumber ].wait();
                //System.out.println(String.format("Thread[%s] wakes UP!", ThreadID.get()));
            } catch (Exception e) {
                System.out.println("EXCEPTION!");
                e.printStackTrace();
            }
        }

    }

    @Override
    public Value get(Key key) {

        Operator op = new Operator(OperationType.GET, key, null, ThreadID.get(), threadLocalStatus.get());

        //System.out.println(String.format("Thread[%s] GETS and goes to SLEEP", ThreadID.get()));

        operationsQueue.enq(op);

        goToSleep();

        Value v = actualTree.get(key);

        int va = numberOfPendingGet.decrementAndGet();

        //System.out.println(String.format("Thread[%s] finish GET numberOfPendingGet = %d", ThreadID.get(), va));

        return v;
    }

    @Override
    public boolean delete(Key key) {
        Operator op = new Operator(OperationType.DELETE, key, null, ThreadID.get(), threadLocalStatus.get());

        //System.out.println(String.format("Thread[%s] DELETES and goes to SLEEP", ThreadID.get()));

        operationsQueue.enq(op);

        goToSleep();

        return op.res;
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
