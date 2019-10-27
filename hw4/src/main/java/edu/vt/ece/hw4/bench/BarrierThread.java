package edu.vt.ece.hw4.bench;


import edu.vt.ece.hw4.barriers.Barrier;

public class BarrierThread extends Thread implements ThreadId {
    private static int ID_GEN = 0;

    private Barrier barrier = null;
    private int id;
    private long elapsed;

    public BarrierThread(Barrier barrier) {
        this.barrier = barrier;
        id = ID_GEN++;
    }

    public static void reset(){
        ID_GEN = 0;
    }

    public void foo(){
        double a = Math.PI / Math.E;
    }

    public void bar(){
        double a = Math.PI / Math.E;
    }

    @Override
    public void run() {
        foo();
        long start = System.nanoTime();

        barrier.enter(getThreadId()); // waiting for slower ones :P

        long end = System.nanoTime();
        bar();
        if(end - start > 0){
            System.out.println(end - start);
        }
        elapsed += end - start;
    }

    public int getThreadId(){
        return id;
    }

    public long getElapsedTime() {
        return elapsed;
    }
}
