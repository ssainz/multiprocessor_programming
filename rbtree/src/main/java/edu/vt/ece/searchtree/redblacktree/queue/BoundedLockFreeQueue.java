package edu.vt.ece.searchtree.redblacktree.queue;

import edu.vt.ece.searchtree.redblacktree.flatcombine.ThreadID;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class BoundedLockFreeQueue<T> implements Queue<T> {
    private int QUEUE_SIZE = 64;
    private Item<T>[] queue ;

    private final AtomicInteger writer = new AtomicInteger();
    private final AtomicInteger reader = new AtomicInteger();


    public class Item<T>{
        volatile public T value ;
        volatile public int lastID = 0;
    }

    public BoundedLockFreeQueue(int queueSize){
        QUEUE_SIZE = queueSize;
        queue = new Item[QUEUE_SIZE];
        for(int i = 0 ; i < QUEUE_SIZE ; i++){
            queue[i] = new Item<T>();
        }
    }


    public void enq(T value){
        long threadID = ThreadID.get();
        int ticket = writer.getAndIncrement();
        int turn = (ticket / QUEUE_SIZE) * 2;
        int position = (ticket % QUEUE_SIZE);
        Item<T> it = queue[position];
        //System.out.println("["+threadID+"]enq:BeforeWhileLoop[it.lastID="+it.lastID+"][turn="+turn+"][position="+position+"]");
        while(it.lastID != turn);
        //System.out.println("["+threadID+"]enq:AfterWhileLoop[it.lastID="+it.lastID+"][turn="+turn+"][position="+position+"]");
        it.value = value;
        it.lastID = turn + 1;
    }

    public T deq(AtomicBoolean timeToFinish){
        long threadID = ThreadID.get();
        int ticket = reader.getAndIncrement();
        int turn = ((ticket / QUEUE_SIZE) * 2) + 1;
        int position = (ticket % QUEUE_SIZE);
        Item<T> it = queue[position];
        //System.out.println("["+threadID+"]deq:BeforeWhileLoop[it.lastID="+it.lastID+"][turn="+turn+"][position="+position+"]");
        while(it.lastID != turn && !timeToFinish.get());
        if(timeToFinish.get()) return null;
        //System.out.println("["+threadID+"]deq:AfterWhileLoop[it.lastID="+it.lastID+ "][turn="+turn+"][position="+position+"]");
        T val = it.value;
        it.lastID = turn + 1;
        return val;
    }

}
