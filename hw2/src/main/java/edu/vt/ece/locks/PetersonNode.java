package edu.vt.ece.locks;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import edu.vt.ece.bench.ThreadId;

public class PetersonNode implements Lock{

    private AtomicBoolean[] flag = new AtomicBoolean[2];
    private AtomicInteger victim ;
    int discriminator = 0;

    public PetersonNode(int discriminator) {
        flag[0] = new AtomicBoolean(false);
        flag[1] = new AtomicBoolean(false);
        victim = new AtomicInteger();
        this.discriminator = discriminator;
    }

    @Override
    public void lock() {
        int threadIndex = ((ThreadId)Thread.currentThread()).getThreadId();
        int i = threadIndex <= discriminator ? 0 : 1;
        int j = 1-i;
        flag[i].set(true);
        victim.set(i);
        while(flag[j].get() && victim.get() == i);
			//System.out.println("Thread " + i + " waiting");

//        if(discriminator == 7){
//            System.out.println("Owned by thread ID " + threadIndex + " flag["+i+"] is " + flag[i].get() + " flag["+j+"] is " + flag[j].get() + ". And victim is "+ victim.get());
//        }

    }

    @Override
    public void unlock() {
        int threadIndex = ((ThreadId)Thread.currentThread()).getThreadId();
        int i = threadIndex <= discriminator ? 0 : 1;
        flag[i].set(false);
    }

}
