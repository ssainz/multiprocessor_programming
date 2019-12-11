package edu.vt.ece.searchtree.redblacktree.flatcombine.v5;

import edu.vt.ece.searchtree.redblacktree.SearchTree;
import edu.vt.ece.searchtree.redblacktree.SearchTreeNode;
import edu.vt.ece.searchtree.redblacktree.flatcombine.ThreadID;
import edu.vt.ece.searchtree.redblacktree.gui.TreeGUI;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;


public class RBTBatch<Key extends Comparable<Key>, Value> implements SearchTree<Key, Value> {

    public static final boolean RED = true;
    public static final boolean BLACK = false;

    public Node<Key, Value> root;

    //ArrayList<Operation> ops = new ArrayList<>();



    public class Operation{
        public Key key;
        public Value value;
        public OperationType operationType;
        public Operation(OperationType operationType, Key key, Value value){
            this.key = key;
            this.value = value;
            this.operationType = operationType;
        }
        @Override
        public String toString(){
            return String.format("%s:{key:%s,val:%s}", operationType, key, value);
        }
    }

    public enum OperationType{
        PUT,
        DELETE
    }

    public RBTBatch() {
    }

    public boolean isEmpty(){
        return root == null;
    }

    public boolean isRed(Node node){
        if(node == null) return false;
        return node.color == RED;
    }

    public boolean putV2(Key key, Value val){

        //ops.add(new Operation(OperationType.PUT, key, val));

        if(key == null){
            return false;
        }

        root = put(root, key, val);
        root.color = BLACK;

        return true;
    }

    private Node<Key, Value> put(Node<Key, Value> node, Key key, Value val) {

        if(node == null) return new Node<Key, Value>(key, val, RED);

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

    private Node<Key, Value> rotateLeft(Node<Key, Value> node){
        Node x = node.right;
        node.right = x.left;
        x.left = node;
        x.color = x.left.color;
        x.left.color = RED;
        return x;
    }

    private Node<Key, Value> rotateRight(Node<Key, Value> node){
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
        if (key == null) return null;
        return get(root, key);
    }

    private Value get(Node<Key, Value> node, Key key) {
        while(node != null){
            int comparison = key.compareTo(node.key);
            if (comparison < 0)         node = node.left;
            else if (comparison > 0)    node = node.right;
            else{
                if(node.isDeleted){
                    return null;
                }else {
                    return node.value;
                }
            }
        }
        return null;
    }

    public boolean delete(Key key){
        //ops.add(new Operation(OperationType.DELETE, key, null));
        if( key == null)  return false;
        if( get(key) == null)  return false;

        if( !isRed(root.left) && !isRed(root.right)){
            root.color = RED;
        }

        root = delete(root, key);

        if(!isEmpty()) root.color = BLACK;

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
            /*if(!isRed(node.right) && node.left != null){
                if(node.left == null){
                    System.out.println(String.format("Thread[%s]:RBT: node.left is null ",ThreadID.get()));

                }
                if( !isRed(node.left.right)){
                    node = moveRedRight(node);
                }
            }*/
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


    private Node moveRedLeft(Node node) {
        flipColors(node);
        if(node.right != null && isRed(node.right.left)){
            node.right = rotateRight(node.right);
            node = rotateLeft(node);
            flipColors(node);
        }
        return node;
    }

    private Node moveRedRight(Node node) {
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

    private Node<Key, Value> min(Node node) {
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
    public SearchTreeNode<Key, Value> getRootV2() {
        return root;
    }

    public int size(){
        Stack<Node> stack = new Stack();
        int count = 0;

        if(this.root == null) return 0;

        stack.push(this.root);
        count ++;



        while(!stack.isEmpty()){
            Node<Key, Value> n = stack.pop();

            //System.out.println(n);

            if(n.left != null){
                stack.push(n.left);
                count++;
            }
            if(n.right != null){
                stack.push(n.right);
                count++;
            }
        }
        return count;
    }

    /**
     * Returns null if node does not exists
     * Or false if exists but is deleted
     * Or true if exists and is actually in the tree.
     */
    public Boolean exists(Key key) {
        if (key == null) return null;
        return exists(root, key);
    }

    public Boolean exists(Node<Key, Value> node, Key key){

        while(node != null){
            int comparison = key.compareTo(node.key);
            if (comparison < 0)         node = node.left;
            else if (comparison > 0)    node = node.right;
            else                        return node.isDeleted;
        }
        return null;
    }

    /**
     * Changes the node specified by key to the value to val
     */
    public void softPut(Key key, Value val) {
        if (key == null) return;
        softPut(root, key, val);
    }

    void softPut(Node<Key, Value> node, Key key, Value val){

        while(node != null){
            int comparison = key.compareTo(node.key);
            if (comparison < 0)         node = node.left;
            else if (comparison > 0)    node = node.right;
            else{
                node.value = val;
                node.isDeleted = false;
                return;
            }
        }
        return;
    }

    /**
     * Changes the node specified by key to the value to val
     */
    public void softDelete(Key key) {
        if (key == null) return;
        softDelete(root, key);
    }

    void softDelete(Node<Key, Value> node, Key key){

        while(node != null){
            int comparison = key.compareTo(node.key);
            if (comparison < 0)         node = node.left;
            else if (comparison > 0)    node = node.right;
            else{
                node.isDeleted = true;
                return;
            }
        }
        return;
    }



    public class Node <Key extends Comparable<Key>, Value> implements SearchTreeNode<Key, Value> {

        public volatile Key key;
        public volatile Value value;
        public volatile Node left;
        public volatile Node right;
        public volatile boolean color;
        public volatile int depth;
        public volatile boolean isDeleted;

        public Node(Key key, Value val, boolean color){
            this.key = key;
            this.value = val;
            this.color = color;
            this.isDeleted = false;
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

    public List<Node<Key, Value>> getDeletedNodes() {

        List<Node<Key, Value>> list = new ArrayList<>();

        Stack<Node> stack = new Stack();
        int count = 0;

        if(this.root == null) return list;

        stack.push(this.root);
        count ++;



        while(!stack.isEmpty()){
            Node<Key, Value> n = stack.pop();

            if(n.isDeleted){
                list.add(n);
            }

            if(n.left != null){
                stack.push(n.left);
                count++;
            }
            if(n.right != null){
                stack.push(n.right);
                count++;
            }
        }
        return list;
    }

    public static void main(String[] args){

        ThreadLocalRandom random = ThreadLocalRandom.current();

        RBTBatch<Integer, Integer> tree = new RBTBatch<>();
        for(int i = 0 ; i < 10 ; i++){
            int nextInsert = random.nextInt(100);
            System.out.println("a "+ nextInsert);
            tree.putV2(nextInsert, nextInsert);
            //tree.put(i, i);

        }

        System.out.println("Finished!");




    }
}
