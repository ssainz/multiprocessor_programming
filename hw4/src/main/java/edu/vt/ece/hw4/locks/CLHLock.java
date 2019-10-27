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
        QNode qnode = myNode.get();
        qnode.locked = true;
        QNode pred = tail.getAndSet(qnode);
        myPred.set(pred);
        while(pred.locked){}
        System.out.println(String.format("Thread[%d]exitsLock", Thread.currentThread().getId()));
    }

    @Override
    public void unlock() {
        System.out.println(String.format("Thread[%d]enterUnLock", Thread.currentThread().getId()));
        QNode qnode = myNode.get();
        qnode.locked = false;
        QNode pred = myPred.get();
        pred.locked = false;
        myNode.set(pred);

        System.out.println(String.format("Thread[%d]exitsUnLock", Thread.currentThread().getId()));

    }

    static class QNode {     // Queue node inner class
        boolean locked = false;
        QNode next = null;
    }
}

