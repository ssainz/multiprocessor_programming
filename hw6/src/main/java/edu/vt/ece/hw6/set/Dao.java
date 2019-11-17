package edu.vt.ece.hw6.set;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Dao {

    public static boolean storeInDB(String setType, int numberThreads, double percentage, double avgThroughput, double avgTime){


        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://hw5.ctbqkxvip5yo.us-east-1.rds.amazonaws.com:5432/postgres", "postgres", "rushrush")) {

            if (conn != null) {
                //System.out.println("Connected to the database!");
            } else {
                //System.out.println("Failed to make connection!");
            }

            Statement stmt = conn.createStatement();
            String stmtStr = String.format("INSERT INTO set_results (setType,numThreads,percentage,avgThroughput,avgTime) VALUES ('%s',%d,%f,%f,%f);", setType, numberThreads, percentage, avgThroughput, avgTime);
            stmt.executeUpdate(stmtStr);
            //System.out.println(stmtStr);
            /*
            CREATE TABLE SET_RESULTS (
            setType varchar(50),
            numThreads INTEGER,
            percentage REAL,
            avgThroughput REAL,
            avgTime REAL
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

    public static boolean storeInDBBAG(String setType, int numberThreads,  double avgThroughput, double avgTime){


        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://hw5.ctbqkxvip5yo.us-east-1.rds.amazonaws.com:5432/postgres", "postgres", "rushrush")) {

            if (conn != null) {
                //System.out.println("Connected to the database!");
            } else {
                //System.out.println("Failed to make connection!");
            }

            Statement stmt = conn.createStatement();
            String stmtStr = String.format("INSERT INTO set_results_bag (setType,numThreads,avgThroughput,avgTime) VALUES ('%s',%d,%f,%f);", setType, numberThreads, avgThroughput, avgTime);
            stmt.executeUpdate(stmtStr);
            System.out.println(stmtStr);
            /*
            CREATE TABLE SET_RESULTS (
            setType varchar(50),
            numThreads INTEGER,
            percentage REAL,
            avgThroughput REAL,
            avgTime REAL
            );
             */

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
