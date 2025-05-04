/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseConnection class handles connecting to the MySQL database
 */
public class DatabaseConnection {
    public Connection connection;
    
    /**
     * Register the MySQL JDBC driver
     */
    public static void dangKyDriver() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("MySQL driver registered successfully");
    }

    /**
     * Create a new database connection
     */
    public DatabaseConnection() throws SQLException {
        this.connection = getConnection();
    }

    /**
     * Close the database connection
     */
    public void close() throws SQLException {
        if (this.connection != null && !this.connection.isClosed()) {
            this.connection.close();
        }
    }

    /**
     * Execute a SELECT query
     */
    public ResultSet executeQuery(String sql) throws SQLException {
        Statement statement = this.connection.createStatement();
        return statement.executeQuery(sql);
    }

    /**
     * Execute an INSERT, UPDATE, or DELETE query
     */
    public int executeUpdate(String sql) throws SQLException {
        Statement statement = this.connection.createStatement();
        return statement.executeUpdate(sql);
    }

    /**
     * Get a connection with default parameters
     */
    private Connection getConnection() throws SQLException {
        String serverIP = "127.0.0.1";     // localhost
        String databaseName = "thietkethucdonapp";
        String user = "root";
        String pass = "111111";
        return getConnection(serverIP, databaseName, user, pass);
    }

    /**
     * Get a connection with custom parameters
     */
    private Connection getConnection(
            String serverIP,
            String databaseName,
            String user,
            String pass) throws SQLException {
        // Use the correct MySQL connection URL format
        String connectionUrl = "jdbc:mysql://" + serverIP + ":3306/" + databaseName + 
                               "?user=" + user + "&password=" + pass;
        
        try {
            Connection conn = DriverManager.getConnection(connectionUrl);
            System.out.println("Connected to database successfully!");
            return conn;
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            throw e;
        }
    }
}
