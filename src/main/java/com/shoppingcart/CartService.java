package com.shoppingcart;

import java.sql.*;
import java.util.List;

public class CartService {

    public void saveCart(int totalItems, double totalCost, String language, List<double[]> items) {
        String insertCart = "INSERT INTO cart_records (total_items, total_cost, language) VALUES (?, ?, ?)";
        String insertItem = "INSERT INTO cart_items (cart_record_id, item_number, price, quantity, subtotal) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            // Save main cart record
            PreparedStatement cartStmt = conn.prepareStatement(insertCart, Statement.RETURN_GENERATED_KEYS);
            cartStmt.setInt(1, totalItems);
            cartStmt.setDouble(2, totalCost);
            cartStmt.setString(3, language);
            cartStmt.executeUpdate();

            // Get generated cart record ID
            ResultSet keys = cartStmt.getGeneratedKeys();
            int cartId = 0;
            if (keys.next()) {
                cartId = keys.getInt(1);
            }

            // Save each item
            PreparedStatement itemStmt = conn.prepareStatement(insertItem);
            for (int i = 0; i < items.size(); i++) {
                double price    = items.get(i)[0];
                int quantity    = (int) items.get(i)[1];
                double subtotal = price * quantity;

                itemStmt.setInt(1, cartId);
                itemStmt.setInt(2, i + 1);
                itemStmt.setDouble(3, price);
                itemStmt.setInt(4, quantity);
                itemStmt.setDouble(5, subtotal);
                itemStmt.addBatch();
            }
            itemStmt.executeBatch();

            conn.commit();
            System.out.println("Cart saved successfully with ID: " + cartId);

        } catch (SQLException e) {
            System.err.println("Error saving cart: " + e.getMessage());
        }
    }
}