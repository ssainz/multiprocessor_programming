package edu.vt.ece.hw5.bag;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockFreeBag<T extends Number> {
    public static final ThreadLocal<Node> localNode = new ThreadLocal<Node>(){
        protected Node initialValue(){
            return new Node();
        }
    };;

    volatile int countOfThreadsInThisBag = 0;

    /**
     * First list Node
     */
    volatile public Node head;
    /**
     * Last list Node
     */
    volatile public Node tail;
    /**
     * Synchronizes access to list
     */
    volatile public Lock lock = new ReentrantLock();


    public boolean addToMainList() {


        //lock.lock();
        synchronized (this){
            //try {
                Node pred, curr;
                int key = localNode.get().hashCode();
                pred = head;
                curr = pred.next;
                while (curr.key < key) {
                    pred = curr;
                    curr = curr.next;
                }
                if (key == curr.key) {
                    return false;
                } else {

                    Node node = localNode.get();
                    node.next = curr;
                    pred.next = node;
                    return true;
                }
//            } finally {
//                lock.unlock();
//
//            }
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
        if(localNode.get().next  == null){
            addToMainList();
        }

        LockFreeListForBag<Integer> list = localNode.get().item;


        list.add(i);

    }

    public void tryRemoveAny() {

        if(localNode.get().next == null){
            addToMainList();
        }

        LockFreeListForBag<Integer> list = localNode.get().item;

        if(!list.remove()){

            Node n = localNode.get().next;
            while(n.key < tail.key){
                if(n.item.remove()){
                    return;
                }
                n = n.next;
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
    static class Node {
        /**
         * actual item
         */
        volatile public LockFreeListForBag<Integer>  item;
        /**
         * item's hash code
         */
        volatile public int key;
        /**
         * next Node in list
         */
        volatile public  Node next;
        /**
         * Constructor for usual Node
         */
        Node() {
            item = new LockFreeListForBag<Integer>();
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
