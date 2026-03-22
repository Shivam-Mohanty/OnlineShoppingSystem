package org.example; // Use your actual package name

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    // Database credentials
    private static final String URL = "jdbc:postgresql://localhost:5432/onlinestore";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    /**
     * Fetches all products from the PostgreSQL database.
     * This keeps your Java logic consistent and reusable.
     */
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        // FIX: Added 'stock_count' to the SELECT statement
        String query = "SELECT id, name, price, stock_count FROM products";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                // FIX: Pass the stock_count from the database into the constructor
                productList.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock_count") // This pulls the actual number from PG
                ));
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return productList;
    }

    public void addProduct(String name, double price, int stock) {
        String query = "INSERT INTO products (name, price, stock_count) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, stock);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error adding product: " + e.getMessage());
        }
    }

    public void reduceStock(int productId) {
        String query = "UPDATE products SET stock_count = stock_count - 1 WHERE id = ? AND stock_count > 0";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, productId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error updating stock: " + e.getMessage());
        }
    }
}
