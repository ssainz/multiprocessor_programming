package edu.vt.ece.searchtree.redblacktree;

import java.util.concurrent.ThreadLocalRandom;

public class RedBlackTreeNonThreadSafe<Key extends Comparable<Key>, Value> implements SearchTree<Key, Value>{

    public static final boolean RED = true;
    public static final boolean BLACK = false;

    Node root;

    class Node{
        Key key;
        Value value;
        Node left, right;
        boolean color;

        public Node(Key key, Value val, boolean color){
            this.key = key;
            this.value = val;
            this.color = color;
        }
    }

    public RedBlackTreeNonThreadSafe() {
    }

    public boolean isEmpty(){
        return root == null;
    }

    public boolean isRed(Node node){
        if(node == null) return false;
        return node.color == RED;
    }

    public boolean put(Key key, Value val){

        if(key == null){
            return false;
        }

        root = put(root, key, val);
        root.color = BLACK;

        return true;
    }

    private Node put(Node node, Key key, Value val) {

        if(node == null) return new Node(key, val, RED);

        int comparison = key.compareTo(node.key);
        if (comparison < 0)         node.left = put(node.left, key, val);
        else if (comparison > 0)    node.right = put(node.right, key, val);
        else                        node.value = val;

        // Balance back
        if( !isRed(node.left) && isRed(node.right))      node = rotateLeft(node);
        if(  isRed(node.left) && isRed(node.left.left))  node = rotateRight(node);
        if(  isRed(node.left) && isRed(node.right) )     flipColors(node);

        return node;
    }

    private Node rotateLeft(Node node){
        Node x = node.right;
        node.right = x.left;
        x.left = node;
        x.color = x.left.color;
        x.left.color = RED;
        return x;
    }

    private Node rotateRight(Node node){
        Node x = node.left;
        node.left = x.right;
        x.right = node;
        x.color = x.right.color;
        x.right.color = RED;
        return x;
    }

    private void flipColors(Node node){
        node.color = !node.color;
        node.left.color = !node.left.color;
        node.right.color = !node.right.color;
    }

    public Value get(Key key) {
        if (key == null) return null;
        return get(root, key);
    }

    private Value get(Node node, Key key) {
        while(node != null){
            int comparison = key.compareTo(node.key);
            if (comparison < 0)         node = node.left;
            else if (comparison > 0)    node = node.right;
            else                        return node.value;
        }
        return null;
    }

    public boolean delete(Key key){
        if( key == null)  return false;
        if( get(key) == null)  return false;

        if( !isRed(root.left) && !isRed(root.right)){
            root.color = RED;
        }

        root = delete(root, key);

        if(!isEmpty()) root.color = BLACK;

        return true;
    }

    private Node delete(Node node, Key key) {

        if(key.compareTo(node.key) < 0){
            if(!isRed(node.left) && !isRed(node.left.left)){
                node = moveRedLeft(node);
            }
            node.left = delete(node.left, key);
        }else{
            if(isRed(node.left)){
                node = rotateRight(node);
            }
            if(key.compareTo(node.key) == 0 && (node.right == null)){
                return null;
            }
            if(!isRed(node.right) && !isRed(node.left.right)){
                node = moveRedRight(node);
            }
            if(key.compareTo(node.key) == 0 ){
                Node x = min(node.right);
                node.key = x.key;
                node.value = x.value;
                node.right = deleteMin(node.right);

            }else{
                node.right = delete(node.right, key);
            }
        }
        return balance(node);
    }

    private Node moveRedLeft(Node node) {
        flipColors(node);
        if(isRed(node.right.left)){
            node.right = rotateRight(node.right);
            node = rotateLeft(node);
            flipColors(node);
        }
        return node;
    }

    private Node moveRedRight(Node node) {
        flipColors(node);
        if(isRed(node.left.left)){
            node = rotateRight(node);
            flipColors(node);
        }
        return node;
    }

    public Key min(){
        if (isEmpty()) return null;
        return min(root).key;
    }

    private Node min(Node node) {
        if(node.left == null) return node;
        return min(node.left);
    }

    public boolean deleteMin(){
        if (isEmpty()) return false;
        if(!isRed(root.left) && !isRed(root.right)){
            root.color = RED;
        }
        root = deleteMin(root);
        if(!isEmpty()) root.color = BLACK;

        return true;
    }

    private Node deleteMin(Node node) {
        if(node.left == null)
            return null;

        if(!isRed(node.left) && !isRed(node.left.left)){
            node = moveRedLeft(node);
        }

        node.left = deleteMin(node.left);
        return balance(node);
    }

    private Node balance(Node node){
        if(isRed(node.right))                           node = rotateLeft(node);
        if(isRed(node.left) && isRed(node.left.left))   node = rotateRight(node);
        if(isRed(node.left) && isRed(node.right))       flipColors(node);

        return node;
    }


    public static void main(String[] args){

        ThreadLocalRandom random = ThreadLocalRandom.current();

        RedBlackTreeNonThreadSafe<Integer, Integer> tree = new RedBlackTreeNonThreadSafe<>();
        for(int i = 0 ; i < 10 ; i++){
            int nextInsert = random.nextInt(100);
            System.out.println("a "+ nextInsert);
            tree.put(nextInsert, nextInsert);
            //tree.put(i, i);

        }

        System.out.println("Finished!");




    }
}
