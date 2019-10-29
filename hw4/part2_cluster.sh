echo "Cluster 2" 
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SimpleHLock 4 640000 2
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SimpleHLock 8 640000 2
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SimpleHLock 16 640000 2
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SimpleHLock 32 640000 2
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SimpleHLock 50 640000 2
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SimpleHLock 64 640000 2
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SimpleHLock 128 640000 2
echo "Cluster 3" 
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SimpleHLock 4 640000 3
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SimpleHLock 8 640000 3
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SimpleHLock 16 640000 3
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SimpleHLock 32 640000 3
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SimpleHLock 50 640000 3
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SimpleHLock 64 640000 3
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SimpleHLock 128 640000 3
echo "Cluster 4" 
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SimpleHLock 4 640000 4
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SimpleHLock 8 640000 4
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SimpleHLock 16 640000 4
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SimpleHLock 32 640000 4
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SimpleHLock 50 640000 4
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SimpleHLock 64 640000 4
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster SimpleHLock 128 640000 4
echo "Cluster 2" 
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster HBOLock 4 640000 2
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster HBOLock 8 640000 2
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster HBOLock 16 640000 2
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster HBOLock 32 640000 2
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster HBOLock 50 640000 2
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster HBOLock 64 640000 2
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster HBOLock 128 640000 2
echo "Cluster 3" 
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster HBOLock 4 640000 3
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster HBOLock 8 640000 3
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster HBOLock 16 640000 3
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster HBOLock 32 640000 3
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster HBOLock 50 640000 3
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster HBOLock 64 640000 3
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster HBOLock 128 640000 3
echo "Cluster 4" 
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster HBOLock 4 640000 4
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster HBOLock 8 640000 4
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster HBOLock 16 640000 4
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster HBOLock 32 640000 4
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster HBOLock 50 640000 4
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster HBOLock 64 640000 4
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark cluster HBOLock 128 640000 4
echo "SpinSleepLock"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster SpinSleepLock 4 640000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster SpinSleepLock 8 640000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster SpinSleepLock 16 640000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster SpinSleepLock 32 640000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster SpinSleepLock 50 640000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster SpinSleepLock 64 640000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster SpinSleepLock 128 640000
echo "TAS"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  TASLock 4 640000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  TASLock 8 640000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  TASLock 16 640000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  TASLock 32 640000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  TASLock 50 640000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  TASLock 64 640000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  TASLock 128 640000
echo "TTAS"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  TTASLock 4 640000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  TTASLock 8 640000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  TTASLock 16 640000 
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  TTASLock 32 640000 
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  TTASLock 50 640000 
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  TTASLock 64 640000 
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  TTASLock 128 640000 
echo "Backoff"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  BackoffLock 4 640000 Fixed
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  BackoffLock 8 640000 Fixed
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  BackoffLock 16 640000 Fixed
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  BackoffLock 32 640000 Fixed
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  BackoffLock 50 640000 Fixed
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  BackoffLock 64 640000 Fixed
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  BackoffLock 128 640000 Fixed
echo "CLH"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  CLHLock 4 640000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  CLHLock 8 640000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  CLHLock 16 640000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  CLHLock 32 640000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  CLHLock 50 640000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  CLHLock 64 640000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  CLHLock 128 640000
echo "MCS"
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  MCSLock 4 640000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  MCSLock 8 640000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  MCSLock 16 640000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  MCSLock 32 640000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  MCSLock 50 640000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  MCSLock 64 640000
java -cp build/libs/hw4.jar edu.vt.ece.hw4.Benchmark  cluster  MCSLock 128 640000



