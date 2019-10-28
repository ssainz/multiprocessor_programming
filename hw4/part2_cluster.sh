echo "Cluster 2" 
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SpinSleepLock 4 64000 2
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SpinSleepLock 8 64000 2
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SpinSleepLock 16 64000 2
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SpinSleepLock 32 64000 2
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SpinSleepLock 50 64000 2
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SpinSleepLock 64 64000 2
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SpinSleepLock 128 64000 2
echo "Cluster 3" 
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SpinSleepLock 4 64000 3
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SpinSleepLock 8 64000 3
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SpinSleepLock 16 64000 3
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SpinSleepLock 32 64000 3
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SpinSleepLock 50 64000 3
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SpinSleepLock 64 64000 3
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SpinSleepLock 128 64000 3
echo "Cluster 4" 
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SpinSleepLock 4 64000 4
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SpinSleepLock 8 64000 4
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SpinSleepLock 16 64000 4
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SpinSleepLock 32 64000 4
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SpinSleepLock 50 64000 4
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SpinSleepLock 64 64000 4
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SpinSleepLock 128 64000 4
echo "SpinSleepLock"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster SpinSleepLock 4 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster SpinSleepLock 8 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster SpinSleepLock 16 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster SpinSleepLock 32 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster SpinSleepLock 50 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster SpinSleepLock 64 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster SpinSleepLock 128 64000
echo "TAS"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  TASLock 4 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  TASLock 8 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  TASLock 16 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  TASLock 32 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  TASLock 50 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  TASLock 64 64000
echo "TTAS"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  TTASLock 4 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  TTASLock 8 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  TTASLock 16 64000 
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  TTASLock 32 64000 
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  TTASLock 50 64000 
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  TTASLock 64 64000 
echo "Backoff"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  BackoffLock 4 64000 Fixed
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  BackoffLock 8 64000 Fixed
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  BackoffLock 16 64000 Fixed
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  BackoffLock 32 64000 Fixed
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  BackoffLock 50 64000 Fixed
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  BackoffLock 64 64000 Fixed
echo "CLH"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  CLHLock 4 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  CLHLock 8 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  CLHLock 16 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  CLHLock 32 64000
echo "MCS"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  MCSLock 4 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  MCSLock 8 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  MCSLock 16 64000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  MCSLock 32 64000



