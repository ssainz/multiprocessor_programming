package edu.vt.ece;

import edu.vt.ece.bench.Counter;
import edu.vt.ece.bench.SharedCounter;
import edu.vt.ece.bench.TestThread;
import edu.vt.ece.bench.TestThread2;
import edu.vt.ece.locks.*;

import java.lang.reflect.InvocationTargetException;

/**
 * 
 * @author Balaji Arun
 */
public class Test2 {
	
	private static final String LOCK_ONE = "LockOne";
	private static final String LOCK_TWO = "LockTwo";
	private static final String PETERSON = "Peterson";
	private static final String TREE_PETERSON = "TreePeterson";
	private static final String BAKERY = "Bakery";
	private static final String FILTER = "Filter";

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, InterruptedException {
		String lockClass = (args.length == 0 ? PETERSON : args[0]);
		int threadCount = (args.length <= 1 ? 16 : Integer.parseInt(args[1]));
		//int totalIters = (args.length <= 2 ? 64000 : Integer.parseInt(args[2]));
		int iters = (args.length <= 2 ? 640000 : Integer.parseInt(args[2]));
		//int iters = totalIters / threadCount;

		double tot = 0;
		int times = 2;
		for (int i = 0; i < times; i++) {
			try {
				tot = run(lockClass, threadCount, iters);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		System.out.println(String.format("%s,%d,%d = %f", lockClass, threadCount, iters, tot));
	}

	private static double run(String lockClass, int threadCount, int iters) throws InterruptedException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
		Class lockerClass = Class.forName("edu.vt.ece.locks." + lockClass);
		Class[] cArg = new Class[1];
		cArg[0] = int.class;
		Lock lock = (Lock) lockerClass.getDeclaredConstructor(cArg).newInstance(threadCount);
		final Counter counter = new SharedCounter(0, lock);
		final TestThread2[] threads = new TestThread2[threadCount];
		TestThread2.reset();

		for(int t=0; t<threadCount; t++) {
			threads[t] = new TestThread2(counter, iters);
		}

		for(int t=0; t<threadCount; t++) {
			threads[t].start();
		}

		long totalTime = 0;
		for(int t=0; t<threadCount; t++) {
			threads[t].join();
			totalTime += threads[t].getElapsedTime();
		}
		return (totalTime*1.0)/(threadCount*1.0);

	}
}
