package edu.vt.ece.searchtree.redblacktree;

public interface SearchTreeNode <Key extends Comparable<Key>, Value> {



    public SearchTreeNode<Key, Value> getLeft();
    public SearchTreeNode<Key, Value> getRight();
    public boolean getColor();

    Key getKey();

    Value getValue();

}
