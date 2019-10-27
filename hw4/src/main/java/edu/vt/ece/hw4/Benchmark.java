package edu.vt.ece.hw4;

import edu.vt.ece.hw4.barriers.ArrayBarrier;
import edu.vt.ece.hw4.barriers.Barrier;
import edu.vt.ece.hw4.barriers.TTASBarrier;
import edu.vt.ece.hw4.bench.*;
import edu.vt.ece.hw4.locks.ALock;
import edu.vt.ece.hw4.locks.BackoffLock;
import edu.vt.ece.hw4.locks.Lock;
import edu.vt.ece.hw4.locks.MCSLock;

public class Benchmark {

    private static final String ALOCK = "ALock";
    private static final String BACKOFFLOCK = "BackoffLock";
    private static final String MCSLOCK = "MCSLock";

    public static void main(String[] args) throws Exception {
        String mode = args.length <= 0 ? "normal" : args[0];
        String lockClass = (args.length <= 1 ? ALOCK : args[1]);
        int threadCount = (args.length <= 2 ? 16 : Integer.parseInt(args[2]));
        int totalIters = (args.length <= 3 ? 64000 : Integer.parseInt(args[3]));
        int iters = totalIters / threadCount;

        run(args, mode, lockClass, threadCount, iters);

    }

    private static void run(String[] args, String mode, String lockClass, int threadCount, int iters) throws Exception {

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
            }

            switch (mode.trim().toLowerCase()) {
                case "normal":
                    final Counter counter = new SharedCounter(0, lock);
                    res = runNormal(counter, threadCount, iters);
                    break;
                case "empty":
                    runEmptyCS(lock, threadCount, iters);
                    break;
                case "long":
                    runLongCS(lock, threadCount, iters);
                    break;
                case "barrier":
                    Barrier b = null;
                    if(args[4].equals("ttasBarrier")){
                        b = new TTASBarrier(threadCount);
                    }else if (args[4].equals("arrayBarrier")){
                        b = new ArrayBarrier(threadCount);
                    }else{
                        b = new ArrayBarrier(threadCount);
                    }

                    // Start test:
                    runBarrier(b, threadCount, iters);
                    break;
                default:
                    throw new UnsupportedOperationException("Implement this");
            }
            results[i] = res;
        }

        double averageWithoutFirst = 0.0;
        double sum = 0;
        for(int i = 1 ; i < 5 ; i++){
            sum += results[i];
        }
        System.out.println(String.format("[Lock = %s][%s][Threads = %d][Iter per thread = %d]avg: " + (sum/4), lockClass, args[4], threadCount, iters));

    }

    private static double runBarrier(Barrier barrier , int threadCount, int iters) throws Exception {
        final BarrierThread[] threads = new BarrierThread[threadCount];


        long totalTime = 0;
        for(int it = 0 ; it < iters ; it++){
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
            barrier.reset();
        }


        double res = (totalTime / threadCount) / 1000000; // Result as milliseconds
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

    private static void runEmptyCS(Lock lock, int threadCount, int iters) throws Exception {

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

        System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
    }

    static void runLongCS(Lock lock, int threadCount, int iters) throws Exception {
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

        System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
    }
}
