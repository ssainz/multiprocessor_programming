package edu.vt.ece.hw4.locks;

public interface Lock {
    boolean trylock();
    void lock();
    void unlock();
}
