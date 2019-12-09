package edu.vt.ece.searchtree.redblacktree.flatcombine.v3;

/**
 * @author Balaji Arun
 */
public interface Backoff {

    void backoff() throws InterruptedException;

    void reset();
}