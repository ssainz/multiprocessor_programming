package edu.vt.ece.hw4.bench;

import edu.vt.ece.hw4.locks.Lock;

/**
 *
 * @author Mohamed M. Saad
 */
public class SharedCounterTimeout extends Counter{
    private Lock lock;

    public SharedCounterTimeout(int c, Lock lock) {
        super(c);
        this.lock = lock;
    }

    @Override
    public int getAndIncrement() {
        int temp = -1;
        if(lock.trylock()){ //sucessfull timeout

            try {
                temp = super.getAndIncrement();
            } finally {
                lock.unlock();
            }
        }
        return temp;
    }

}
