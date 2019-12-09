package edu.vt.ece.searchtree.redblacktree;

import edu.vt.ece.searchtree.redblacktree.flatcombine.ThreadID;
import edu.vt.ece.searchtree.redblacktree.gui.TreeGUI;

import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

public class RedBlackTreeVolatile<Key extends Comparable<Key>, Value> implements SearchTree<Key, Value>{

    public static final boolean RED = true;
    public static final boolean BLACK = false;

    //public volatile Node<Key, Value> root;


    public final AtomicReference<Node<Key,Value>> root = new AtomicReference<>();
    ArrayList<Operation> ops = new ArrayList<>();



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

    public RedBlackTreeVolatile() {
    }

    public boolean isEmpty(){
        return root.get() == null;
    }

    public boolean isRed(Node node){
        if(node == null) return false;
        return node.color == RED;
    }

    public boolean putV2(Key key, Value val){

        System.out.println(String.format("Thread[%s] ENTER PUT key [%s]", ThreadID.get(), key));
        ops.add(new Operation(OperationType.PUT, key, val));

        if(key == null){
            System.out.println(String.format("Thread[%s] EXIT PUT key [%s]", ThreadID.get(), key));
            return false;
        }

        root.set(put(root.get(), key, val));
        root.get().color = BLACK;
        System.out.println(String.format("Thread[%s] EXIT PUT key [%s]", ThreadID.get(), key));
        return true;
    }

    private Node<Key, Value> put(Node<Key, Value> node, Key key, Value val) {


        if(node == null) {

            return new Node<Key, Value>(key, val, RED);
        }

        int comparison = key.compareTo(node.key);
        if (comparison < 0)         node.left.set(put(node.left.get(), key, val));
        else if (comparison > 0)    node.right.set(put(node.right.get(), key, val));
        else                        node.value = val;

        // Balance back

        if( !isRed(node.left.get()) && isRed(node.right.get()))      node = rotateLeft(node);
        if(  isRed(node.left.get()) && isRed(node.left.get().left.get()))  node = rotateRight(node);
        if(  isRed(node.left.get()) && isRed(node.right.get()) )     flipColors(node);

        return node;
    }

    private Node<Key, Value> rotateLeft(Node<Key, Value> node){
        Node<Key, Value> x = node.right.get();
        node.right.set(x.left.get());
        x.left.set(node) ;
        x.color = x.left.get().color;
        x.left.get().color = RED;
        return x;
    }

    private Node<Key, Value> rotateRight(Node<Key, Value> node){
        Node<Key, Value> x = node.left.get();
        node.left.set(x.right.get());
        x.right.set(node);
        x.color = x.right.get().color;
        x.right.get().color = RED;
        return x;
    }

    private void flipColors(Node<Key, Value> node){
        node.color = !node.color;
        node.left.get().color = !node.left.get().color;
        node.right.get().color = !node.right.get().color;
    }

    public Value get(Key key) {
        if (key == null) return null;
        return get(root.get(), key);
    }

    private Value get(Node<Key, Value> node, Key key) {
        while(node != null){
            int comparison = key.compareTo(node.key);
            if (comparison < 0)         node = node.left.get();
            else if (comparison > 0)    node = node.right.get();
            else                        return node.value;
        }
        return null;
    }

    public boolean delete(Key key){
        System.out.println(String.format("Thread[%s] ENTER DELETE key [%s]", ThreadID.get(), key));
        ops.add(new Operation(OperationType.DELETE, key, null));
        if( key == null)  {
            System.out.println(String.format("Thread[%s] EXIT DELETE key [%s]", ThreadID.get(), key));
            return false;
        }
        if( get(key) == null)  {
            System.out.println(String.format("Thread[%s] EXIT DELETE key [%s]", ThreadID.get(), key));
            return false;
        }

        if( !isRed(root.get().left.get()) && !isRed(root.get().right.get())){
            root.get().color = RED;
        }

        root.set(delete(root.get(), key));

        if(!isEmpty()) root.get().color = BLACK;
        System.out.println(String.format("Thread[%s] EXIT DELETE key [%s]", ThreadID.get(), key));
        return true;
    }

    @Override
    public void end() {

    }

    private Node delete(Node<Key, Value> node, Key key) {

        if(key.compareTo(node.key) < 0){
            if(!isRed(node.left.get()) && node.left.get() != null && !isRed(node.left.get().left.get())){
                node = moveRedLeft(node);
            }
            node.left.set(delete(node.left.get(), key));
        }else{
            if(isRed(node.left.get())){
                node = rotateRight(node);
            }
            if(key.compareTo(node.key) == 0 && (node.right.get() == null)){
                return null;
            }

            /*if(!isRed(node.right) && !isRed(node.left.right)){
                node = moveRedRight(node);
            }*/
            if(!isRed(node.right.get()) ){
                if(node.left.get() == null){

                    int size = this.size();

                    System.out.println(String.format("Thread[%s]:RBT: node.left is null ", ThreadID.get()));
                    System.out.println(String.format("Thread[%s]:RBT: key looking for is %s ",ThreadID.get(), key.toString()));
                    Value v = get(key);
                    System.out.println(String.format("Thread[%s]:RBT: key.val is %s ",ThreadID.get(), v));
                    System.out.println(String.format("Thread[%s]:RBT: node.key is %s ",ThreadID.get(), node.key));
                    System.out.println(String.format("Thread[%s]:RBT: size is %d ",ThreadID.get(), size));
                    System.out.println(String.format("%s",ops.toString()));

                    // build another coarse grained tree and see if we get same thing :P


//                    TreeGUI gui = new TreeGUI<Key, Value>(this, " orig");
//
//                    SearchTree<Key, Value> benchmarkTree = buildTreeFromOps(ops, new RedBlackTreeCoarseGrained<Key,Value>());
//                    TreeGUI gui2 = new TreeGUI<Key, Value>(benchmarkTree, " test");
//
//                    try {
//                        Thread.sleep(10000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                    benchmarkTree.delete(ops.get(ops.size()-1).key);
//
//                    System.out.println(" REMOVED SUCCESSFULLY FROM benchmark tree");
//                    TreeGUI gui3 = new TreeGUI<Key, Value>(benchmarkTree, " test after removal");

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    RedBlackTreeNonThreadSafe<Key,Value> t4 = new RedBlackTreeNonThreadSafe<Key,Value>();
                    SearchTree<Key, Value> volatileTree = buildTreeFromOps(ops, t4);

                    int sizeV = t4.size();

                    System.out.println("ThreadUnsafe size " + sizeV + " old one size " + size);

                    System.out.println("NOW DELETING");

                    t4.delete(ops.get(ops.size()-1).key);

                    //TreeGUI gui4 = new TreeGUI<Key, Value>(volatileTree, " volatile again single threaded");

                }
                if( !isRed(node.left.get().right.get())){
                    node = moveRedRight(node);
                }
            }
            if(key.compareTo(node.key) == 0 ){
                Node<Key, Value> x = min(node.right.get());
                node.key = x.key;
                node.value = x.value;
                node.right.set(deleteMin(node.right.get()));

            }else{
                node.right.set(delete(node.right.get(), key));
            }
        }
        return balance(node);
    }

    private SearchTree<Key,Value> buildTreeFromOps(ArrayList<Operation> ops, SearchTree<Key,Value> testTree) {


        for(int i = 0 ; i < ops.size() -1 ; i++){ // skip last delete
            Operation o = ops.get(i);
            if(o.operationType == OperationType.DELETE){
                testTree.delete(o.key);
            }else if(o.operationType == OperationType.PUT){
                testTree.putV2(o.key, o.value);
            }
        }
        System.out.println("Operation " + ops.get(ops.size() - 1) + " not performed!");
        return testTree;
    }

    private Node moveRedLeft(Node<Key, Value> node) {
        flipColors(node);
        if(isRed(node.right.get().left.get())){
            node.right.set(rotateRight(node.right.get()));
            node = rotateLeft(node);
            flipColors(node);
        }
        return node;
    }

    private Node moveRedRight(Node<Key, Value> node) {
        flipColors(node);
        if(isRed(node.left.get().left.get())){
            node = rotateRight(node);
            flipColors(node);
        }
        return node;
    }

    public Key min(){
        if (isEmpty()) return null;
        return min(root.get()).key;
    }

    private Node<Key, Value> min(Node<Key, Value> node) {
        if(node.left.get() == null) return node;
        return min(node.left.get());
    }

    public boolean deleteMin(){
        if (isEmpty()) return false;
        if(!isRed(root.get().left.get()) && !isRed(root.get().right.get())){
            root.get().color = RED;
        }
        root.set(deleteMin(root.get()));
        if(!isEmpty()) root.get().color = BLACK;

        return true;
    }

    private Node deleteMin(Node<Key, Value> node) {
        if(node.left.get() == null)
            return null;

        if(!isRed(node.left.get()) && !isRed(node.left.get().left.get())){
            node = moveRedLeft(node);
        }

        node.left.set(deleteMin(node.left.get()));
        return balance(node);
    }

    private Node balance(Node<Key, Value> node){
        if(isRed(node.right.get()))                           node = rotateLeft(node);
        if(isRed(node.left.get()) && isRed(node.left.get().left.get()))   node = rotateRight(node);
        if(isRed(node.left.get()) && isRed(node.right.get()))       flipColors(node);

        return node;
    }


    public int maxDepth(){
        Stack<Node> stack = new Stack();
        this.root.get().depth = 0;

        stack.push(this.root.get());
        int maxDept = 0;

        while(!stack.isEmpty()){
            Node<Key, Value> n = stack.pop();
            maxDept = n.depth > maxDept? n.depth : maxDept;

            if(n.left.get() != null){

                n.left.get().depth = n.depth + 1;
                stack.push(n.left.get());
            }
            if(n.right.get() != null){
                n.right.get().depth = n.depth + 1;
                stack.push(n.right.get());
            }
        }
        return maxDept;
    }

    @Override
    public Node<Key, Value> getRootV2() {
        return root.get();
    }

    public int size(){
        Stack<Node> stack = new Stack();
        int count = 0;

        if(this.root.get() == null) return 0;

        stack.push(this.root.get());
        count ++;



        while(!stack.isEmpty()){
            Node<Key, Value> n = stack.pop();

            //System.out.println(n);

            if(n.left.get() != null){
                stack.push(n.left.get());
                count++;
            }
            if(n.right.get() != null){
                stack.push(n.right.get());
                count++;
            }
        }
        return count;
    }

    public class Node <Key extends Comparable<Key>, Value> implements SearchTreeNode<Key, Value> {

        public volatile Key key;
        public volatile Value value;
        public AtomicReference<Node<Key, Value>> left;
        public AtomicReference<Node<Key, Value>> right;
        public volatile boolean color;
        public volatile int depth;

        public Node(Key key, Value val, boolean color){
            this.key = key;
            this.value = val;
            this.color = color;
            left = new AtomicReference<>();
            right = new AtomicReference<>();
        }

        @Override
        public SearchTreeNode<Key, Value> getLeft() {
            return left.get();
        }

        @Override
        public SearchTreeNode<Key, Value> getRight() {
            return right.get();
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
