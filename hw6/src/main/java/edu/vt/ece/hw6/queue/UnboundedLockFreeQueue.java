package edu.vt.ece.hw6.queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

public class UnboundedLockFreeQueue<T> implements Queue<T>{

    public AtomicReference<Node> tail ;
    public AtomicReference<Node> head ;

    public class Node{

        public T value;
        public AtomicReference<Node> next;
        public Node(T value){
            this.value = value;
            next = new AtomicReference<Node>(null);
        }
    }

    public UnboundedLockFreeQueue(){
        tail.set(new Node(null));
        head.set(tail.get());
    }

    public void enq(T value){
        Node node = new Node(value);
        while(true){
            Node last = tail.get();
            Node next = last.next.get();
            if(last == tail.get()){
                if(next == null){
                    if(last.next.compareAndSet(next, node)){
                        tail.compareAndSet(last,node);
                        return;
                    }
                }else{
                    tail.compareAndSet(last,next);
                }
            }
        }
    }// enq

    public T deq() throws EmptyException{
        while(true){
            Node first = head.get();
            Node last = tail.get();
            Node next = first.next.get();
            if(first == head.get()){
                if(first == last){
                    if(next == null){
                        throw new EmptyException();
                    }
                    tail.compareAndSet(last,next);
                } else{
                    T value = next.value;
                    if(head.compareAndSet(first,next)){
                        return value;
                    }
                }
            }
        }
    } // deq
}
