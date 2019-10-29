package edu.vt.ece.hw4.locks;

import edu.vt.ece.hw4.utils.ThreadCluster;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class SimpleHLock implements Lock {

    public static int numClusters = 0;
    public static int BATCH_SIZE = 0;
    public volatile Lock globalLock = null;
    public volatile Lock[] localLocks = null;
    public volatile int previousOwner = -1;
    public volatile int countOfUse = 0;
    public AtomicIntegerArray countWaitingThreads = null;
    public AtomicBoolean willReleaseGlobalLock = null;


    public SimpleHLock(int clusters, int numThreads) {
        BATCH_SIZE = numThreads/clusters;
        int maxSpin = (int) BATCH_SIZE / 2;
        maxSpin = Math.max(1, maxSpin);
        numClusters = clusters;
        //globalLock = new TTASLock();
        //globalLock = new TestAndSpinLock();
        globalLock = new TestAndSpinLock();
        localLocks = new Lock[clusters];
        willReleaseGlobalLock = new AtomicBoolean(true);
        for(int i = 0 ; i < clusters ; i++){
            //localLocks[i] = new SpinSleepLock(0,numThreads/clusters/3);
            //localLocks[i] = new SpinSleepLock(0,maxSpin); // Long waits in 128 cores.
            //localLocks[i] = new BackoffLock("Fixed"); // Longer than TAS
            localLocks[i] = new CLHLock(); // About same as TAS
            //localLocks[i] = new MCSLock(); // Pretty slow :P
            //localLocks[i] = new TTASLock(); // BEST in dogwood
            //localLocks[i] = new TestAndSpinLock(); // Second best

        }

        countWaitingThreads = new AtomicIntegerArray(clusters);

    }

    @Override
    public boolean trylock() {
        return false;
    }

    @Override
    public void lock() {


        int clusterId = ThreadCluster.getCluster(numClusters);

        //System.out.println(String.format("LOCK:Thread[%s][%d]a",Thread.currentThread(),clusterId));

        Lock localLock = localLocks[clusterId];

        int waitingThreads = countWaitingThreads.getAndIncrement(clusterId);
        localLock.lock();
        //System.out.println(String.format("LOCK:Thread[%s][%d][waiting:%d]b",Thread.currentThread(),clusterId, waitingThreads));

        if(clusterId == previousOwner && willReleaseGlobalLock.compareAndSet(false,true)){
            //System.out.println(String.format("LOCK:Thread[%s][%d][waiting:%d]c:enterCS without globallock",Thread.currentThread(),clusterId, waitingThreads));
        }else{
            //System.out.println(String.format("LOCK:Thread[%s][%d]c:wait globallock",Thread.currentThread(),clusterId));
            globalLock.lock();
        }

        //System.out.println(String.format("LOCK:Thread[%s][%d]d:enter CS",Thread.currentThread(),clusterId));

        //For fairness, we start counting global lock usage:
        if(clusterId != previousOwner){
            previousOwner = clusterId;
            countOfUse = 1;
        }else{
            countOfUse++;
        }
        //System.out.println(String.format("LOCK:Thread[%s][%d][counterOfUse=%d]e:enter CS",Thread.currentThread(),clusterId, countOfUse));
    }

    @Override
    public void unlock() {
        int clusterId = ThreadCluster.getCluster(numClusters);
        //System.out.println(String.format("UNLOCK:Thread[%s][%d]a",Thread.currentThread(),clusterId));
        int countOfThreadsWaitingInCluster = countWaitingThreads.decrementAndGet(clusterId);
        Lock localLock = localLocks[clusterId];
        if(BATCH_SIZE <= countOfUse){
            //System.out.println(String.format("UNLOCK:Thread[%s][%d][BATCH= %d < countOfUse %d]b",Thread.currentThread(),clusterId,BATCH_SIZE, countOfUse));
            localLock.unlock();
            globalLock.unlock(); // For fairness
            return;
        }
        if(countOfThreadsWaitingInCluster == 0){
            //System.out.println(String.format("UNLOCK:Thread[%s][%d][countOfThreadsWaitingInCluster is %d]c",Thread.currentThread(),clusterId,countOfThreadsWaitingInCluster));
            localLock.unlock(); // No one wants it.
            globalLock.unlock();
            return;
        }

        if(BATCH_SIZE > countOfUse){
            //System.out.println(String.format("UNLOCK:Thread[%s][%d][BATCH= %d > countOfUse %d]d",Thread.currentThread(),clusterId, countOfUse,BATCH_SIZE));
            willReleaseGlobalLock.set(false);
            localLock.unlock();
            return;
        }

        //System.out.println(String.format("UNLOCK:Thread[%s][%d][BATCH= %d , countOfUse %d, countOfThreadsWaitingInCluster %d]e",Thread.currentThread(),clusterId, countOfUse,BATCH_SIZE, countOfThreadsWaitingInCluster));
    }
}
