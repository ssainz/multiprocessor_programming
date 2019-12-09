package edu.vt.ece.searchtree.redblacktree.notworking;

import java.util.concurrent.atomic.AtomicBoolean;

public class RedBlackTreeLockFreeNode<Key extends Comparable<Key>, Value> {
    private Value value;
    private Key key;
    private RedBlackTreeLockFreeNode left;
    private RedBlackTreeLockFreeNode right;
    private RedBlackTreeLockFreeNode parent;
    private boolean isRed;
    public AtomicBoolean flag;

    public RedBlackTreeLockFreeNode(){
        this.value = null;
        this.key = null;
        this.left = null;
        this.right = null;
        this.parent = null;
        this.isRed = false;
        this.flag = new AtomicBoolean(false);
    }

    public RedBlackTreeLockFreeNode(Key key, Value value){
        this.value = value;
        this.key = key;
        this.left = new RedBlackTreeLockFreeNode<Key, Value>();
        this.right = new RedBlackTreeLockFreeNode<Key, Value>();
        //this.left = null;
        //this.right = null;
        this.parent = null;
        this.isRed = true;
        this.flag = new AtomicBoolean(false);
    }

    public Key getKey(){
        return key;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public RedBlackTreeLockFreeNode<Key, Value> getLeft() {
        return left;
    }

    public void setLeft(RedBlackTreeLockFreeNode<Key, Value> left) {
        this.left = left;
    }

    public RedBlackTreeLockFreeNode<Key, Value> getRight() {
        return right;
    }

    public void setRight(RedBlackTreeLockFreeNode<Key, Value> right) {
        this.right = right;
    }

    public RedBlackTreeLockFreeNode<Key, Value> getParent() {
        return parent;
    }

    public void setParent(RedBlackTreeLockFreeNode<Key, Value> parent) {
        this.parent = parent;
    }

    public boolean isRed() {
        return isRed;
    }

    public void setRed(boolean isRed) {
        this.isRed = isRed;
    }

    public int height(){
        if(this==null)return 0;
        return (1+ Math.max(this.getLeft().height(),this.getRight().height()));
    }
    public void displayNode(RedBlackTreeLockFreeNode<Key, Value> n) {
        System.out.print(n.getValue() + " ");
    }
}