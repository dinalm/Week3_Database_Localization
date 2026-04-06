package com.shoppingcart;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class LocalizationService {

    public Map<String, String> getStrings(String language) {
        Map<String, String> strings = new HashMap<>();

        String sql = "SELECT `key`, value FROM localization_strings WHERE language = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, language);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                strings.put(rs.getString("key"), rs.getString("value"));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching localization strings: " + e.getMessage());
        }

        return strings;
    }
}