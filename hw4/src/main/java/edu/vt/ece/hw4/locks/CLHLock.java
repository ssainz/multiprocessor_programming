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

        QNode pred = null;
        QNode qnode = myNode.get();
        qnode.locked = true;
        pred = tail.getAndSet(qnode);
        myPred.set(pred);
        while(pred.locked){}

    }

    @Override
    public void unlock() {

        QNode pred = null;
        QNode qnode = myNode.get();
        qnode.locked = false;
        pred = myPred.get();
        myNode.set(pred);
        qnode = myNode.get();
        qnode.locked = false;

    }

    static class QNode {     // Queue node inner class
        public volatile boolean locked = false;
    }
}

