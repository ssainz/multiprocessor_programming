echo "TAS"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty TASLock 4 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty TASLock 8 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty TASLock 16 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty TASLock 32 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty TASLock 50 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty TASLock 64 64000
echo "TTAS"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty TTASLock 4 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty TTASLock 8 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty TTASLock 16 64000 
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty TTASLock 32 64000 
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty TTASLock 50 64000 
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty TTASLock 64 64000 
echo "Backoff"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty BackoffLock 4 64000 Fixed
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty BackoffLock 8 64000 Fixed
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty BackoffLock 16 64000 Fixed
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty BackoffLock 32 64000 Fixed
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty BackoffLock 50 64000 Fixed
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty BackoffLock 64 64000 Fixed
echo "CLH"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty CLHLock 4 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty CLHLock 8 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty CLHLock 16 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty CLHLock 32 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty CLHLock 50 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty CLHLock 64 64000
echo "MCS"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty MCSLock 4 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty MCSLock 8 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty MCSLock 16 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty MCSLock 32 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty MCSLock 50 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark empty MCSLock 64 64000





