package edu.vt.ece.hw4.bench;

/**
 *
 * @author Mohamed M. Saad
 */
public class PriorityBasedThread extends Thread implements ThreadId {
    private static int ID_GEN = 0;

    private Counter counter;
    private long id;
    private long elapsed;
    private int iter;
    public int priority;

    public PriorityBasedThread(Counter counter, int iter) {
        id = Thread.currentThread().getId();
        priority = (int) ((id % 5 ) + 1);

        this.counter = counter;
        this.iter = iter;
    }

    public static void reset() {
        ID_GEN = 0;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();

        for(int i=0; i<iter; i++)
            counter.getAndIncrement();

        long end = System.currentTimeMillis();

        elapsed = (end - start) * priority;
    }

    public int getThreadId(){
        return (int) id;
    }

    public long getElapsedTime() {
        System.out.println(String.format("[%d],Priority[%d],time[%d]",id,priority,elapsed));
        return elapsed;
    }
}
