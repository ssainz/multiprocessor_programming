package edu.vt.ece.hw5.bag;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockFreeBag<T extends Number> {
    ThreadLocal<Integer> threadCount = null;
    ThreadLocal<Node> localNode = null;

    volatile int countOfThreadsInThisBag = 0;

    /**
     * First list Node
     */
    volatile private Node head;
    /**
     * Last list Node
     */
    volatile private Node tail;
    /**
     * Synchronizes access to list
     */
    private Lock lock = new ReentrantLock();


    public boolean addToMainList() {


        localNode = new ThreadLocal<Node>(){
            protected Node initialValue(){
                LockFreeListForBag<Integer> list = new LockFreeListForBag<Integer>();
                return new Node(list);
            }
        };

        Node pred, curr;
        int key = localNode.hashCode();
        //lock.lock();
        synchronized (this){
            //try {
            pred = head;
            curr = pred.next;
            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }
            if (key == curr.key) {
                return false;
            } else {
                countOfThreadsInThisBag ++;
                threadCount = new ThreadLocal<Integer>(){
                    protected Integer initialValue(){
                        return countOfThreadsInThisBag;
                    }
                };
                Node node = localNode.get();
                node.next = curr;
                pred.next = node;
                return true;
            }
            /*} finally {
                lock.unlock();
            }*/
        }

    }

    public LockFreeBag(){
        countOfThreadsInThisBag = 0;
        // Add sentinels to start and end
        head  = new Node(Integer.MIN_VALUE);
        tail  = new Node(Integer.MAX_VALUE);
        head.next = this.tail;
    }


    public void add(Integer i) {
        if(localNode == null){
            addToMainList();
        }

        LockFreeListForBag<Integer> list = localNode.get().item;
        list.add(i);

    }

    public void tryRemoveAny() {

        //System.out.println("HIII");
        if(localNode == null){
            addToMainList();
        }

        LockFreeListForBag<Integer> list = localNode.get().item;

        if(!list.remove()){

            Node n = localNode.get().next;

            System.out.println("n = "+ n);
            System.out.println("tail = "+ tail);
            while(n.key < tail.key){
                if(n.item.remove()){
                    return;
                }
                n = n.next;
                System.out.println("n = "+ n);
                System.out.println("tail = "+ tail);
            }

            // Start from beginning
            n = head.next;
            while(n.key < localNode.get().key){
                if(n.item.remove()){
                    return;
                }
                n = n.next;
            }

        }
        return;
    }


    /**
     * list Node
     */
    private class Node {
        /**
         * actual item
         */
        volatile LockFreeListForBag<Integer>  item;
        /**
         * item's hash code
         */
        volatile int key;
        /**
         * next Node in list
         */
        volatile Node next;
        /**
         * Constructor for usual Node
         * @param item element in list
         */
        Node(LockFreeListForBag<Integer> item) {
            this.item = item;
            this.key = item.hashCode();
        }
        /**
         * Constructor for sentinel Node
         * @param key should be min or max int value
         */
        Node(int key) {
            this.item = null;
            this.key = key;
        }
    }
}
