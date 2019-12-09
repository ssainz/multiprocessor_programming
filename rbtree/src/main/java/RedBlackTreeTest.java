import edu.vt.ece.searchtree.redblacktree.RedBlackTreeCoarseGrained;
import edu.vt.ece.searchtree.redblacktree.flatcombine.v2.RedBlackTreeFlatCombinev2;
import edu.vt.ece.searchtree.redblacktree.flatcombine.v3.RedBlackTreeFlatCombinev3;
import edu.vt.ece.searchtree.redblacktree.flatcombine.v4.RedBlackTreeFlatCombinev4;
import edu.vt.ece.searchtree.redblacktree.notworking.RedBlackTreeLockFree;
import edu.vt.ece.searchtree.redblacktree.SearchTree;
import edu.vt.ece.searchtree.redblacktree.SearchTreeTestThread;
import edu.vt.ece.searchtree.redblacktree.flatcombine.v1.RedBlackTreeFlatCombine;
import edu.vt.ece.searchtree.redblacktree.synchrobench.CompositionalRBTreeIntSet;
import edu.vt.ece.searchtree.redblacktree.synchrobench.TransactionalRBTreeSet;

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


        double[] throughputs = new double[7];
        double[] times = new double[7];

        double[] avgGetMilli = new double[7];
        double[] avgPutMilli = new double[7];
        double[] avgDeleteMilli = new double[7];

        long numberOfPut = 0;
        long numberOfGet = 0;
        long numberOfDelete = 0;
        int LOOPS = 6;

        long heapSize = Runtime.getRuntime().totalMemory();

        System.out.println(String.format("[%d]", heapSize));

        for(int i = 0 ; i <= LOOPS ; i++) { // 7 times, first two times are warm up
            numberOfPut = 0;
            numberOfGet = 0;
            numberOfDelete = 0;

            int getMilli = 0;
            int putMilli = 0;
            int deleteMilli = 0;

            tree = getTree(treeType, threadCount);

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
                getMilli += threads[t].getMilli;
                putMilli += threads[t].putMilli;
                deleteMilli += threads[t].deleteMilli;
            }

            if(i > 1){
                throughputs[ i - 2 ] = (iters*threadCount) / (maxTime*0.001); // Multiplies by .001 to reduce to milliseconds from nanoseconds
                times[ i - 2 ] = totalTime / threadCount;
                avgGetMilli[ i - 2 ] = (getMilli / numberOfGet);
                avgPutMilli[ i - 2 ] = (putMilli / numberOfPut);
                avgDeleteMilli[ i - 2 ] = (deleteMilli / numberOfDelete);
            }

            tree.end();
        }

        double totalThroughput = 0;
        double totalTimeOverall = 0;
        double avgGetMilliTime = 0;
        double avgPutMilliTime = 0;
        double avgDeleteMilliTime = 0;
        for(int i = 0 ; i < 5 ; i++ ){
            totalThroughput += throughputs[i];
            totalTimeOverall += times[i];
            avgGetMilliTime += avgGetMilli[i];
            avgPutMilliTime += avgPutMilli[i];
            avgDeleteMilliTime += avgDeleteMilli[i];
        }
        double avgThroughput = totalThroughput / 5;
        double avgTime = totalTimeOverall / 5;

        avgGetMilliTime = avgGetMilliTime / 5;
        avgPutMilliTime = avgPutMilliTime / 5;
        avgDeleteMilliTime = avgDeleteMilliTime / 5;

        //Dao.storeQueueInDB(queueType,threadCount, avgThroughput, n);
        System.out.println(String.format("put:[%d],get:[%d],delete:[%d]", numberOfPut, numberOfGet, numberOfDelete));
        System.out.println(String.format("put milli:[%f],get milli:[%f],delete mill:[%f]", avgPutMilliTime, avgGetMilliTime, avgDeleteMilliTime));
        System.out.println(String.format("%f", avgThroughput));

    }

    private static SearchTree<Integer, Integer> getTree(String treeType, int threadCount) {
        SearchTree<Integer, Integer> tree = null;
        switch (treeType){
            case "RBTCoarseGrained":
                tree = new RedBlackTreeCoarseGrained<>();
                break;
            case "RBTLockFree":
                tree = new RedBlackTreeLockFree<>();
                break;
            case "RBTTransactional":
                tree = new TransactionalRBTreeSet<Integer, Integer>();
                break;
            case "RBTCompositional":
                tree = new CompositionalRBTreeIntSet<Integer, Integer>();
                break;
            case "RBTFlatCombineV1":
                tree = new RedBlackTreeFlatCombine(threadCount);
                break;
            case "RBTFlatCombineV2":
                tree = new RedBlackTreeFlatCombinev2(threadCount);
                break;
            case "RBTFlatCombineV3":
                tree = new RedBlackTreeFlatCombinev3(threadCount);
                break;
            case "RBTFlatCombineV4":
                tree = new RedBlackTreeFlatCombinev4(threadCount);
                break;
            default:
                System.out.println("Unknown tree type " + treeType);
                System.exit(1);
        }
        return tree;
    }

}
