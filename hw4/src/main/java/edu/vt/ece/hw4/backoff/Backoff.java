package edu.vt.ece.hw4.backoff;

/**
 * @author Balaji Arun
 */
public interface Backoff {

    void backoff() throws InterruptedException;

}
