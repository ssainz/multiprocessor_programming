package edu.vt.ece.hw5.bag;

import java.util.concurrent.CyclicBarrier;

public class Benchmark {

    public static void main(String[] args) throws Exception {
        final int threadCount = Integer.parseInt(args[0]);
        final int iters = Integer.parseInt(args[1]);

        final TestThread[] threads = new TestThread[threadCount];
        final LockFreeBag<Integer> bag = new LockFreeBag<>();
        final LockFreeList<Integer> list = new LockFreeList<>();
        final CyclicBarrier barrier = new CyclicBarrier(threadCount);

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new TestThread(iters, bag, list, barrier);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime1 = 0;
        long totalTime2 = 0;
        long maxTime1 = 0;
        long maxTime2 = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime1 += threads[t].getElapsedTime1();
            totalTime2 += threads[t].getElapsedTime2();
            maxTime1 = Math.max(maxTime1, threads[t].getElapsedTime1());
            maxTime2 = Math.max(maxTime2, threads[t].getElapsedTime2());
        }

        System.out.println("Bag: Throughput is " + (iters*threadCount) / (maxTime1*0.001) + "ops/s");
        System.out.println("Bag: Average time per thread is " + totalTime1 / threadCount + "ms");

        System.out.println("List: Throughput is " + (iters*threadCount) / (maxTime2*0.001) + "ops/s");
        System.out.println("List: Average time per thread is " + totalTime2 / threadCount + "ms");
    }
}
