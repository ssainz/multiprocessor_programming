package edu.vt.ece.hw6.queue;

import java.util.concurrent.atomic.AtomicInteger;

public class BLFQueue<T> implements Queue<T> {
    private final static int QUEUE_SIZE = 64;
    private final Item<T>[] queue = new Item[QUEUE_SIZE];

    private final AtomicInteger writer = new AtomicInteger();
    private final AtomicInteger reader = new AtomicInteger();


    public class Item<T>{
        volatile public T value ;
        volatile public int lastID = 0;
    }

    public BLFQueue(){
        for(int i = 0 ; i < QUEUE_SIZE ; i++){
            queue[i] = new Item<T>();
        }
    }


    public void enq(T value){
        int ticket = writer.getAndIncrement();
        int turn = (ticket / QUEUE_SIZE) * 2;
        int position = (ticket % QUEUE_SIZE);
        Item<T> it = queue[position];
        while(it.lastID != turn);
        System.out.println("enq:AfterWhileLoop");
        it.value = value;
        it.lastID = turn + 1;
    }

    public T deq(){
        int ticket = reader.getAndIncrement();
        int turn = ((ticket / QUEUE_SIZE) * 2) - 1;
        turn = (turn ==  -1) ? 1: turn;
        int position = (ticket % QUEUE_SIZE);
        Item<T> it = queue[position];
        while(it.lastID != turn);
        System.out.println("deq:AfterWhileLoop");
        T val = it.value;
        it.lastID = turn + 1;
        return val;
    }

}
