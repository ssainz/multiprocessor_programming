echo "SpinSleepLock"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long SpinSleepLock 4 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long SpinSleepLock 8 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long SpinSleepLock 16 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long SpinSleepLock 32 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long SpinSleepLock 50 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long SpinSleepLock 64 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long SpinSleepLock 128 64000
echo "TAS"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long TASLock 4 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long TASLock 8 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long TASLock 16 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long TASLock 32 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long TASLock 50 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long TASLock 64 64000
echo "TTAS"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long TTASLock 4 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long TTASLock 8 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long TTASLock 16 64000 
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long TTASLock 32 64000 
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long TTASLock 50 64000 
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long TTASLock 64 64000 
echo "Backoff"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long BackoffLock 4 64000 Fixed
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long BackoffLock 8 64000 Fixed
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long BackoffLock 16 64000 Fixed
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long BackoffLock 32 64000 Fixed
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long BackoffLock 50 64000 Fixed
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long BackoffLock 64 64000 Fixed
echo "CLH"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long CLHLock 4 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long CLHLock 8 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long CLHLock 16 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long CLHLock 32 64000
echo "MCS"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long MCSLock 4 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long MCSLock 8 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long MCSLock 16 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long MCSLock 32 64000




