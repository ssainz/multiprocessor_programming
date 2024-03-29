package edu.vt.ece.hw4.backoff;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class ExponentialBackoff implements Backoff {

    private int unsuccessfullAttemps = 0;
    static int MAX_SLEEP_MILLISECONDS = 600000; // 10 minutes

    @Override
    public void backoff() throws InterruptedException {
        int unsuccessAtt = ++unsuccessfullAttemps;

        long sleepTime = 1 << unsuccessAtt;

        // Sleep for max:
        //sleepTime = Math.min(MAX_SLEEP_MILLISECONDS, sleepTime);

        TimeUnit.MILLISECONDS.sleep(sleepTime);

    }

}
