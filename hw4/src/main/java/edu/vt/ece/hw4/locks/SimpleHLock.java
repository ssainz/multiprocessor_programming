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
        numClusters = clusters;
        globalLock = new TestAndSpinLock();
        localLocks = new Lock[clusters];
        willReleaseGlobalLock = new AtomicBoolean(true);
        for(int i = 0 ; i < clusters ; i++){
            localLocks[i] = new SpinSleepLock(0,numThreads/clusters/3);
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

        Lock localLock = localLocks[clusterId];

        countWaitingThreads.getAndIncrement(clusterId);
        localLock.lock();


        if(clusterId == previousOwner && willReleaseGlobalLock.compareAndSet(false,true)){

        }else{
            globalLock.lock();
        }



        //For fairness, we start counting global lock usage:
        if(clusterId != previousOwner){
            previousOwner = clusterId;
            countOfUse = 1;
        }else{
            countOfUse++;
        }

    }

    @Override
    public void unlock() {
        int clusterId = ThreadCluster.getCluster(numClusters);
        int countOfThreadsWaitingInCluster = countWaitingThreads.decrementAndGet(clusterId);
        Lock localLock = localLocks[clusterId];
        if(BATCH_SIZE < countOfUse){
            localLock.unlock();
            globalLock.unlock(); // For fairness
            return;
        }
        if(countOfThreadsWaitingInCluster == 0){
            localLock.unlock(); // No one wants it.
            globalLock.unlock();
            return;
        }

        if(BATCH_SIZE > countOfUse){
            willReleaseGlobalLock.set(false);
            localLock.unlock();
            return;
        }

    }
}
