package edu.vt.ece.hw4.backoff;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class FibonacciBackoff implements Backoff {

    private int unsuccessfullAttemps = 0;
    static long MAX_SLEEP_MILLISECONDS = 600000; // 10 minutes

    @Override
    public void backoff() throws InterruptedException {

        int unsuccessAttempt = ++unsuccessfullAttemps;

        long sleepTime = calculateFibo(unsuccessAttempt);

        // Sleep for max:
        //long sleepTime = Math.min(MAX_SLEEP_MILLISECONDS, fibo);

        TimeUnit.MILLISECONDS.sleep(sleepTime);

    }

    public long calculateFibo(int i){

        long prev = 0;
        long curr = 1;
        int iter = 0;
        while(iter < i){
            long newCurr = prev + curr;
            prev = curr;
            curr = newCurr;
            iter ++;
        }
        return curr;
    }
}
