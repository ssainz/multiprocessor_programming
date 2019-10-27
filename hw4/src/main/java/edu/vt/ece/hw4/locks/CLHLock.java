/*
 * MCSLock.java
 *
 * Created on January 20, 2006, 11:41 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package edu.vt.ece.hw4.locks;

import java.util.concurrent.atomic.AtomicReference;

public class CLHLock implements Lock {
    AtomicReference<QNode> tail;
    ThreadLocal<QNode> myPred;
    ThreadLocal<QNode> myNode;


    public CLHLock() {
        tail = new AtomicReference<QNode>(new QNode());
        myNode = new ThreadLocal<QNode>(){
            protected QNode initialValue(){
                return new QNode();
            }
        };
        myPred = new ThreadLocal<QNode>(){
            protected QNode initialValue(){
                return null;
            }
        };
    }

    @Override
    public void lock() {
        System.out.println(String.format("Thread[%d]enterLock", Thread.currentThread().getId()));
        QNode pred = null;
        QNode qnode = myNode.get();
        System.out.println(String.format("Thread[%d]enterLock:a", Thread.currentThread().getId()));
        qnode.locked = true;
        System.out.println(String.format("Thread[%d]enterLock:b", Thread.currentThread().getId()));
        pred = tail.getAndSet(qnode);
        System.out.println(String.format("Thread[%d]enterLock:c", Thread.currentThread().getId()));
        myPred.set(pred);
        System.out.println(String.format("Thread[%d]enterLock:d, %s", Thread.currentThread().getId(), pred));
        while(pred.locked){}
        System.out.println(String.format("Thread[%d]exitsLock", Thread.currentThread().getId()));
    }

    @Override
    public void unlock() {
        System.out.println(String.format("Thread[%d]enterUnLock", Thread.currentThread().getId()));
        QNode pred = null;
        System.out.println(String.format("Thread[%d]enterUnLock:a", Thread.currentThread().getId()));
        QNode qnode = myNode.get();
        System.out.println(String.format("Thread[%d]enterUnLock:b", Thread.currentThread().getId()));
        qnode.locked = false;
        System.out.println(String.format("Thread[%d]enterUnLock:c,%s", Thread.currentThread().getId(), qnode));
        pred = myPred.get();
        System.out.println(String.format("Thread[%d]enterUnLock:d", Thread.currentThread().getId()));
        pred.locked = false;
        myNode.set(pred);

        System.out.println(String.format("Thread[%d]exitsUnLock", Thread.currentThread().getId()));

    }

    static class QNode {     // Queue node inner class
        boolean locked = false;
        QNode next = null;
    }
}

