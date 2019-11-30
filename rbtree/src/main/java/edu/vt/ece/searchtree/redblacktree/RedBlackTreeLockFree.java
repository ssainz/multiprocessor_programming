package edu.vt.ece.searchtree.redblacktree;

import java.util.ArrayList;

public class RedBlackTreeLockFree<Key extends Comparable<Key>, Value> implements SearchTree<Key,Value>{
    public int size = 0;
    public RedBlackTreeLockFreeNode<Key, Value> root;



    public RedBlackTreeLockFree() {
        this.root = new RedBlackTreeLockFreeNode();
    }

    @Override
    public boolean put(Key key, Value val) {
        RedBlackTreeLockFreeNode<Key, Value> insertedNode = new RedBlackTreeLockFreeNode<>(key, val);
        RedBlackTreeLockFreeNode<Key, Value> temp1, temp2;
        insertedNode.flag.set(true);
        while (true) {
            temp1 = this.root;
            temp2 = null;
            while (temp1.getKey() != null) {
                temp2 = temp1;
                int comparison = temp1.getKey().compareTo(key);
                if (comparison < 0) {
                    temp1 = temp1.getLeft();
                } else if(comparison > 0){
                    temp1 = temp1.getRight();
                } else{
                    return false; // key already exists
                }
            }
            if (!setupLocalAreaForInsert(temp2)) {
                temp2.flag.set(false);
                continue;
            } else {
                break;
            }
        }

        insertedNode.setParent(temp2);
        if (temp2 == null) {
            this.root = insertedNode;
        } else{
            int comparison = temp2.getKey().compareTo(key);
            if (comparison < 0) {
                temp2.setLeft(insertedNode);
            } else {
                temp2.setRight(insertedNode);
            }
        }
        insertedNode.getLeft().setParent(insertedNode);
        insertedNode.getRight().setParent(insertedNode);
        insertedNode.setRed(true);
        rbInsertFixup(insertedNode);
        return false;
    }

    public Value get(Key key) {
        if (root == null) {
            return null;
        }
        RedBlackTreeLockFreeNode<Key, Value> temp = root;
        while (temp != null && temp.getKey() != null) {
            int comparison = temp.getKey().compareTo(key);
            if (comparison < 0) {
                temp = temp.getLeft();
            }else if (comparison > 0) {
                temp = temp.getRight();
            } else{
                return temp.getValue();
            }
        }
        return temp == null ? null : temp.getValue();
    }

    @Override
    public boolean delete(Key key) {
        return false;
    }



    private boolean setupLocalAreaForInsert(RedBlackTreeLockFreeNode<Key, Value> x) {
        if (x == null) {
            return true;
        }
        RedBlackTreeLockFreeNode<Key, Value> parent = x.getParent();
        RedBlackTreeLockFreeNode<Key, Value> uncle;
        if (parent == null) return true;
        if (!x.flag.compareAndSet(false, true)) {
            return false;
        }
        if (!parent.flag.compareAndSet(false, true)) {
            return false;
        }
        if (parent != x.getParent()) {
            parent.flag.set(false);
            return false;
        }
        if (x == x.getParent().getLeft()) {
            uncle = x.getParent().getRight();
        } else {
            uncle = x.getParent().getLeft();
        }
        if (uncle != null && !uncle.flag.compareAndSet(false, true)) {
            x.getParent().flag.set(false);
            return false;
        }
        return true;
    }

    private void rbInsertFixup(RedBlackTreeLockFreeNode<Key, Value> x) {
        RedBlackTreeLockFreeNode<Key,Value> temp, parent, uncle = null, gradparent = null;
        parent = x.getParent();
        ArrayList<RedBlackTreeLockFreeNode<Key,Value>> local_area = new ArrayList<RedBlackTreeLockFreeNode<Key,Value>>();
        local_area.add(x);
        local_area.add(parent);

        if (parent != null) {
            gradparent = parent.getParent();
        }

        if (gradparent != null) {
            if (gradparent.getLeft() == parent) {
                uncle = gradparent.getRight();
            } else {
                uncle = gradparent.getLeft();
            }
        }

        local_area.add(uncle);
        local_area.add(gradparent);

        while (x.getParent()!= null && x.getParent().isRed()) {
            parent = x.getParent();
            gradparent = gradparent.getParent();

            if (x.getParent() == x.getParent().getParent().getLeft()) {
                temp = x.getParent().getParent().getRight();
                uncle = temp;
                local_area.add(x);
                local_area.add(parent);
                local_area.add(gradparent);
                local_area.add(uncle);

                if (temp.isRed()) {
                    x.getParent().setRed(false);
                    temp.setRed(false);
                    x.getParent().getParent().setRed(true);
                    x = moveLocalAreaUpward(x, local_area);
                } else {
                    if (x == x.getParent().getRight()) {
                        // Case 2
                        x = x.getParent();
                        leftRotate(x);
                    }
                    // Case 3
                    x.getParent().setRed(false);
                    x.getParent().getParent().setRed(true);
                    rightRotate(x.getParent().getParent());
                }
            } else {
                temp = x.getParent().getParent().getLeft();
                uncle = temp;

                local_area.add(x);
                local_area.add(parent);
                local_area.add(gradparent);
                local_area.add(uncle);

                if (temp.isRed()) {
                    // Case 1
                    x.getParent().setRed(false);
                    temp.setRed(false);
                    x.getParent().getParent().setRed(true);
                    x = moveLocalAreaUpward(x, local_area);
                } else {
                    if (x == x.getParent().getLeft()) {
                        // Case 2
                        x = x.getParent();
                        rightRotate(x);
                    }
                    // Case 3
                    x.getParent().setRed(false);
                    x.getParent().getParent().setRed(true);
                    leftRotate(x.getParent().getParent());
                }
            }
        }

        this.root.setRed(false);

        for (RedBlackTreeLockFreeNode<Key, Value> node : local_area) {
            if (node!= null) node.flag.set(false);
        }
    }

    private RedBlackTreeLockFreeNode moveLocalAreaUpward(RedBlackTreeLockFreeNode x, ArrayList<RedBlackTreeLockFreeNode<Key,Value>> working) {
        RedBlackTreeLockFreeNode<Key, Value> parent = x.getParent();
        RedBlackTreeLockFreeNode<Key, Value> grandparent = parent.getParent();
        RedBlackTreeLockFreeNode<Key, Value> uncle;
        if (parent == grandparent.getLeft()){
            uncle = grandparent.getRight();
        } else {
            uncle = grandparent.getLeft();
        }

        RedBlackTreeLockFreeNode<Key, Value> updated_x, updated_parent = null, updated_uncle = null, updated_grandparent = null;
        updated_x = grandparent;

        while (true && updated_x.getParent()!= null) {
            updated_parent = updated_x.getParent();
            if (!updated_parent.flag.compareAndSet(false, true)) {
                continue;
            }
            updated_grandparent = updated_parent.getParent();
            if (updated_grandparent == null) break;
            if (!updated_grandparent.flag.compareAndSet(false, true)) {
                updated_parent.flag.set(false);
                continue;
            }
            if (updated_parent == updated_grandparent.getLeft()) {
                updated_uncle = updated_grandparent.getRight();
            } else {
                updated_uncle = updated_grandparent.getLeft();
            }

            if (updated_uncle != null && !updated_uncle.flag.compareAndSet(false, true)) {
                updated_grandparent.flag.set(false);
                updated_parent.flag.set(false);
                continue;
            }
            break;
        }

        working.add(updated_x);
        working.add(updated_parent);
        working.add(updated_grandparent);
        working.add(updated_uncle);

        return updated_x;
    }

    private  void leftRotate(RedBlackTreeLockFreeNode<Key, Value> x) {
        if (x == null) return;
        RedBlackTreeLockFreeNode<Key, Value> y = x.getRight();
        x.setRight(y.getLeft());
        if (y.getLeft() != null) {
            y.getLeft().setParent(x);
        }
        y.setParent(x.getParent());
        if (x.getParent() == null) this.root = y;
        else{
            if (x == x.getParent().getLeft())
                x.getParent().setLeft(y);
            else
                x.getParent().setRight(y);
        }
        y.setLeft(x);
        x.setParent(y);
    }

    private void rightRotate(RedBlackTreeLockFreeNode<Key, Value> y) {
        if (y == null) return;
        RedBlackTreeLockFreeNode x = y.getLeft();
        y.setLeft(x.getRight());
        if (x.getRight() != null) {
            x.getRight().setParent(y);
        }
        x.setParent(y.getParent());
        if (y.getParent() == null) this.root = x;
        else{
            if (y == y.getParent().getLeft())
                y.getParent().setLeft(x);
            else
                y.getParent().setRight(x);
        }
        x.setRight(y);
        y.setParent(x);
    }


    public int getheight(RedBlackTreeLockFreeNode<Key, Value> root) {
        if (root == null)
            return 0;
        return Math.max(getheight(root.getLeft()), getheight(root.getRight())) + 1;
    }


    public void preOrder(RedBlackTreeLockFreeNode<Key, Value> n ){

        if (n == null)
            return;
        //n.displayNode(n);
        preOrder(n.getLeft());
        preOrder(n.getRight());
    }

    public void breadth(RedBlackTreeLockFreeNode<Key, Value> n ){

        if (n == null)
            return;
        ///n.displayNode(n);
        preOrder(n.getLeft());
        preOrder(n.getRight());
    }

}