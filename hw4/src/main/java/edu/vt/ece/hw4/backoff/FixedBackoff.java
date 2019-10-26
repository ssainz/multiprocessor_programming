package edu.vt.ece.hw4.backoff;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class FixedBackoff implements Backoff {

    private int unsuccessfullAttemps = 0;
    static long MAX_SLEEP_MILLISECONDS = 600000; // 10 minutes
    static long[] fixedDelays = {1, 3, 10, 20, 50, 100, 200, 500, 1000, 3033, 5000};


    @Override
    public void backoff() throws InterruptedException {

        int unsuccessAttempt = ++unsuccessfullAttemps;

        long sleepTime = (unsuccessAttempt -1) >= fixedDelays.length ? fixedDelays[fixedDelays.length - 1] : fixedDelays[unsuccessAttempt - 1];

        // Sleep for max:
        // long sleepTime = Math.min(MAX_SLEEP_MILLISECONDS, sleepTime);


        TimeUnit.MILLISECONDS.sleep(sleepTime);

    }

}
