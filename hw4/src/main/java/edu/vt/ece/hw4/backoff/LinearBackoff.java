package edu.vt.ece.hw4.backoff;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class LinearBackoff implements Backoff {

    private int unsuccessfullAttemps = 0;
    static long MAX_SLEEP_MILLISECONDS = 600000; // 10 minutes


    @Override
    public void backoff() throws InterruptedException {

        int unsuccessAttempt = ++unsuccessfullAttemps;

        long sleepTime = unsuccessAttempt * 100;

        // Sleep for max:
        // long sleepTime = Math.min(MAX_SLEEP_MILLISECONDS, sleepTime);


        TimeUnit.MILLISECONDS.sleep(sleepTime);

    }

}
