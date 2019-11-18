package edu.vt.ece.hw6.queue;

import edu.vt.ece.hw6.bag.LockFreeBag;
import edu.vt.ece.hw6.queue.EmptyException;
import lists.LockFreeList;

import java.util.concurrent.atomic.AtomicInteger;

public class SemiLinearizableQueueV1<T> implements Queue<T> {

    private int QUEUE_SIZE = 64;
    private Item<T>[] queue ;
    private int n = 1;

    private final AtomicInteger writer = new AtomicInteger();
    private final AtomicInteger reader = new AtomicInteger();

    public SemiLinearizableQueueV1(int queueSize, int n ) {
        QUEUE_SIZE = queueSize;
        this.n = n;
        queue = new Item[QUEUE_SIZE];
        for(int i = 0 ; i < QUEUE_SIZE ; i++){
            queue[i] = new Item<T>();
        }
    }


    public class Item<T>{
        volatile LockFreeBag<T> bag = new LockFreeBag<>();

    }


    @Override
    public void enq(T value) {
        long threadID = Thread.currentThread().getId();
        int fineGrainedTicket = writer.getAndIncrement();
        int ticket = fineGrainedTicket / n;
        int position = (ticket % QUEUE_SIZE);
        Item<T> it = queue[position];
        System.out.println("["+threadID+"]enq:BeforeAddingToBag[position="+position+"]");
        it.bag.add(value);
    }

    @Override
    public T deq() throws EmptyException {
        long threadID = Thread.currentThread().getId();
        int fineGrainedTicket = reader.getAndIncrement();
        int ticket = fineGrainedTicket / n;
        int position = (ticket % QUEUE_SIZE);
        Item<T> it = queue[position];
        System.out.println("["+threadID+"]deq:BeforeRemovingFromBag[position="+position+"]");
        T val = it.bag.deque();
        return val;
    }
}
