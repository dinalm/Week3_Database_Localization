package com.shoppingcart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartCalculatorTest {

    private ShoppingCartCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new ShoppingCartCalculator();
    }

    @Test
    void testSingleItemTotal() {
        assertEquals(20.0, calculator.calculateItemTotal(5.0, 4));
    }

    @Test
    void testItemTotalWithZeroQuantity() {
        assertEquals(0.0, calculator.calculateItemTotal(10.0, 0));
    }

    @Test
    void testCartTotalMultipleItems() {
        List<double[]> items = List.of(
                new double[]{2.5, 4},   // 10.0
                new double[]{3.0, 3},   // 9.0
                new double[]{5.0, 2}    // 10.0
        );
        assertEquals(29.0, calculator.calculateCartTotal(items));
    }

    @Test
    void testCartTotalSingleItem() {
        List<double[]> items = List.of(new double[]{7.99, 3});
        assertEquals(23.97, calculator.calculateCartTotal(items), 0.001);
    }

    @Test
    void testNegativePriceThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateItemTotal(-1.0, 2));
    }

    @Test
    void testNegativeQuantityThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateItemTotal(5.0, -1));
    }

    @Test
    void testEmptyCart() {
        assertEquals(0.0, calculator.calculateCartTotal(List.of()));
    }
}