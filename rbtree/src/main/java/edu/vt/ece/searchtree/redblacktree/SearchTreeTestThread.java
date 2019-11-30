package edu.vt.ece.searchtree.redblacktree;

import java.util.concurrent.ThreadLocalRandom;

public class SearchTreeTestThread extends Thread{

    private SearchTree<Integer,Integer> tree ;
    public long elapsed;

    public long putTimes;
    public long getTimes;
    public long deleteTimes;

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
        long start = System.currentTimeMillis();

        boolean isEnq = true;
        for (int i = 0; i < iter; i++) {
            int rand = random.nextInt(100);

            int operation = random.nextInt(100);

            if(operation >= 20){
                tree.get(rand);
                getTimes++;
            }else if(operation >= 10){
                tree.put(rand,rand);
                putTimes++;
            }else{
                //tree.delete(rand);
                //deleteTimes++;
            }
        }
        elapsed = System.currentTimeMillis() - start;
    }

}
