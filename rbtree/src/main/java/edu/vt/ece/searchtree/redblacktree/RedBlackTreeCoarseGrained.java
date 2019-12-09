package edu.vt.ece.searchtree.redblacktree;

import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

public class RedBlackTreeCoarseGrained<Key extends Comparable<Key>, Value> implements SearchTree<Key, Value>{

    public static final boolean RED = true;
    public static final boolean BLACK = false;

    private ReentrantLock lock = new ReentrantLock();

    Node<Key, Value> root;



    public RedBlackTreeCoarseGrained() {
    }

    public boolean isEmpty(){
        return root == null;
    }

    public boolean isRed(Node node){
        if(node == null) return false;
        return node.color == RED;
    }

    public boolean putV2(Key key, Value val){

        lock.lock();

        if(key == null){
            lock.unlock();
            return false;
        }

        root = put(root, key, val);
        root.color = BLACK;
        lock.unlock();
        return true;
    }

    private Node put(Node<Key, Value> node, Key key, Value val) {

        if(node == null) return new Node<Key, Value>(key, val, RED);

        int comparison = key.compareTo(node.key);
        if (comparison < 0)         node.left = put(node.left, key, val);
        else if (comparison > 0)    node.right = put(node.right, key, val);
        else                        node.value = val;

        // Balance back
        if( !isRed(node.left) && isRed(node.right))      node = rotateLeft(node);
        if(  isRed(node.left) && node.left != null && isRed(node.left.left))  node = rotateRight(node);
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
        if(node.left != null) {
            node.left.color = !node.left.color;
        }
        if(node.right != null) {
            node.right.color = !node.right.color;
        }
    }

    public Value get(Key key) {
        lock.lock();
        if (key == null){
            lock.unlock();
            return null;
        }
        Value val = get(root, key);
        lock.unlock();
        return val;
    }

    private Value get(Node<Key, Value> node, Key key) {
        while(node != null){
            int comparison = key.compareTo(node.key);
            if (comparison < 0)         node = node.left;
            else if (comparison > 0)    node = node.right;
            else                        return node.value;
        }
        return null;
    }

    public boolean delete(Key key){
        lock.lock();
        if( key == null){
            lock.unlock();
            return false;
        }
        if( get(key) == null){
            lock.unlock();
            return false;
        }

        if( !isRed(root.left) && !isRed(root.right)){
            root.color = RED;
        }

        root = delete(root, key);

        if(!isEmpty()) root.color = BLACK;

        lock.unlock();
        return true;
    }

    @Override
    public void end() {

    }

    private Node delete(Node<Key, Value> node, Key key) {

        if(key.compareTo(node.key) < 0){
            if(!isRed(node.left) && node.left != null && !isRed(node.left.left)){
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
            if(!isRed(node.right) && node.left != null && !isRed(node.left.right)){
                node = moveRedRight(node);
            }
            if(key.compareTo(node.key) == 0 ){
                Node<Key, Value> x = min(node.right);
                node.key = x.key;
                node.value = x.value;
                node.right = deleteMin(node.right);

            }else{
                node.right = delete(node.right, key);
            }
        }
        return balance(node);
    }

    private Node<Key, Value> moveRedLeft(Node<Key, Value> node) {
        flipColors(node);
        if(node.right != null && isRed(node.right.left)){
            node.right = rotateRight(node.right);
            node = rotateLeft(node);
            flipColors(node);
        }
        return node;
    }

    private Node<Key, Value> moveRedRight(Node<Key, Value> node) {
        flipColors(node);
        if(node.left != null && isRed(node.left.left)){
            node = rotateRight(node);
            flipColors(node);
        }
        return node;
    }

    public Key min(){
        if (isEmpty()) return null;
        return min(root).key;
    }

    private Node<Key, Value> min(Node<Key, Value> node) {
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

        if(!isRed(node.left) && node.left != null && !isRed(node.left.left)){
            node = moveRedLeft(node);
        }

        node.left = deleteMin(node.left);
        return balance(node);
    }

    private Node balance(Node node){
        if(isRed(node.right))                           node = rotateLeft(node);
        if(isRed(node.left) && node.left != null && isRed(node.left.left))   node = rotateRight(node);
        if(isRed(node.left) && isRed(node.right))       flipColors(node);

        return node;
    }

    public int maxDepth(){
        Stack<Node> stack = new Stack();
        this.root.depth = 0;

        stack.push(this.root);
        int maxDept = 0;

        while(!stack.isEmpty()){
            Node n = stack.pop();
            maxDept = n.depth > maxDept? n.depth : maxDept;

            if(n.left != null){
                n.left.depth = n.depth + 1;
                stack.push(n.left);
            }
            if(n.right != null){
                n.right.depth = n.depth + 1;
                stack.push(n.right);
            }
        }
        return maxDept;
    }

    @Override
    public Node<Key, Value> getRootV2() {
        return root;
    }


    public class Node <Key extends Comparable<Key>, Value> implements SearchTreeNode<Key, Value> {

        public volatile Key key;
        public volatile Value value;
        public volatile Node left;
        public volatile Node right;
        public volatile boolean color;
        public volatile int depth;

        public Node(Key key, Value val, boolean color){
            this.key = key;
            this.value = val;
            this.color = color;
        }

        @Override
        public SearchTreeNode<Key, Value> getLeft() {
            return left;
        }

        @Override
        public SearchTreeNode<Key, Value> getRight() {
            return right;
        }

        @Override
        public  boolean getColor() {
            return color;
        }

        @Override
        public Key getKey() {
            return key;
        }

        @Override
        public Value getValue() {
            return value;
        }


    }



    public static void main(String[] args){

        ThreadLocalRandom random = ThreadLocalRandom.current();

        RedBlackTreeNonThreadSafe<Integer, Integer> tree = new RedBlackTreeNonThreadSafe<>();
        for(int i = 0 ; i < 10 ; i++){
            int nextInsert = random.nextInt(100);
            System.out.println("a "+ nextInsert);
            tree.putV2(nextInsert, nextInsert);
            //tree.put(i, i);

        }

        System.out.println("Finished!");




    }
}
