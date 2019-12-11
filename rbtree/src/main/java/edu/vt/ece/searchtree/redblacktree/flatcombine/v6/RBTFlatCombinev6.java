package edu.vt.ece.searchtree.redblacktree.flatcombine.v6;

import edu.vt.ece.searchtree.redblacktree.SearchTree;
import edu.vt.ece.searchtree.redblacktree.SearchTreeNode;
import edu.vt.ece.searchtree.redblacktree.flatcombine.ThreadID;
import edu.vt.ece.searchtree.redblacktree.flatcombine.v5.RBTBatch;
import edu.vt.ece.searchtree.redblacktree.queue.BoundedLockFreeQueue;
import edu.vt.ece.searchtree.redblacktree.queue.Queue;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class RBTFlatCombinev6 <Key extends Comparable<Key>, Value> implements SearchTree<Key, Value> {


    boolean isProd = true;
    /**
     * This v6 will wait in different places, also let get go and stop them with a atomic var.
     */

    public final ThreadLocal<ThreadStatus> threadLocalStatus = new ThreadLocal<ThreadStatus>() {
        protected ThreadStatus initialValue(){
            ThreadStatus threadStatus = new ThreadStatus(ThreadID.get());
            return threadStatus;
        }
    };




    class ThreadStatus{
        int pid = 0;
        AtomicBoolean stopTheWorld;
        AtomicBoolean stopped;
        AtomicBoolean waitingInBlockingQueue;
        volatile public ThreadStatus next;
        volatile public int nodekey;
        volatile OperationType operationType;
        volatile Key opKey;
        volatile Value value;
        volatile boolean res;
        volatile boolean softOperation = false;

        public ThreadStatus(int key){
            pid = key;
            this.nodekey = key;
            stopTheWorld = new AtomicBoolean(false);
            stopped = new AtomicBoolean(false);
            waitingInBlockingQueue = new AtomicBoolean(false);
        }

        public void set(OperationType put, Key key, Value val) {
            this.operationType = put;
            this.opKey = key;
            this.value = val;
        }
    }

    static enum OperationType{
        PUT,
        GET,
        DELETE,
        NULL
    }



    volatile int countOfThreadsInThisBag = 0;
    public int MAX_SIZE = 1000;
    public volatile RBTBatch<Key, Value> actualTree = new RBTBatch<>();
    public Queue<ThreadStatus> operationsQueue = null; // Tracks operations.
    int threadNumber = 0;
    RBTCombineWorkerv6 worker = null;
    AtomicBoolean timeToFinish = new AtomicBoolean(false);
    AtomicInteger size = new AtomicInteger(0);

    volatile public ThreadStatus tail, head;

    public RBTFlatCombinev6(int threadNumber, int maxSize){
        MAX_SIZE = maxSize;
        this.threadNumber = threadNumber;
        this.operationsQueue = new BoundedLockFreeQueue<ThreadStatus>(threadNumber * 3);
        worker = new RBTCombineWorkerv6(this);
        worker.start(); // Start worker thread.


        /* List within tree */
        countOfThreadsInThisBag = 0;
        // Add sentinels to start and end
        head  = new ThreadStatus(Integer.MIN_VALUE);
        tail  = new ThreadStatus(Integer.MAX_VALUE);
        head.next = this.tail;

    }

    public boolean addToMainList() {

        if(threadLocalStatus.get().next  != null){
            return true;
        }

        int id = ThreadID.get();
        //lock.lock();
        synchronized (this){
            //try {
            ThreadStatus pred, curr;
            int key = threadLocalStatus.get().nodekey;
            pred = head;
            curr = pred.next;
            while (curr.nodekey < key) {
                pred = curr;
                curr = curr.next;
            }
            if (key == curr.nodekey) {
                return false;
            } else {

                ThreadStatus node = threadLocalStatus.get();
                node.next = curr;
                pred.next = node;
                //System.out.println(String.format("addToMainList[Bag:%s] Thread=%d,adds its localNode %s", this, id, node));
                return true;
            }
//            } finally {
//                lock.unlock();
//            }
        }


    }

    @Override
    public boolean putV2(Key key, Value val) {

        ThreadStatus status = threadLocalStatus.get();
        if(!isProd) System.out.println(String.format("Thread[%s] PUT position %d", ThreadID.get(), ThreadID.get() % (threadNumber + 1)));

        if(status.stopTheWorld.get()){



            synchronized (status){
                while(!status.stopped.compareAndSet(false, true)); // Mark so worker knows this thread stopped.
                if(!isProd) System.out.println(String.format("Status:%d,%s, stopped object %s , stopped %b", status.pid, status, status.stopped, status.stopped.get()));
                try {
                    status.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        status.set(OperationType.PUT, key, val);



        while(!status.waitingInBlockingQueue.compareAndSet(false, true));
        //System.out.println(String.format("Thread[%s] PUTS and goes to SLEEP", ThreadID.get()));
        if(!isProd) System.out.println(String.format("Status:%d,%s, waitingInBlockingQueue object %s , waitingInBlockingQueue %b", status.pid, status, status.waitingInBlockingQueue, status.waitingInBlockingQueue.get()));

        operationsQueue.enq(status);

        while(status.waitingInBlockingQueue.get()); //Spinning

        if(status.softOperation){ // Caller does the job in case the key already exists and this is an update only.

            status.softOperation = false; // Reset for next time.
            actualTree.softPut(key, val); // Marks node as Not deleted.
            return true;

        }

        return true;
    }



    public boolean putV2_util(Key key, Value val) {
        return actualTree.putV2(key, val);
    }

    @Override
    public Value get(Key key) {
        addToMainList();

        ThreadStatus status = threadLocalStatus.get();
        if(!isProd) System.out.println(String.format("Thread[%s] GET position %d", ThreadID.get(), ThreadID.get() % (threadNumber + 1)));

        if(status.stopTheWorld.get()){

            synchronized (status){

                try {
                    while(!status.stopped.compareAndSet(false, true)); // Mark so worker knows this thread stopped.
                    if(!isProd) System.out.println(String.format("Status:%d,%s, stopped object %s , stopped %b", status.pid, status, status.stopped, status.stopped.get()));
                    if(!isProd) System.out.println(String.format("Thread[%s] GET position %d STOPPED %b", ThreadID.get(), ThreadID.get() % (threadNumber + 1), status.stopped.get()));
                    status.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        //op.set(OperationType.GET, key, null);

        //System.out.println(String.format("Thread[%s] GETS and goes to SLEEP", ThreadID.get()));

        Value v = actualTree.get(key);

        //System.out.println(String.format("Thread[%s] finish GET numberOfPendingGet = %d", ThreadID.get(), va));

        return v;
    }

    @Override
    public boolean delete(Key key) {
        ThreadStatus status = threadLocalStatus.get();
        if(!isProd) System.out.println(String.format("Thread[%s] DELETE position %d", ThreadID.get(), ThreadID.get() % (threadNumber + 1)));

        if(status.stopTheWorld.get()){

            synchronized (status){
                while(!status.stopped.compareAndSet(false, true)); // Mark so worker knows this thread stopped.
                if(!isProd) System.out.println(String.format("Status:%d,%s, stopped object %s , stopped %b", status.pid, status, status.stopped, status.stopped.get()));
                if(!isProd) System.out.println(String.format("Thread[%s] DELETE position %d STOPPED %b", ThreadID.get(), ThreadID.get() % (threadNumber + 1), status.stopped.get()));
                try {
                    status.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        status.set(OperationType.DELETE, key, null);

        //System.out.println(String.format("Thread[%s] DELETES and goes to SLEEP", ThreadID.get()));

        while(!status.waitingInBlockingQueue.compareAndSet(false, true));
        if(!isProd) System.out.println(String.format("Status:%d,%s, waitingInBlockingQueue object %s , waitingInBlockingQueue %b", status.pid, status, status.waitingInBlockingQueue, status.waitingInBlockingQueue.get()));
        if(!isProd) System.out.println(String.format("Thread[%s] DELETE position %d waitingInBlockingQueue %b, status %s", ThreadID.get(), ThreadID.get() % (threadNumber + 1), status.waitingInBlockingQueue.get(), status));

        operationsQueue.enq(status);

        while(status.waitingInBlockingQueue.get()); //Spinning


        if(status.softOperation){ // Caller does the job in case the key already exists and this is an update only.
            status.softOperation = false; // Reset for next time.
            actualTree.softDelete(key);
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


