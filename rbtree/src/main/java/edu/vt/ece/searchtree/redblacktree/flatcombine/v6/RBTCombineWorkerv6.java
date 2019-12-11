package edu.vt.ece.searchtree.redblacktree.flatcombine.v6;

import edu.vt.ece.searchtree.redblacktree.flatcombine.v5.RBTBatch;
import edu.vt.ece.searchtree.redblacktree.flatcombine.v6.RBTFlatCombinev6.Operator;
import edu.vt.ece.searchtree.redblacktree.queue.EmptyException;
import edu.vt.ece.searchtree.redblacktree.queue.Queue;

import java.util.List;

public class RBTCombineWorkerv6 <Key extends Comparable<Key>, Value> extends Thread {

    RBTFlatCombinev6 tree = null;



    RBTCombineWorkerv6(RBTFlatCombinev6<Key, Value> tree){
        this.tree = tree;
    }

    public void run(){

        Queue<Operator> queue = tree.operationsQueue;

        while(true){
            try {



                Operator operator = queue.deq(tree.timeToFinish);
                if(operator == null) return; // is the end

                //System.out.println(String.format("Thread[%s] gets operator %s", ThreadID.get(), operator.operationType));
                /*int sizeBefore = 0;
                int sizeAfter = 0;
                boolean beforeExisted = false;
                long heapSize = Runtime.getRuntime().totalMemory();
                 */

                switch(operator.operationType){
                    case PUT:

                        if(tree.actualTree.exists(operator.key) != null){ // Key already exists.

                            // In this case we just let the caller complete the update.
                            operator.softOperation = true; // Let caller do the work.
                            operator.status.stopped.set(false); // Awake caller.
                            continue; // go back to the while loop.
                        }

                        // insert a new node.

                        // STOP THE WORLD!
                        for(int i = 0 ; i < tree.threadStatusArray.length ; i++){
                            if(tree.threadStatusArray[i] != null){
                                tree.threadStatusArray[i].stopTheWorld.set(true);
                            }
                        }

                        operator.status.stopped.set(true); // calling thread is already spinning in about to sleep either way.

                        boolean allStopped = false;
                        while(!allStopped){ // loops until all threads are stopped.

                            allStopped = true;
                            for(int i = 0 ; i < tree.threadStatusArray.length ; i++){
                                if(tree.threadStatusArray[i] != null) {
                                    allStopped &= tree.threadStatusArray[i].stopped.get();
                                }
                            }

                        }

                        // At this point all threads are waiting.

                        // REMOVE STOP THE WORLD flag
                        for(int i = 0 ; i < tree.threadStatusArray.length ; i++){
                            if(tree.threadStatusArray[i] != null) {
                                tree.threadStatusArray[i].stopTheWorld.set(false);
                            }
                        }

                        // Check if MAX_SIZE is reached.

                        if(tree.size.get() >= tree.MAX_SIZE){
                            // Cleanse the tree.
                            cleanse();
                        }

                        //beforeExisted = tree.actualTree.get(operator.key) != null;
                        //sizeBefore = tree.actualTree.size();

                        operator.res = tree.putV2_util(operator.key, operator.value);
                        tree.size.incrementAndGet();


                        // Awake all threads:
                        for(int i = 0 ; i < tree.threadStatusArray.length ; i++){
                            if(tree.threadStatusArray[i] != null){
                                tree.threadStatusArray[i].stopped.set(false);
                                synchronized(tree.threadStatusArray[i]){
                                    tree.threadStatusArray[i].notifyAll();
                                }
                            }

                        }


                        //System.out.println(String.format("Thread[%s] wakes up %s, PUT key [%s] SIZE_BEFORE[%d] SIZE_AFTER[%d] RES[%s] HEAP[%d]", ThreadID.get(), operator.pid, operator.key, sizeBefore, sizeAfter, operator.res, heapSize));

                        /*
                        sizeAfter = tree.actualTree.size();
                        if(!beforeExisted && sizeBefore == sizeAfter ){
                            System.out.println(String.format("Thread[%s] PUT key [%s] BUT NOT FOUND", ThreadID.get(), operator.key));

                            TreeGUI gui = new TreeGUI<Key, Value>(tree.actualTree, " orig");

                            synchronized (this){
                                try {
                                    Thread.currentThread().wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                        }*/





                        break;
                    case DELETE:

                        if(tree.actualTree.exists(operator.key) != null){ // Key already exists.

                            // In this case we just let the caller complete the update.
                            operator.softOperation = true; // Let caller do the work.
                            operator.status.stopped.set(false); // Awake caller.
                            continue; // go back to the while loop.
                        }

                        // In this case, key does not exists, thus no need to do anything else, let spinning thread continue.

                        operator.res = true;

                        operator.status.stopped.set(false);

                        break;
                    case GET:


                        //System.out.println(String.format("Thread[%s] wakes up %s, GET, numberOfPendingGet=%d ", ThreadID.get(), operator.pid, va));

                        //wakeUp(operator);

                        operator.status.stopped.set(false);

                        break;

                    default:
                        break;
                }
                //System.out.println(String.format("Thread[%s] finish processing operator", ThreadID.get()));

            } catch (EmptyException e) {
            }
        } // while(true)

    } // run

    /**
     * Delete for reals the deleted nodes.
     */
    private void cleanse() {

        List<RBTBatch.Node> deletedNodes;
        deletedNodes = tree.actualTree.getDeletedNodes();

        for(RBTBatch.Node node: deletedNodes){
            tree.actualTree.delete(node.key);
            tree.size.decrementAndGet();
        }


    }

}// Class

