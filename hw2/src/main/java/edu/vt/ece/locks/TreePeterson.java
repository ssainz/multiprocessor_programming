package edu.vt.ece.locks;

import edu.vt.ece.bench.ThreadId;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

public class TreePeterson implements Lock {

    Node[] leaves = null;
    Node root = null;
    AtomicInteger shared_memory = new AtomicInteger(0);

    class Node{
        Lock lock = null;
        Node left = null;
        Node right = null;
        Node parent = null;

        Node(Lock lock){
            this.lock = lock;
        }
        Node(Lock lock, Node left, Node right){
            this.lock = lock;
            this.left = left;
            this.right = right;
        }

        void addParent(Node parent){
            this.parent = parent;
        }
    }

    public TreePeterson() {
        this(2);
    }

    public TreePeterson(int n) {

        leaves = new Node[n/2];
        List<Node> nodes = new ArrayList<>();
        for(int i = 0 ; i < leaves.length ; i++){
            leaves[i] = new Node(new PetersonNode(i*2));
            nodes.add(leaves[i]);
        }

        int treeSize = 2*2;
        int halfTreeSize = 2;
        while(nodes.size() > 1){

            List<Node> newNodes = new ArrayList<>();
            for(int i = 0 ; i < nodes.size() ; i = i+2){
                int discriminator = ( (i/2) * treeSize ) + (treeSize - halfTreeSize) - 1;
                Node tn = new Node(new PetersonNode(discriminator),nodes.get(i), nodes.get(i+1));
                nodes.get(i).addParent(tn);
                nodes.get(i+1).addParent(tn);
                newNodes.add(tn);
            }
            nodes = newNodes;
            treeSize *= 2;
            halfTreeSize *= 2;
        }

        root = nodes.get(0);
    }

    @Override
    public void lock() {
        int i = ((ThreadId)Thread.currentThread()).getThreadId();
        int indexOfNode = i / 2;
        //System.out.println("Thread " + i + " entering lock. indexOfNode = "+ indexOfNode);
        Node n = leaves[indexOfNode];

        while(n != null){
            //System.out.println("Thread " + i + " about to lock "+ ((PetersonNode)n.lock).discriminator + " " + (PetersonNode)n.lock) ;
            n.lock.lock();
            //System.out.println("Thread " + i + " got lock "+ ((PetersonNode)n.lock).discriminator + " " + (PetersonNode)n.lock);
            n = n.parent;
        }

        assert(shared_memory.get() == 0);
        shared_memory.set(1);
        //System.out.println("Thread " + i + " acquire lock!");
    }

    @Override
    public void unlock() {
        int i = ((ThreadId)Thread.currentThread()).getThreadId();
        int indexOfNode = i / 2;

        assert(shared_memory.get() == 1);
        shared_memory.set(0);

        Node n = leaves[indexOfNode];
        Stack s = new Stack();
        while(n != null){
            s.push(n);
            n = n.parent;
        }


        //System.out.println("Thread " + i + " returning lock!");

        while(!s.isEmpty()){
            Node tn = (Node) s.pop();
            tn.lock.unlock();
        }

    }
}
