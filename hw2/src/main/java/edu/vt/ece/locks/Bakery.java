package edu.vt.ece.locks;

import edu.vt.ece.bench.ThreadId;
import edu.vt.ece.locks.Lock;

import java.util.concurrent.atomic.AtomicBoolean;

public class Bakery implements Lock {
    AtomicBoolean[] flag = null;
    TimestampSystem stampSystem = null;

    public Bakery() {

        this(2);
    }

    public Bakery(int n) {
        flag = new AtomicBoolean[n];
        stampSystem = new TimestampSystemImp(n);
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
            boolean isContinue = false;
            Timestamp[] stamps = stampSystem.scan();
            for(int k = 0 ; k  < stamps.length ; k++){
                if(k != i){
                    if(flag[k].get() && stamps[k].compare(ithStamp)){ // flag[k] && label[k] << label[i]
                        isContinue = true;
                    }
                }
            }

            if(!isContinue) break;
        }

    }

    @Override
    public void unlock() {
        int i = ((ThreadId)Thread.currentThread()).getThreadId();
        flag[i].set(false);
    }
}
