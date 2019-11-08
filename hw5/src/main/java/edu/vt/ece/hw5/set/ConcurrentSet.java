package edu.vt.ece.hw5.set;

public interface ConcurrentSet<T> {

    public boolean contains(T item);
    public boolean add(T item);
    public boolean remove(T item);

}
