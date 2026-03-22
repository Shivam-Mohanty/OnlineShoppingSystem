package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestDB {
    public static void main(String[] args) {
        // Use the database name you created in the terminal
        String url = "jdbc:postgresql://localhost:5432/onlinestore";
        String user = "postgres";
        String password = "postgres"; // Use the password you set/found earlier

        System.out.println("Connecting to database...");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if (conn != null) {
                System.out.println("Connection successful!");

                // Let's try to read the data you inserted in the terminal
                String query = "SELECT * FROM products";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                System.out.println("--- Current Products in Database ---");
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id") +
                            " | Name: " + rs.getString("name") +
                            " | Price: $" + rs.getDouble("price"));
                }
            }
        } catch (Exception e) {
            System.err.println("Connection failed!");
            e.printStackTrace();
        }
    }
}
