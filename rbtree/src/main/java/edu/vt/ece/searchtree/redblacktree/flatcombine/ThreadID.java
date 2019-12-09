package edu.vt.ece.searchtree.redblacktree.flatcombine;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Illustrates use of thread-local storage. Test by running main().
 * @author Maurice Herlihy
 */
public class ThreadID {
    /**
     * The next thread ID to be assigned
     **/
    private static AtomicInteger nextID = new AtomicInteger(0);

    private static final ThreadLocal<Integer> threadIDv2 = new ThreadLocal<Integer>() {
        protected Integer initialValue() {
//            System.out.println("NEW THREAD SPAWNED!!");
//            for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
//                System.out.println(ste);
//            }
            return new Integer(nextID.getAndIncrement());
        }
    };


    /**
     * My thread-local ID.
     **/
    private static ThreadLocalID threadID = new ThreadLocalID();
    public static int get() {
        //return (int) Thread.currentThread().getId();
        //return threadID.get();
        return threadIDv2.get();
    }
    /**
     * When running multiple tests, reset thread id state
     **/
    public static void reset() {
        nextID.set(0);
    }
    public static void set(int value) {
        threadID.set(value);
    }

    public static int getCluster() {
        return threadID.get() / 2;
    }
    private static class ThreadLocalID extends ThreadLocal<Integer> {
        protected synchronized Integer initialValue() {
            return nextID.getAndIncrement();
        }
    }
}