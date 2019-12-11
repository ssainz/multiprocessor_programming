package edu.vt.ece.searchtree.redblacktree.flatcombine.v6;

import edu.vt.ece.searchtree.redblacktree.flatcombine.ThreadID;
import edu.vt.ece.searchtree.redblacktree.flatcombine.v5.RBTBatch;
import edu.vt.ece.searchtree.redblacktree.flatcombine.v6.RBTFlatCombinev6.ThreadStatus;
import edu.vt.ece.searchtree.redblacktree.queue.EmptyException;
import edu.vt.ece.searchtree.redblacktree.queue.Queue;

import java.util.List;

public class RBTCombineWorkerv6 <Key extends Comparable<Key>, Value> extends Thread {

    RBTFlatCombinev6 tree = null;

    boolean isProd = true;

    RBTCombineWorkerv6(RBTFlatCombinev6<Key, Value> tree){
        this.tree = tree;
    }

    public void run(){

        Queue<ThreadStatus> queue = tree.operationsQueue;

        while(true){
            try {

                ThreadStatus status = queue.deq(tree.timeToFinish);
                if(status == null) return; // is the end

                if(!isProd)
                    System.out.println(String.format("Thread[%s] gets operator %s", ThreadID.get(), status.operationType));

                int sizeBefore = 0;
                int sizeAfter = 0;
                boolean beforeExisted = false;
                long heapSize = Runtime.getRuntime().totalMemory();


                switch(status.operationType){
                    case PUT:

                        if(!isProd)
                            System.out.println(String.format("Thread[%s] PID %s, PUT key [%s] ", ThreadID.get(), status.pid, status.opKey));

                        if(tree.actualTree.exists(status.opKey) != null){ // Key already exists.

                            // In this case we just let the caller complete the update.
                            status.softOperation = true; // Let caller do the work.
                            status.waitingInBlockingQueue.set(false); // Awake caller.
                            continue; // go back to the while loop.
                        }

                        // insert a new node.

                        // STOP THE WORLD!
                        if(!isProd)
                            System.out.println("STOP THE WORLD!!");

                        ThreadStatus n = tree.head.next;
                        while (n.nodekey < tree.tail.nodekey) {
                            //System.out.println(String.format("LockFreeBag:%d,%s", id, n));
                            n.stopTheWorld.set(true);
                            n = n.next;
                        }


                        boolean allStopped = false;
                        int counter = 0;
                        while(!allStopped){ // loops until all threads are stopped.

                            allStopped = true;

                            n = tree.head.next;
                            while (n.nodekey < tree.tail.nodekey) {
                                //System.out.println(String.format("LockFreeBag:%d,%s", id, n));
                                if(n.stopped.get()){
                                    allStopped = allStopped & true;
                                }else{
                                    if(n.waitingInBlockingQueue.get()){
                                        allStopped = allStopped & true;
                                    }else{
                                        allStopped = allStopped & false;
                                    }
                                }
                                n = n.next;
                            }
                            counter++;
                            if(counter == 10000){
                                n = tree.head.next;
                                while (n.nodekey < tree.tail.nodekey) {
                                    if(!isProd) System.out.println(String.format("WORKER Status:%d,%s, stopped object %s , stopped %b", n.pid, n, n.stopped, n.stopped.get()));
                                    allStopped &= n.stopped.get();
                                    n = n.next;
                                }
                                //System.exit(0);
                            }

                        }


//                        System.out.println("TEST;");
//                        n = tree.head;
//                        while (n.nodekey < tree.tail.nodekey) {
//                            System.out.println(String.format("WORKER Status:%d,%s, stopped object %s , stopped %b", n.pid, n, n.stopped, n.stopped.get()));
//                            allStopped &= n.stopped.get();
//                            n = n.next;
//                        }
                        //System.exit(0);
                        // At this point all threads are waiting.

                        // REMOVE STOP THE WORLD flag
                        n = tree.head.next;
                        while (n.nodekey < tree.tail.nodekey) {
                            //System.out.println(String.format("LockFreeBag:%d,%s", id, n));
                            n.stopTheWorld.set(false);
                            n = n.next;
                        }

                        // Check if MAX_SIZE is reached.

                        if(tree.size.get() >= tree.MAX_SIZE){
                            // Cleanse the tree.
                            cleanse();
                        }

                        //beforeExisted = tree.actualTree.get(operator.key) != null;
                        //sizeBefore = tree.actualTree.size();

                        status.res = tree.putV2_util(status.opKey, status.value);
                        tree.size.incrementAndGet();


                        // Awake all threads:
                        n = tree.head.next;
                        while (n.nodekey < tree.tail.nodekey) {

                            if(n.stopped.get()){ // Only stopped nodes are waiting!!!
                                synchronized (n){

                                    n.stopped.set(false);
                                    if(!isProd) System.out.println(String.format("WORKER Status:%d,%s, stopped object %s , stopped %b", n.pid, n, n.stopped, n.stopped.get()));
                                    n.notifyAll();
                                }
                            }
                            //System.out.println(String.format("LockFreeBag:%d,%s", id, n));

                            n = n.next;
                        }

                        if(!isProd) System.out.println(String.format("Thread[%s] PID %s, PUT key [%s] ", ThreadID.get(), status.pid, status.opKey));

                        status.waitingInBlockingQueue.set(false); //Awake original thread who made the PUT



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

                        if(!isProd) System.out.println(String.format("Thread[%s] PID %s, DELETE key [%s] ", ThreadID.get(), status.pid, status.opKey));

                        if(tree.actualTree.exists(status.opKey) != null){ // Key already exists.

                            // In this case we just let the caller complete the update.
                            status.softOperation = true; // Let caller do the work.
                            status.waitingInBlockingQueue.set(false); // Awake caller.
                            continue; // go back to the while loop.
                        }

                        // In this case, key does not exists, thus no need to do anything else, let spinning thread continue.

                        status.res = true;

                        status.waitingInBlockingQueue.set(false);

                        if(!isProd) System.out.println(String.format("Thread[%s] wakes up %s, DELETE key [%s] ", ThreadID.get(), status.pid, status.opKey));

                        break;
                    case GET:


                        //System.out.println(String.format("Thread[%s] wakes up %s, GET, numberOfPendingGet=%d ", ThreadID.get(), operator.pid, va));

                        //wakeUp(operator);

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

