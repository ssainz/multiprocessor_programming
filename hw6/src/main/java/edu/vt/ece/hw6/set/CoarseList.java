package edu.vt.ece.hw6.set;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * List using coarse-grained synchronization.
 * @param T Item type.
 * @author Sergio
 */
public class CoarseList<T> implements ConcurrentSet<T> {
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

    /**
     * Constructor
     */
    public CoarseList() {
        // Add sentinels to start and end
        head  = new Node(Integer.MIN_VALUE);
        tail  = new Node(Integer.MAX_VALUE);
        head.next = this.tail;
    }

    /**
     * Add an element.
     * @param item element to add
     * @return true iff element was not there already
     */

    public boolean add(T item) {
        Node pred, curr;
        int key = item.hashCode();
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
                    Node node = new Node(item);
                    node.next = curr;
                    pred.next = node;
                    return true;
                }
            /*} finally {
                lock.unlock();
            }*/
        }

    }

    /**
     * Remove an element.
     * @param item element to remove
     * @return true iff element was present
     */
    public boolean remove(T item) {
        Node pred, curr;
        int key = item.hashCode();
        //lock.lock();
        synchronized (this){
            //try {
                pred = this.head;
                curr = pred.next;
                while (curr.key < key) {
                    pred = curr;
                    curr = curr.next;
                }
                if (key == curr.key) {  // present
                    pred.next = curr.next;
                    return true;
                } else {
                    return false;         // not present
                }
            /*}  finally {               // always unlock
                lock.unlock();
            } */
        }


    }
    /**
     * Test whether element is present
     * @param item element to test
     * @return true iff element is present
     */
    public boolean contains(T item) {
        Node pred, curr;
        int key = item.hashCode();
        //lock.lock();
        //try {
        synchronized (this) {
            pred = head;
            curr = pred.next;
            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }
            return (key == curr.key);
        }
        /*} finally {               // always unlock
            lock.unlock();
        }*/
    }
    /**
     * list Node
     */
    private class Node {
        /**
         * actual item
         */
        T item;
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
        Node(T item) {
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
