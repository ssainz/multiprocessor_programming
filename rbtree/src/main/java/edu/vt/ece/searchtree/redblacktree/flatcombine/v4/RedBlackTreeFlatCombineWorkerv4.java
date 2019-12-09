package edu.vt.ece.searchtree.redblacktree.flatcombine.v4;

import edu.vt.ece.searchtree.redblacktree.flatcombine.ThreadID;
import edu.vt.ece.searchtree.redblacktree.flatcombine.v4.RedBlackTreeFlatCombinev4;
import edu.vt.ece.searchtree.redblacktree.flatcombine.v4.RedBlackTreeFlatCombinev4.Operator;
import edu.vt.ece.searchtree.redblacktree.gui.TreeGUI;
import edu.vt.ece.searchtree.redblacktree.queue.EmptyException;
import edu.vt.ece.searchtree.redblacktree.queue.Queue;

public class RedBlackTreeFlatCombineWorkerv4 <Key extends Comparable<Key>, Value> extends Thread {

    RedBlackTreeFlatCombinev4 tree = null;

    RedBlackTreeFlatCombineWorkerv4(RedBlackTreeFlatCombinev4<Key, Value> tree){
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

                        while(tree.numberOfPendingGet.get() > 0); // Wait for pending get to finish.

                        //beforeExisted = tree.actualTree.get(operator.key) != null;
                        //sizeBefore = tree.actualTree.size();

                        operator.res = tree.putV2_util(operator.key, operator.value);


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

                        while(tree.numberOfPendingGet.get() > 0); // Wait for pending get to finish.

                        //sizeBefore = tree.actualTree.size();
                        //beforeExisted = tree.actualTree.get(operator.key) != null;

                        operator.res = tree.delete_util(operator.key);


                        //System.out.println(String.format("Thread[%s] wakes up %s, DELETE key [%s]  SIZE_BEFORE[%d] SIZE_AFTER[%d] RES[%s] HEAP[%d]", ThreadID.get(), operator.pid, operator.key, sizeBefore, sizeAfter, operator.res, heapSize));

                        /*
                        sizeAfter = tree.actualTree.size();
                        if(sizeBefore == sizeAfter && beforeExisted){
                            System.out.println(String.format("Thread[%s] DELETE key [%s] BUT size did not decrease", ThreadID.get(), operator.key));

                            TreeGUI gui = new TreeGUI<Key, Value>(tree, " orig");

                            System.exit(0);
                        }*/

                        operator.status.aboutToSleep.set(false);

                        break;
                    case GET:

                        int va = tree.numberOfPendingGet.incrementAndGet();

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

}// Class

