package com.shoppingcart;

import java.util.List;

public class ShoppingCartCalculator {

    public double calculateItemTotal(double price, int quantity) {
        if (price < 0 || quantity < 0) {
            throw new IllegalArgumentException("Price and quantity must be non-negative.");
        }
        return price * quantity;
    }

    public double calculateCartTotal(List<double[]> items) {
        // Each double[] is {price, quantity}
        double total = 0;
        for (double[] item : items) {
            total += calculateItemTotal(item[0], (int) item[1]);
        }
        return total;
    }
}