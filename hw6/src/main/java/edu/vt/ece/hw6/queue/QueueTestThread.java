package edu.vt.ece.hw6.queue;

import java.util.concurrent.ThreadLocalRandom;

public class QueueTestThread extends Thread{

    private Queue<Integer> queue ;
    public long elapsed;

    public long enqTimes;
    public long deqTimesSuccess;
    public long deqTimesFailure;

    private int iter;

    public QueueTestThread(Queue<Integer> queue , int iter){
        this.iter = iter;
        this.queue = queue;
    }

    public void run(){
        ThreadLocalRandom random = ThreadLocalRandom.current();
        enqTimes = 0;
        deqTimesFailure = 0;
        deqTimesSuccess = 0;
        long start = System.currentTimeMillis();

        boolean isEnq = true;
        for (int i = 0; i < iter; i++) {
            int rand = random.nextInt(1073741824);

            if(isEnq){
                queue.enq(rand);
                enqTimes++;
            }else{
                try {
                    int val = queue.deq();
                    deqTimesSuccess++;
                } catch (EmptyException e) {
                    deqTimesFailure++;
                }
            }
            isEnq = !isEnq;
        }
        elapsed = System.currentTimeMillis() - start;
    }

}
