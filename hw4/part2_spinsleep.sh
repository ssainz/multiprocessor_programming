echo "SpinSleepLock"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long SpinSleepLock 2 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long SpinSleepLock 4 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long SpinSleepLock 8 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long SpinSleepLock 16 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long SpinSleepLock 32 64000
#java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long SpinSleepLock 50 64000
#java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long SpinSleepLock 64 64000
echo "Backoff"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long BackoffLock 2 64000 Fixed
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long BackoffLock 4 64000 Fixed
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long BackoffLock 8 64000 Fixed
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long BackoffLock 16 64000 Fixed
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long BackoffLock 32 64000 Fixed
#java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long BackoffLock 50 64000 Fixed
#java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark long BackoffLock 64 64000 Fixed





