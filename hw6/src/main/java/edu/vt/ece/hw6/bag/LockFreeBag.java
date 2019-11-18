package edu.vt.ece.hw6.bag;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockFreeBag<T> {
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
                Node<T> pred, curr;
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


    public void add(T i) {
        if(localNode.get().next  == null){
            addToMainList();
        }

        LockFreeListForBag<T> list = localNode.get().item;

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

    public T deque() {

        long id = Thread.currentThread().getId();
        if(localNode.get().next == null){
            addToMainList();
        }

        LockFreeListForBag<T> list = localNode.get().item;
        while(true) {
            T item = list.dequeue();
            if (item == null) {

                Node<T> n = localNode.get().next;
                while (n.key < tail.key) {
                    System.out.println(String.format("LockFreeBag:%d,%s", id, n));
                    item = n.item.dequeue();
                    if (item != null) {
                        return item;
                    }
                    n = n.next;
                }

                // Start from beginning
                n = head.next;
                while (n.key < localNode.get().key) {
                    item = n.item.dequeue();
                    if (item != null) {
                        return item;
                    }
                    n = n.next;
                }

            }
        }
    }


    /**
     * list Node
     */
    static class Node<T> {
        /**
         * actual item
         */
        volatile public LockFreeListForBag<T>  item;
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
            item = new LockFreeListForBag<T>();
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
