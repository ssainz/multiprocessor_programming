package edu.vt.ece.searchtree.redblacktree.notworking;

import java.util.concurrent.atomic.AtomicBoolean;

public class RedBlackTreeNodeLockFreeKim <Key extends Comparable<Key>, Value> {
    public Value value;
    public Key key;
    public RedBlackTreeNodeLockFreeKim left;
    public RedBlackTreeNodeLockFreeKim right;
    public RedBlackTreeNodeLockFreeKim parent;
    public boolean isRed;
    public AtomicBoolean flag;

    public RedBlackTreeNodeLockFreeKim(){
        this.value = null;
        this.key = null;
        this.left = null;
        this.right = null;
        this.parent = null;
        this.isRed = false;
        this.flag = new AtomicBoolean(false);
    }

    public RedBlackTreeNodeLockFreeKim(Key key, Value value){
        this.value = value;
        this.key = key;
        this.left = new RedBlackTreeNodeLockFreeKim<Key, Value>();
        this.right = new RedBlackTreeNodeLockFreeKim<Key, Value>();
        //this.left = null;
        //this.right = null;
        this.parent = null;
        this.isRed = true;
        this.flag = new AtomicBoolean(false);
    }


    public int height(){
        if(this==null)return 0;
        return (1+ Math.max(this.left.height(),this.right.height()));
    }
    public void displayNode(RedBlackTreeNodeLockFreeKim<Key, Value> n) {
        System.out.print(n.value + " ");
    }
}
