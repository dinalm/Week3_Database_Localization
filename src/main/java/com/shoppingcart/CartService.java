package com.shoppingcart;

import java.sql.*;
import java.util.List;
import java.util.logging.Logger;

public class CartService {

    private static final Logger logger = Logger.getLogger(CartService.class.getName());

    public void saveCart(int totalItems, double totalCost, String language, List<double[]> items) {
        String insertCart = "INSERT INTO cart_records (total_items, total_cost, language) VALUES (?, ?, ?)";
        String insertItem = "INSERT INTO cart_items (cart_record_id, item_number, price, quantity, subtotal) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement cartStmt = conn.prepareStatement(insertCart, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement itemStmt = conn.prepareStatement(insertItem)) {

            conn.setAutoCommit(false);

            cartStmt.setInt(1, totalItems);
            cartStmt.setDouble(2, totalCost);
            cartStmt.setString(3, language);
            cartStmt.executeUpdate();

            try (ResultSet keys = cartStmt.getGeneratedKeys()) {
                int cartId = 0;
                if (keys.next()) {
                    cartId = keys.getInt(1);
                }

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
            }

            conn.commit();
            logger.info("Cart saved successfully");

        } catch (SQLException e) {
            logger.severe("Error saving cart: " + e.getMessage());
        }
    }
}