package edu.vt.ece.hw4.locks;

import java.util.concurrent.atomic.AtomicBoolean;

public class TestAndSpinLock implements Lock {

    private AtomicBoolean state = new AtomicBoolean(false);

    public void lock() {
        while (state.getAndSet(true)) {};  // ece.vt.edu.spin

    }

    public void unlock() {
        state.set(false);
    }

}
