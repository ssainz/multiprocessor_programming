package edu.vt.ece;

import edu.vt.ece.bench.Counter;
import edu.vt.ece.bench.SharedCounter;
import edu.vt.ece.bench.TestThread;
import edu.vt.ece.locks.*;

import java.lang.reflect.InvocationTargetException;

/**
 * 
 * @author Mohamed M. Saad
 */
public class Test {

	private static final int THREAD_COUNT = 2;
	
	private static final String LOCK_ONE = "LockOne";
	private static final String LOCK_TWO = "LockTwo";
	private static final String PETERSON = "Peterson";
	private static final String TREE_PETERSON = "TreePeterson";
	private static final String FILTER = "Filter";

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		int threadCount = 16;
		String lockClass = (args.length==0 ? PETERSON : args[0]);
		Class lockerClass = Class.forName("edu.vt.ece.locks." + lockClass);
		Class[] cArg = new Class[1];
		cArg[0] = int.class;
		try {
			Lock lock = (Lock) lockerClass.getDeclaredConstructor(cArg).newInstance(threadCount);
			final Counter counter = new SharedCounter(0, lock);
			for(int t=0; t<threadCount; t++)
				new TestThread(counter).start();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}


	}
}
