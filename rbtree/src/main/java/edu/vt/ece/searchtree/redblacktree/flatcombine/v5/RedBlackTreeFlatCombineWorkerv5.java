package edu.vt.ece.searchtree.redblacktree.flatcombine.v5;

import edu.vt.ece.searchtree.redblacktree.flatcombine.v5.RedBlackTreeFlatCombinev5.Operator;
import edu.vt.ece.searchtree.redblacktree.queue.EmptyException;
import edu.vt.ece.searchtree.redblacktree.queue.Queue;

import java.util.List;

public class RedBlackTreeFlatCombineWorkerv5 <Key extends Comparable<Key>, Value> extends Thread {

    RedBlackTreeFlatCombinev5 tree = null;



    RedBlackTreeFlatCombineWorkerv5(RedBlackTreeFlatCombinev5<Key, Value> tree){
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
                            tree.numberOfPendingOperations.incrementAndGet(); // When "stop the world" operation needs to occur we want to make sure this is finished.
                            operator.status.aboutToSleep.set(false); // Awake caller.
                            continue; // go back to the while loop.
                        }

                        // insert a new node.

                        while(tree.numberOfPendingOperations.get() > 0); // Wait for pending get to finish.

                        // Check if MAX_SIZE is reached.

                        if(tree.size.get() >= tree.MAX_SIZE){
                            // Cleanse the tree.
                            cleanse();
                        }

                        //beforeExisted = tree.actualTree.get(operator.key) != null;
                        //sizeBefore = tree.actualTree.size();

                        operator.res = tree.putV2_util(operator.key, operator.value);
                        tree.size.incrementAndGet();

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



                        operator.status.aboutToSleep.set(false);

                        break;
                    case DELETE:

                        if(tree.actualTree.exists(operator.key) != null){ // Key already exists.

                            // In this case we just let the caller complete the update.
                            operator.softOperation = true; // Let caller do the work.
                            tree.numberOfPendingOperations.incrementAndGet(); // When "stop the world" operation needs to occur we want to make sure this is finished.
                            operator.status.aboutToSleep.set(false); // Awake caller.
                            continue; // go back to the while loop.
                        }

                        // In this case, key does not exists, thus no need to do anything else, let spinning thread continue.

                        operator.res = true;

                        operator.status.aboutToSleep.set(false);

                        break;
                    case GET:

                        int va = tree.numberOfPendingOperations.incrementAndGet();

                        //System.out.println(String.format("Thread[%s] wakes up %s, GET, numberOfPendingGet=%d ", ThreadID.get(), operator.pid, va));

                        //wakeUp(operator);

                        operator.status.aboutToSleep.set(false);

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
        }


    }

}// Class

