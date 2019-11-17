package edu.vt.ece.hw6.set;

import java.util.concurrent.ThreadLocalRandom;

public class SetTestThread extends Thread {

    private ConcurrentSet<Integer> set ;

    private long elapsed;

    private int iter;

    private double percentageOfContains;

    public SetTestThread(int iter, ConcurrentSet<Integer> set, double percentageOfContains){

        this.set = set;
        this.iter = iter;
        this.percentageOfContains = Math.min(1.0,Math.max(0.0, percentageOfContains));

    }

    public void run() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        long start = System.currentTimeMillis();
        for (int i = 0; i < iter; i++) {
            int rand = random.nextInt(100);
            int limit = (int)(100*this.percentageOfContains);
            if (rand < limit) {
                this.set.contains(rand);
            } else {
                int rand2 = random.nextInt(100);
                if(rand2 < 50){
                    this.set.add(rand);
                }else{
                    this.set.remove(rand);
                }

            }
        }
        elapsed = System.currentTimeMillis() - start;

    }

    public long getElapsedTime() {

        return this.elapsed;

    }
}
