package edu.vt.ece.hw5.bag;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

public class TestThread extends Thread {
    private final LockFreeBag<Integer> bag;
    private final LockFreeList<Integer> list;
    private final CyclicBarrier barrier;

    private long elapsed1;
    private long elapsed2;

    private int iter;

    TestThread(int iter, LockFreeBag<Integer> bag, LockFreeList<Integer> list, CyclicBarrier barrier) {
        this.iter = iter;
        this.bag = bag;
        this.list = list;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        long start = System.currentTimeMillis();
        for (int i = 0; i < iter; i++) {
            if (random.nextInt(100) < 50) {
                this.bag.add(i);
            } else {
                this.bag.tryRemoveAny();
            }
        }
        elapsed1 = System.currentTimeMillis() - start;

        try {
            this.barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        start = System.currentTimeMillis();
        for (int i = 0; i < iter; i++) {
            int rand = random.nextInt(100);
            if (rand < 50) {
                this.list.add(rand);
            } else {
                this.list.remove(rand % 50);
            }
        }
        elapsed2 = System.currentTimeMillis() - start;
    }

    long getElapsedTime1() {
        return this.elapsed1;
    }

    long getElapsedTime2() {
        return this.elapsed2;
    }
}
