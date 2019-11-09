package edu.vt.ece.hw5.bag;

import java.util.concurrent.CyclicBarrier;

public class Benchmark {

    public static void main(String[] args) throws Exception {
        //final int threadCount = Integer.parseInt(args[0]);
        final int iters = Integer.parseInt(args[0]);

        //int[] threadsNumber = {4,9,14,19,20,24,29,35,40};
        int[] threadsNumber = {4};

        for(int i = 0 ; i < threadsNumber.length ; i++){


            int threadCount = threadsNumber[i];
            System.out.println("Start eval with " + threadCount + " threads");
            int times = 5;

            double[] throughputs1 = new double[5];
            double[] avgTimes1 = new double[5];
            double[] throughputs2 = new double[5];
            double[] avgTimes2 = new double[5];

            for(int j = 0 ; j < times + 2; j++){

                final TestThread[] threads = new TestThread[threadCount];
                final LockFreeBag<Integer> bag = new LockFreeBag<>();
                final LockFreeList<Integer> list = new LockFreeList<>();
                final CyclicBarrier barrier = new CyclicBarrier(threadCount);

                for (int t = 0; t < threadCount; t++) {
                    threads[t] = new TestThread(iters, bag, list, barrier);
                }

                for (int t = 0; t < threadCount; t++) {
                    threads[t].start();
                }

                long totalTime1 = 0;
                long totalTime2 = 0;
                long maxTime1 = 0;
                long maxTime2 = 0;
                for (int t = 0; t < threadCount; t++) {
                    threads[t].join();
                    totalTime1 += threads[t].getElapsedTime1();
                    totalTime2 += threads[t].getElapsedTime2();
                    maxTime1 = Math.max(maxTime1, threads[t].getElapsedTime1());
                    maxTime2 = Math.max(maxTime2, threads[t].getElapsedTime2());
                }




//                System.out.println("Bag: Throughput is " + (iters*threadCount) / (maxTime1*0.001) + "ops/s");
//                System.out.println("Bag: Average time per thread is " + totalTime1 / threadCount + "ms");
//
//                System.out.println("List: Throughput is " + (iters*threadCount) / (maxTime2*0.001) + "ops/s");
//                System.out.println("List: Average time per thread is " + totalTime2 / threadCount + "ms");

                if(j > 1){
                    throughputs1[j - 2] = (iters*threadCount) / (maxTime1*0.001);
                    avgTimes1[j - 2] = totalTime1 / threadCount;
                    throughputs2[j - 2] = (iters*threadCount) / (maxTime2*0.001);
                    avgTimes2[j - 2] = totalTime2 / threadCount;
                }




            }

            double totalThroughput1 = 0.0;
            double totalAvgTimes1 = 0.0;
            double totalThroughput2 = 0.0;
            double totalAvgTimes2 = 0.0;
            for(int o = 0 ; o < 5 ; o++){
                totalAvgTimes1 += avgTimes1[o];
                totalThroughput1 += throughputs1[o];
                totalAvgTimes2 += avgTimes2[o];
                totalThroughput2 += throughputs2[o];
            }
            double throughput1 = totalThroughput1 / 5;
            double avgTotTimes1 = totalAvgTimes1 / 5;
            double throughput2 = totalThroughput2 / 5;
            double avgTotTimes2 = totalAvgTimes2 / 5;

            String s = String.format("[Bag\t][%d][%f][%f]",threadCount,throughput1,avgTotTimes1 );
            String s2 = String.format("[List\t][%d][%f][%f]",threadCount,throughput2,avgTotTimes2 );

            System.out.println(s);
            System.out.println(s2);

        }


    }
}
