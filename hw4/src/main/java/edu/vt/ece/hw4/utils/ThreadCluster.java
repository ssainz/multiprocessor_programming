package edu.vt.ece.hw4.utils;

import edu.vt.ece.spin.ThreadID;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadCluster {


    /**
     * The next thread ID to be assigned
     **/
    private static AtomicInteger nextID = new AtomicInteger(0);

    /**
     * My thread-local ID.
     **/
    private static ThreadLocalID threadID = new ThreadLocalID();

    public static int get() {
        return threadID.get();
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

    public static int getCluster(int numClusters) {
        return threadID.get() % numClusters;
    }
    private static class ThreadLocalID extends ThreadLocal<Integer> {
        protected synchronized Integer initialValue() {
            return nextID.getAndIncrement();
        }
    }
}
