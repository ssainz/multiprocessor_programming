echo "PriorityQueue"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normal PriorityQueueLock 4 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normal PriorityQueueLock 8 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normal PriorityQueueLock 16 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normal PriorityQueueLock 32 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normal PriorityQueueLock 50 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normal PriorityQueueLock 64 64000
echo "PriorityQueueTimeout"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normal PriorityQueueLock 4 64000 Timeout
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normal PriorityQueueLock 8 64000 Timeout
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normal PriorityQueueLock 16 64000 Timeout
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normal PriorityQueueLock 32 64000 Timeout
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normal PriorityQueueLock 50 64000 Timeout
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normal PriorityQueueLock 64 64000 Timeout
echo "CLH"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normal CLHLock 4 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normal CLHLock 8 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normal CLHLock 16 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normal CLHLock 32 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normal CLHLock 50 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normal CLHLock 64 64000





