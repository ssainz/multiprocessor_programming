package edu.vt.ece.hw4.barriers;

import edu.vt.ece.hw4.bench.ThreadId;
import edu.vt.ece.hw4.locks.TTASLock;

public class ArrayBarrier implements Barrier {
    public int[] flags = null;
    public int counter = 0;
    public int totalThread = 0;

    public ArrayBarrier(int threadNumber){
        totalThread = threadNumber;
        flags = new int[totalThread];
    }

    @Override
    public void enter(int threadId) {
        int id = threadId;

        if(id != 0){
            while(flags[id-1] == 0){}
        }

        if(id == totalThread - 1){
            flags[id] = 2;
            return;
        }else{
            flags[id] = 1;
        }

        while(flags[totalThread - 1] == 2){}

    }

    @Override
    public void reset() {
        for(int i = 0 ; i < totalThread ; i++){
            flags[i] = 0 ;
        }
    }
}
