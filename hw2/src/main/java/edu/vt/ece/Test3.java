package edu.vt.ece;

import edu.vt.ece.bench.Counter;
import edu.vt.ece.bench.SharedCounter;
import edu.vt.ece.bench.TestThread;
import edu.vt.ece.bench.TestThread2;
import edu.vt.ece.locks.*;

import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Mohamed M. Saad
 */
public class Test3 {

    private static final int THREAD_COUNT = 2;

    private static final String LOCK_ONE = "LockOne";
    private static final String LOCK_TWO = "LockTwo";
    private static final String PETERSON = "Peterson";
    private static final String L_BAKERY = "LBakery";
    private static final String TREE_PETERSON = "TreePeterson";
    private static final String FILTER = "Filter";

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

        String lockClass = (args.length==0 ? PETERSON : args[0]);
        int threadCount = (args.length==1 ? 16 : Integer.parseInt(args[1]));
        int lCount = (args.length==2 ? 4 : Integer.parseInt(args[2]));
        int iters = (args.length==3 ? 4 : Integer.parseInt(args[3]));
        Class lockerClass = Class.forName("edu.vt.ece.locks." + lockClass);
        Class[] cArg = new Class[2];
        cArg[0] = int.class;
        cArg[1] = int.class;
        try {
            Lock lock = (Lock) lockerClass.getDeclaredConstructor(cArg).newInstance(threadCount, lCount);
            final Counter counter = new SharedCounter(0, lock);
            for(int t=0; t<threadCount; t++)
                new TestThread2(counter, iters).start();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


    }
}
