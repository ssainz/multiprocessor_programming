package edu.vt.ece.hw4.locks;

import java.util.concurrent.atomic.AtomicInteger;

public class SpinSleepLock implements Lock {
    ThreadLocal<Integer> mySlotIndex = new ThreadLocal<Integer>(){
        protected Integer initialValue(){
            return 0;
        }
    };
    public AtomicInteger tail ;
    public AtomicInteger head ;
    public AtomicInteger numberOfThreadsInLock ;
    public volatile boolean[] flag;
    public volatile Thread[] threads = null;
    public int maxSpin = 0;
    public int size = 0;

    public SpinSleepLock(int capacity, int maxSpin) {
        tail = new AtomicInteger(0);
        head = new AtomicInteger(0);
        numberOfThreadsInLock = new AtomicInteger(0);
        threads = new Thread[capacity];
        flag = new boolean[capacity];
        flag[0] = true;
        this.maxSpin = maxSpin <= 0 ? 1: maxSpin;
        this.size = capacity;
    }



    @Override
    public void lock() {
        System.out.println(String.format("Lock [%s]",Thread.currentThread()));

        int tailSlot = tail.getAndIncrement();
        int slot =  tailSlot % size;
        threads[slot] = Thread.currentThread();
        int headSlot = head.get();
        int threadsInLock = tailSlot - headSlot ; // Does not count self.
        mySlotIndex.set(slot);
        System.out.println(String.format("Lock [%s] a",Thread.currentThread()));
        if(threadsInLock > maxSpin){
            try {
                System.out.println(String.format("Lock [%s] b",Thread.currentThread()));
                synchronized (threads[slot]){
                    threads[slot].wait();
                }
            } catch (InterruptedException e) {
                // Awake, continue:
                System.out.println("AWAKE!");
            }
        }
        System.out.println(String.format("Lock [%s] c, waiting flag[%d] to be true, [threadsInLock= %d]>[maxSpin= %d] ",Thread.currentThread(), slot, threadsInLock, maxSpin));
        while(!flag[slot]){}
        head.set(tailSlot);
        System.out.println(String.format("Lock [%s] d",Thread.currentThread()));
    }

    @Override
    public void unlock() {
        System.out.println(String.format("Unlock [%s]",Thread.currentThread()));
        int slot = mySlotIndex.get();
        int tailSlot = tail.get() - 1;// there is always increment after getting tail.
        int newHeadSlot = head.get() + 1; // This thread was the last one to set head. No concurrency problem.
        int threadsInLock = tailSlot - newHeadSlot ; // Does not count itself.
        //System.out.println(String.format("Unlock [%s]b, threadsInLock=[%d],maxSpin[%d]",Thread.currentThread(),threadsInLock, maxSpin));

        for(int i = 0 ; i + newHeadSlot <= tailSlot ; i++){
            if(i <= maxSpin){
                if(threads[ ( i + newHeadSlot % size)].getState() == Thread.State.WAITING){
                    synchronized (threads[ ( i + newHeadSlot % size)]){

                        System.out.println(String.format("Unlock[%s], slot=[%d],Notifying [%s], state=[%s]",Thread.currentThread(), i + newHeadSlot % size, threads[i + newHeadSlot % size], threads[i + newHeadSlot % size].getState()));
                        threads[ ( i + newHeadSlot % size)].notifyAll();
                    }
                }
            }else{
                break;
            }
        }


        System.out.println(String.format("Unlock [%s]c, set flag[%d] to true",Thread.currentThread(), (slot + 1) % size));
        flag[slot] = false;
        flag[(slot + 1) % size] = true;

    }

    @Override
    public boolean trylock() {
        return false;
    }

}
