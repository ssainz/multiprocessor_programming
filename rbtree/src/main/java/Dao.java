import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Dao {

    public static boolean storeInDB(String method, int numberThreads,
                                         int numGet,int numPut, int numDelete,
                                         double avgGetTime, double avgPutTime, double avgDeleteTime,
                                         double avgThroughput, int load){


        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://multiprocessor.ctbqkxvip5yo.us-east-1.rds.amazonaws.com:5432/postgres", "postgres", "rushrush")) {

            if (conn != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }

            // Hostname
            String hostname = InetAddress.getLocalHost().getHostName();

            Statement stmt = conn.createStatement();
            String stmtStr = String.format("INSERT INTO results (hostname, method,numThreads,numGet,numPut,numDelete,avgGetTime," +
                    "avgPutTime,avgDeleteTime,avgThroughput,load, times) VALUES ('%s','%s',%d,%d,%d,%d,%f,%f,%f,%f,%d, now());", hostname, method, numberThreads,
                    numGet,numPut,numDelete,
                    avgGetTime,avgPutTime,avgDeleteTime,
                    avgThroughput, load);
            //System.out.println(stmtStr);
            stmt.executeUpdate(stmtStr);

            /*
            drop table results;
            CREATE TABLE RESULTS (
            hostname varchar(50),
            method varchar(50),
            numThreads INTEGER,
            numGet  INTEGER,
            numPut  INTEGER,
            numDelete INTEGER,
            avgGetTime REAL,
            avgPutTime REAL,
            avgDeleteTime REAL,
            avgThroughput REAL,
            load INTEGER,
            times TIMESTAMP
            );
             */

        } catch (SQLException e) {
            //System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }

        return true;
    }
}
