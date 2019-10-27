package edu.vt.ece.hw4.locks;

import java.util.concurrent.atomic.AtomicInteger;

public class SpinSleepLock implements Lock {


    public SpinSleepLock(int maxSpin) {
    }

    @Override
    public boolean trylock() {
        return false;
    }

    @Override
    public void lock() {
    }

    @Override
    public void unlock() {

    }

}
