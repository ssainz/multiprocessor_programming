package edu.vt.ece.hw4;

import edu.vt.ece.hw4.barriers.ArrayBarrier;
import edu.vt.ece.hw4.barriers.Barrier;
import edu.vt.ece.hw4.barriers.TTASBarrier;
import edu.vt.ece.hw4.barriers.VolatileArrayBarrier;
import edu.vt.ece.hw4.bench.*;
import edu.vt.ece.hw4.locks.*;

public class Benchmark {

    private static final String ALOCK = "ALock";
    private static final String BACKOFFLOCK = "BackoffLock";
    private static final String CLHLOCK = "CLHLock";
    private static final String MCSLOCK = "MCSLock";
    private static final String TASLOCK = "TASLock";
    private static final String TTASLOCK = "TTASLock";
    private static final String PRIORITYQUEUELOCK = "PriorityQueueLock";
    private static final String SPINSLEEPLOCK = "SpinSleepLock";
    private static final String SPINSLEEPARRAYLOCK = "SpinSleepArrayLock";
    private static final String SIMPLEHLOCK = "SimpleHLock";
    private static final String HBOLOCK = "HBOLock";

    public static void main(String[] args) throws Exception {
        String mode = args.length <= 0 ? "normal" : args[0];
        String lockClass = (args.length <= 1 ? ALOCK : args[1]);
        int threadCount = (args.length <= 2 ? 16 : Integer.parseInt(args[2]));
        int totalIters = (args.length <= 3 ? 64000 : Integer.parseInt(args[3]));
        int iters = totalIters / threadCount;

        run(args, mode, lockClass, threadCount, iters);

    }

    private static void run(String[] args, String mode, String lockClass, int threadCount, int iters) throws Exception {

        String option = args.length < 5 ? "1" : args[4] ;

        double[] results = new double[5];

        for (int i = 0; i < 5; i++) {
            double res = 0.0;
            Lock lock = null;
            switch (lockClass.trim()) {
                case ALOCK:
                    lock = new ALock(threadCount);
                    break;
                case BACKOFFLOCK:
                    lock = new BackoffLock(args[4]);
                    break;
                case MCSLOCK:
                    lock = new MCSLock();
                    break;
                case CLHLOCK:
                    lock = new CLHLock();
                    break;
                case TTASLOCK:
                    lock = new TTASLock();
                    break;
                case TASLOCK:
                    lock = new TestAndSpinLock();
                    break;
                case PRIORITYQUEUELOCK:
                    lock = new PriorityQueueLock(1000);
                    break;
                case SPINSLEEPLOCK:
                    lock = new SpinSleepLock(threadCount, (int) threadCount/3);
                    break;
                case SPINSLEEPARRAYLOCK:
                    lock = new SpinSleepArrayLock(threadCount, (int) threadCount/3);
                    break;
                case SIMPLEHLOCK:
                    lock = new SimpleHLock(Integer.parseInt(option), threadCount);
                    break;
                case HBOLOCK:
                    lock = new HBOLock(Integer.parseInt(option));
                    break;
            }
            Counter counter = null;
            if(option.equals("Timeout")){
                counter = new SharedCounterTimeout(0, lock);
            }else{
                counter = new SharedCounter(0, lock);
            }
            switch (mode.trim().toLowerCase()) {
                case "normal":
                    res = runNormal(counter, threadCount, iters);
                    break;

                case "normalpriority":
                    res = runNormalPriority(counter, threadCount, iters);
                    break;
                case "empty":
                    res = runEmptyCS(lock, threadCount, iters);
                    break;
                case "long":
                    res = runLongCS(lock, threadCount, iters);
                    break;
                case "cluster":
                    if(lockClass.equals(BACKOFFLOCK)){
                        res = runClusterCS(lock, threadCount, iters, 1);
                    }else{
                        res = runClusterCS(lock, threadCount, iters, Integer.parseInt(option));
                    }

                    break;
                case "barrier":
                    Barrier b = null;
                    if(args[4].equals("ttasBarrier")){
                        b = new TTASBarrier(threadCount);
                    }else if (args[4].equals("arrayBarrier")){
                        b = new ArrayBarrier(threadCount);
                    }else if (args[4].equals("volatileArrayBarrier")){
                        b = new VolatileArrayBarrier(threadCount);
                    }else{
                        b = new ArrayBarrier(threadCount);
                    }

                    // Start test:
                    res = runBarrier(b, threadCount, iters);
                    break;
                default:
                    System.out.println(mode.trim().toLowerCase());
                    throw new UnsupportedOperationException("Implement this");
            }
            results[i] = res;
        }

        double averageWithoutFirst = 0.0;
        double sum = 0;
        for(int i = 1 ; i < 5 ; i++){
            sum += results[i];
        }

        System.out.println(String.format("[Lock = %s][%s][Threads = %d][Iter per thread = %d]avg:  " + (sum/4), lockClass, option, threadCount, iters));

    }

    private static double runBarrier(Barrier barrier , int threadCount, int iters) throws Exception {
        final BarrierThread[] threads = new BarrierThread[threadCount];


        long totalTime = 0;

        for(int it = 0 ; it < iters ; it++){
            //System.out.println(String.format("It[%d] - ENTER",it));
            BarrierThread.reset();
            for (int t = 0; t < threadCount; t++) {
                threads[t] = new BarrierThread(barrier);
            }

            for (int t = 0; t < threadCount; t++) {
                threads[t].start();
            }

            for (int t = 0; t < threadCount; t++) {
                threads[t].join();
                totalTime += threads[t].getElapsedTime();
            }
            //System.out.println(String.format("It[%d] - EXIT",it));
            barrier.reset();
        }

        double res = (totalTime / threadCount) / 1000000 ; // Result as milliseconds

        //System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
        return res;
    }

    private static double runNormalPriority(Counter counter, int threadCount, int iters) throws Exception {
        final PriorityBasedThread[] threads = new PriorityBasedThread[threadCount];
        TestThread.reset();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new PriorityBasedThread(counter, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();

        }

        double res = totalTime / threadCount;
        //System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
        return res;
    }

    private static double runNormal(Counter counter, int threadCount, int iters) throws Exception {
        final TestThread[] threads = new TestThread[threadCount];
        TestThread.reset();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new TestThread(counter, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
        }

        double res = totalTime / threadCount;
        //System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
        return res;
    }

    private static double runEmptyCS(Lock lock, int threadCount, int iters) throws Exception {

        final EmptyCSTestThread[] threads = new EmptyCSTestThread[threadCount];
        TestThread.reset();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new EmptyCSTestThread(lock, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
        }
        return totalTime / threadCount;
        //System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
    }

    static double runLongCS(Lock lock, int threadCount, int iters) throws Exception {
        final Counter counter = new Counter(0);
        final LongCSTestThread[] threads = new LongCSTestThread[threadCount];
        TestThread.reset();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new LongCSTestThread(lock, counter, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
        }

        return totalTime / threadCount;
        //System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
    }

    static double runClusterCS(Lock lock, int threadCount, int iters, int cluster) throws Exception {
        /**
         * The cluster number is already sent to the lock object instance and the lock uses it in each call to the "getClusterId".
         */
        final Counter counter = new Counter(0);
        final LongCSTestThread[] threads = new LongCSTestThread[threadCount];
        TestThread.reset();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new LongCSTestThread(lock, counter, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
        }

        return totalTime / threadCount;
        //System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
    }
}
