package edu.vt.ece.searchtree.redblacktree;

import edu.vt.ece.searchtree.redblacktree.flatcombine.ThreadID;
import edu.vt.ece.searchtree.redblacktree.flatcombine.v6.RBTFlatCombinev6;

import java.util.concurrent.ThreadLocalRandom;

public class SearchTreeTestThread extends Thread{

    private SearchTree<Integer,Integer> tree ;
    public long elapsed;

    public long putTimes;
    public long getTimes;
    public long deleteTimes;

    public long getMilli;
    public long putMilli;
    public long deleteMilli;

    private int iter;

    public SearchTreeTestThread(SearchTree<Integer,Integer> tree , int iter){
        this.iter = iter;
        this.tree = tree;
    }

    public void run(){
        ThreadLocalRandom random = ThreadLocalRandom.current();
        putTimes = 0;
        getTimes = 0;
        deleteTimes = 0;

        if(tree instanceof RBTFlatCombinev6){
            tree.get(0);
        }

        long start = System.currentTimeMillis();

        boolean isEnq = true;
        for (int i = 0; i < iter; i++) {
            long startOp = System.nanoTime();
            int rand = random.nextInt(2000);

            int operation = random.nextInt(100);

            if(operation >= 20){
                tree.get(rand);
                getTimes++;
                getMilli += (System.nanoTime() - startOp);
            }else if(operation >= 10){
                tree.putV2(rand,rand);
                putTimes++;
                putMilli += (System.nanoTime() - startOp);
            }else{
                tree.delete(rand);
                deleteTimes++;
                deleteMilli += (System.nanoTime()- startOp);
            }
            //System.out.println(String.format("Thread[%s] finish iter[%d] ", ThreadID.get(), i));
        }
        //System.out.println(String.format("Thread[%s] FINISH ALL ", ThreadID.get()));
        elapsed = System.currentTimeMillis() - start;
    }

}
