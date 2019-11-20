import edu.vt.ece.hw6.queue.*;
import edu.vt.ece.hw6.set.Dao;

public class QueueTest {

    public static void main(String[] args) throws InterruptedException {
        // Benchmark Queue Implementation here.
        // Network password: lbml-ajat-gwwz-pxyp
        // Fill this as you please, but you may be better off reusing code from
        // previous homeworks

        final String queueType = args[0];
        final int threadCount = Integer.parseInt(args[1]);
        final int iters = Integer.parseInt(args[2]);
        int n = 1;
        if(args.length > 3){
            n = Integer.parseInt(args[3]);
        }

        Queue<Integer> queue = null;
        switch (queueType){
            case "JLQueue":
                queue = new ConcurrentLinkedQueueWrapper<Integer>();
                break;
            case "ULFQueue":
                queue = new UnboundedLockFreeQueue<Integer>();
                break;
            case "BLFQueue":
                queue = new BoundedLockFreeQueue<Integer>(threadCount * 3);
                break;
            case "SLQueue":
                queue = new SemiLinearizableQueueV1<Integer>(threadCount * 3  , n);
                break;
            default:
                System.exit(1);
        }

        double[] throughputs = new double[7];
        double[] times = new double[7];

        long numberOfEnq = 0;
        long numberOfDeqSuccess = 0;
        long numberOfDeqFailure = 0;
        for(int i = 0 ; i <= 6 ; i++) { // 7 times, first two times are warm up
            numberOfEnq = 0;
            numberOfDeqSuccess = 0;
            numberOfDeqFailure = 0;

            final QueueTestThread[] threads = new QueueTestThread[threadCount];

            for (int t = 0; t < threadCount; t++) {
                threads[t] = new QueueTestThread(queue, iters);
            }

            for (int t = 0; t < threadCount; t++) {
                threads[t].start();
            }

            long totalTime = 0;
            long maxTime = 0;
            for (int t = 0; t < threadCount; t++) {
                threads[t].join();
                totalTime += threads[t].elapsed;
                maxTime = Math.max(maxTime, threads[t].elapsed);
                numberOfEnq += threads[t].enqTimes;
                numberOfDeqSuccess += threads[t].deqTimesSuccess;
                numberOfDeqFailure += threads[t].deqTimesFailure;

            }

            if(i > 1){
                throughputs[ i - 2 ] = (iters*threadCount) / (maxTime*0.001); // Multiplies by .001 to reduce to milliseconds from nanoseconds
                times[ i - 2 ] = totalTime / threadCount;
            }
        }

        double totalThroughput = 0;
        double totalTimeOverall = 0;
        for(int i = 0 ; i < 5 ; i++ ){
            totalThroughput += throughputs[i];
            totalTimeOverall += times[i];
        }
        double avgThroughput = totalThroughput / 5;
        double avgTime = totalTimeOverall / 5;
        Dao.storeQueueInDB(queueType,threadCount, avgThroughput, n);
        System.out.println(String.format("%d %d %d", numberOfEnq, numberOfDeqSuccess, numberOfEnq - numberOfDeqSuccess));
        System.out.println(String.format("%f", avgThroughput));
    }
}
