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
 *
 * @author ADMIN
 */
public class DatabaseConnection implements AutoCloseable {

    Connection connection;
    private static boolean isFirstConnection = true;

    public static void dangKyDriver() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("MySQL driver đăng ký thành công");
    }

    public DatabaseConnection() throws SQLException {
        this.connection = getConnection();
    }

    @Override
    public void close() throws SQLException {
        if (this.connection != null && !this.connection.isClosed()) {
            this.connection.close();
        }
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        Statement statement = this.connection.createStatement();
        return statement.executeQuery(sql);
    }

    public int executeUpdate(String sql) throws SQLException {
        Statement statement = this.connection.createStatement();
        return statement.executeUpdate(sql);
    }

    private Connection getConnection() throws SQLException {
        String serverIP = "127.0.0.1";  // localhost
        String databaseName = "thietkethucdonapp";
        String user = "root";
        String pass = "111111";
        return getConnection(serverIP, databaseName, user, pass);
    }

    private Connection getConnection(
            String serverIP,
            String databaseName,
            String user,
            String pass) throws SQLException {
        StringBuilder sbd = new StringBuilder();
        sbd.append("jdbc:mysql://");
        sbd.append(serverIP).append(":3306");
        sbd.append("/").append(databaseName);
        sbd.append("?user=").append(user);
        sbd.append("&password=").append(pass);
        
        try {
            Connection conn = DriverManager.getConnection(sbd.toString());
            
            if (isFirstConnection) {
                System.out.println("Kết nối database thành công!");
                isFirstConnection = false;
            }
            
            return conn;
        } catch (SQLException e) {
            System.err.println("Kết nối database thất bại!: " + e.getMessage());
            throw e;
        }
    }
}
