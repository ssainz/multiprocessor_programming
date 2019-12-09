public class BuildBenchmarkCommands {

    public static void main(String[] args){

        /* HOW TO RUN
        java -cp build/libs/rbtree.jar BuildBenchmarkCommands > test_search_tree.sh
         */

        String[] methods = {"RBTCoarseGrained", "RBTLockFree","RBTCompositional","RBTTransactional","RBTFlatCombineV1", "RBTFlatCombineV2", "RBTFlatCombineV3", "RBTFlatCombineV4"};
        int[] threads = {4,8,10,15,20,25,30,35,40,45,64};
        int[] load = {640000};

        for(int i = 0 ; i < methods.length; i++){

            for(int j = 0 ; j < threads.length ; j++){

                for(int z = 0; z < load.length ; z++){

                    int threadNum = threads[j];

                    String m = methods[i];

                    if(m.contains("RBTFlatCombine")){
                        threadNum--; // consider the worker thread
                    }

                    String t = Integer.toString(threadNum);
                    String l = Integer.toString(load[z]);


                    String modifier = "";
                    if(m.equals("RBTCompositional") || m.equals("RBTTransactional")){
                        modifier = "-javaagent:libs/deuceAgent-1.3.0.jar";
                    }

                    String cmd = String.format("java %s -cp build/libs/rbtree.jar RedBlackTreeTest %s %s %s", modifier, m, threadNum, l);

                    System.out.println(cmd);

                }

            }

        }

    }
}
