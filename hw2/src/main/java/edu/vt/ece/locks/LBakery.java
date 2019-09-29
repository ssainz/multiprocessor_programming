package edu.vt.ece.locks;

import edu.vt.ece.bench.ThreadId;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class LBakery implements Lock {

    AtomicBoolean[] flag = null;
    TimestampSystem stampSystem = null;
    int L = 0;
    AtomicInteger counter = new AtomicInteger(0);

    public LBakery(int l, int n) {
        flag = new AtomicBoolean[n];
        stampSystem = new TimestampSystemImp(n);
        L = l;
        for(int i = 0 ; i < n ; i++){
            flag[i] = new AtomicBoolean(false);
        }
    }

    @Override
    public void lock() {
        int i = ((ThreadId)Thread.currentThread()).getThreadId();

        flag[i].set(true);

        Timestamp ithStamp = new TimestampImp(i);
        stampSystem.label(ithStamp, i);

        while(true){
            Timestamp[] stamps = stampSystem.scan();
            int countOfHigherPriorityThreads = 0;
            for(int k = 0 ; k  < stamps.length ; k++){
                if(k != i){
                    if(flag[k].get() && stamps[k].compare(ithStamp)){ // flag[k] && label[k] << label[i]
                        countOfHigherPriorityThreads++;
                    }
                }
            }

            if(countOfHigherPriorityThreads <= L - 1){
                break; // We break if there are at most L - 1 threads with higher priority (some threads may be also
                        // trying to get in to CS, but not yet set their stamp[k]. So they have even lower priority.
                        // Still, CS won't have more than L threads in CS.
            }




        }

        //counter.set(counter.get() + 1);
        //System.out.println(String.format("Thread [%d] inside CS ! total threads in CS is %d.", i, counter.get()));
        System.out.println(String.format("Thread [%d] inside CS !", i));

    }

    @Override
    public void unlock() {
        //counter.set(counter.get() - 1);

        int i = ((ThreadId)Thread.currentThread()).getThreadId();
        System.out.println(String.format("Thread [%d] about to exit CS ! ", i));
        flag[i].set(false);
        //System.out.println(String.format("Thread [%d] exited CS ! total threads in CS is %d. ", i, counter.get()));


    }
}
