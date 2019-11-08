package edu.vt.ece.hw5.set;

public class Benchmark {

    // Benchmark Set Implementation here.
    public static void main(String[] args) throws InterruptedException {
        // Fill this as you please, but you may be better off reusing code from
        // previous homeworks

        final int threadCount = Integer.parseInt(args[0]);
        final int iters = Integer.parseInt(args[1]);
        final double percentageOfContains = Double.parseDouble(args[1]);
        final String listType = args[3];
        //
        ConcurrentSet set = null;
        switch (listType){
            case "CoarseList":
                set = new CoarseList();
                break;
            case "FineList":
                set = new FineList();
                break;
            case "LazyList":
                set = new LazyList();
                break;
            case "LockFreeList":
                set = new LockFreeList();
                break;
            case "OptimisticList":
                set = new OptimisticList();
                break;
            default:
                set = new CoarseList();
        }

        double[] throughputs = new double[5];
        double[] times = new double[5];
        for(int i = 0 ; i <= 6 ; i++){ // 7 times, first two times are warm up
            final SetTestThread[] threads = new SetTestThread[threadCount];

            for (int t = 0; t < threadCount; t++) {
                threads[t] = new SetTestThread(iters, set, percentageOfContains);
            }

            for (int t = 0; t < threadCount; t++) {
                threads[t].start();
            }

            long totalTime = 0;
            long maxTime = 0;
            for (int t = 0; t < threadCount; t++) {
                threads[t].join();
                totalTime += threads[t].getElapsedTime();
                maxTime = Math.max(maxTime, threads[t].getElapsedTime());


            }

            if(i > 1){
                throughputs[ i - 2 ] = (iters*threadCount) / (maxTime*0.001);
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


        System.out.println(String.format("[%s\t][Avg.Througput][Avg.Time]  %d \t %d",listType, avgThroughput, avgTime) );

        //System.out.println("Bag: Average time per thread is " + totalTime / threadCount + "ms");

    }

}
