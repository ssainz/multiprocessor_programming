package edu.vt.ece.hw6.queue;

import java.util.concurrent.atomic.AtomicInteger;

public class BLFQueue<T> implements Queue<T> {
    private int QUEUE_SIZE = 64;
    private final Item<T>[] queue = new Item[QUEUE_SIZE];

    private final AtomicInteger writer = new AtomicInteger();
    private final AtomicInteger reader = new AtomicInteger();


    public class Item<T>{
        volatile public T value ;
        volatile public int lastID = 0;
    }

    public BLFQueue(int queueSize){
        for(int i = 0 ; i < QUEUE_SIZE ; i++){
            queue[i] = new Item<T>();
        }
    }


    public void enq(T value){
        long threadID = Thread.currentThread().getId();
        int ticket = writer.getAndIncrement();
        int turn = (ticket / QUEUE_SIZE) * 2;
        int position = (ticket % QUEUE_SIZE);
        Item<T> it = queue[position];
        //System.out.println("["+threadID+"]enq:BeforeWhileLoop[turn="+turn+"][position="+position+"]");
        while(it.lastID != turn);
        //System.out.println("["+threadID+"]enq:AfterWhileLoop[turn="+turn+"][position="+position+"]");
        it.value = value;
        it.lastID = turn + 1;
    }

    public T deq(){
        long threadID = Thread.currentThread().getId();
        int ticket = reader.getAndIncrement();
        int turn = ((ticket / QUEUE_SIZE) * 2) + 1;
        int position = (ticket % QUEUE_SIZE);
        Item<T> it = queue[position];
        //System.out.println("["+threadID+"]deq:BeforeWhileLoop[turn="+turn+"][position="+position+"]");
        while(it.lastID != turn);
        //System.out.println("["+threadID+"]deq:AfterWhileLoop[turn="+turn+"][position="+position+"]");
        T val = it.value;
        it.lastID = turn + 1;
        return val;
    }

}
