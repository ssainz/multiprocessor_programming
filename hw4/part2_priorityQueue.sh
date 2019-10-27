echo "PriorityQueue"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normalPriority PriorityQueueLock 2 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normalPriority PriorityQueueLock 4 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normalPriority PriorityQueueLock 8 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normalPriority PriorityQueueLock 16 64000
#java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normalPriority PriorityQueueLock 32 64000
#java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normalPriority PriorityQueueLock 50 64000
#java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normalPriority PriorityQueueLock 64 64000
echo "PriorityQueueTimeout"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normalPriority PriorityQueueLock 2 64000 Timeout
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normalPriority PriorityQueueLock 4 64000 Timeout
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normalPriority PriorityQueueLock 8 64000 Timeout
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normalPriority PriorityQueueLock 16 64000 Timeout
#java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normalPriority PriorityQueueLock 32 64000 Timeout
#java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normalPriority PriorityQueueLock 50 64000 Timeout
#java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normalPriority PriorityQueueLock 64 64000 Timeout
echo "CLH"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normalPriority CLHLock 2 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normalPriority CLHLock 4 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normalPriority CLHLock 8 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normalPriority CLHLock 16 64000
#java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normalPriority CLHLock 32 64000
#java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normalPriority CLHLock 50 64000
#java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark normalPriority CLHLock 64 64000





