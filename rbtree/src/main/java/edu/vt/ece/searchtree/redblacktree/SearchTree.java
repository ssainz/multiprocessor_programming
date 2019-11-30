package edu.vt.ece.searchtree.redblacktree;

public interface SearchTree <Key extends Comparable<Key>, Value> {

    public boolean put(Key key, Value val);

    public Value get(Key key);

    public boolean delete(Key key);
}
