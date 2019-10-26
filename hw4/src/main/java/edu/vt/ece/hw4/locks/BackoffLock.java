package edu.vt.ece.hw4.locks;

import edu.vt.ece.hw4.backoff.Backoff;
import edu.vt.ece.hw4.backoff.BackoffFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class BackoffLock implements Lock {

    private AtomicBoolean state;
    private final String backoffStrategy;

    public BackoffLock(String backoffStrategy) {
        this.state = new AtomicBoolean(false);
        this.backoffStrategy = backoffStrategy;
    }

    @Override
    public void lock() {
        Backoff backoff = BackoffFactory.getBackoff(this.backoffStrategy);
        while (true) {
            while (state.get()) {
            }
            if (!state.getAndSet(true)) { // try to acquire lock
                return;
            } else {            // backoff on failure
                try {
                    backoff.backoff();
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    @Override
    public void unlock() {
        state.set(false);
    }
}
