
package com.quantitymeasurementapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayDeque;
import java.util.Queue;

public class ConnectionPool {

    private static final int POOL_SIZE = 5;
    private static final Queue<Connection> pool = new ArrayDeque<>();

    static {

        try {

            String url = DatabaseConfig.getProperty("db.url");
            String user = DatabaseConfig.getProperty("db.username");
            String password = DatabaseConfig.getProperty("db.password");

            for (int i = 0; i < POOL_SIZE; i++) {
                Connection connection = DriverManager.getConnection(url, user, password);
                pool.add(connection);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error initializing connection pool", e);
        }
    }

    public static synchronized Connection getConnection() throws Exception {

        if (pool.isEmpty()) {
            throw new RuntimeException("No DB connections available");
        }

        return pool.poll();
    }

    public static synchronized void releaseConnection(Connection connection) {
        pool.add(connection);
    }
}