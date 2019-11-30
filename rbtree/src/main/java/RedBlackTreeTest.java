import edu.vt.ece.hw6.queue.*;
import edu.vt.ece.searchtree.redblacktree.RedBlackTreeCoarseGrained;
import edu.vt.ece.searchtree.redblacktree.RedBlackTreeLockFree;
import edu.vt.ece.searchtree.redblacktree.SearchTree;
import edu.vt.ece.searchtree.redblacktree.SearchTreeTestThread;

public class RedBlackTreeTest {

    public static void main(String[] args) throws InterruptedException {
        // Benchmark Queue Implementation here.
        // Network password: lbml-ajat-gwwz-pxyp
        // Fill this as you please, but you may be better off reusing code from
        // previous homeworks

        final String treeType = args[0];
        final int threadCount = Integer.parseInt(args[1]);
        final int iters = Integer.parseInt(args[2]);


        SearchTree<Integer, Integer> tree = null;
        switch (treeType){
            case "RBTCoarseGrained":
                tree = new RedBlackTreeCoarseGrained<>();
                break;
            case "RBTLockFree":
                tree = new RedBlackTreeLockFree<>();
                break;
            default:
                System.exit(1);
        }

        double[] throughputs = new double[7];
        double[] times = new double[7];

        long numberOfPut = 0;
        long numberOfGet = 0;
        long numberOfDelete = 0;
        for(int i = 0 ; i <= 6 ; i++) { // 7 times, first two times are warm up
            numberOfPut = 0;
            numberOfGet = 0;
            numberOfDelete = 0;

            final SearchTreeTestThread[] threads = new SearchTreeTestThread[threadCount];

            for (int t = 0; t < threadCount; t++) {
                threads[t] = new SearchTreeTestThread(tree, iters);
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
                numberOfDelete += threads[t].deleteTimes;
                numberOfGet += threads[t].getTimes;
                numberOfPut += threads[t].putTimes;
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
        //Dao.storeQueueInDB(queueType,threadCount, avgThroughput, n);
        System.out.println(String.format("put:[%d],get:[%d],delete:[%d]", numberOfPut, numberOfGet, numberOfDelete));
        System.out.println(String.format("%f", avgThroughput));

    }

}
