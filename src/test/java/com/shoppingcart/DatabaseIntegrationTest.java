package com.shoppingcart;

import org.junit.jupiter.api.*;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DatabaseIntegrationTest {

    private LocalizationService localizationService;
    private CartService cartService;

    @BeforeEach
    void setUp() {
        localizationService = new LocalizationService();
        cartService = new CartService();
    }

    @Test
    @Order(1)
    void testLocalizationServiceEnglish() {
        Map<String, String> strings = localizationService.getStrings("en_US");
        assertFalse(strings.isEmpty(), "English strings should not be empty");
        assertEquals("Calculate Total", strings.get("calculate.btn"));
        assertEquals("Total cost:", strings.get("total.cost"));
    }

    @Test
    @Order(2)
    void testLocalizationServiceFinnish() {
        Map<String, String> strings = localizationService.getStrings("fi_FI");
        assertFalse(strings.isEmpty(), "Finnish strings should not be empty");
        assertEquals("Laske kokonaishinta", strings.get("calculate.btn"));
    }

    @Test
    @Order(3)
    void testLocalizationServiceArabic() {
        Map<String, String> strings = localizationService.getStrings("ar_AR");
        assertFalse(strings.isEmpty(), "Arabic strings should not be empty");
        assertNotNull(strings.get("total.cost"));
    }

    @Test
    @Order(4)
    void testLocalizationServiceAllLanguages() {
        List<String> languages = List.of("en_US", "fi_FI", "sv_SE", "ja_JP", "ar_AR");
        for (String lang : languages) {
            Map<String, String> strings = localizationService.getStrings(lang);
            assertFalse(strings.isEmpty(), lang + " strings should not be empty");
        }
    }

    @Test
    @Order(5)
    void testCartServiceSavesRecord() {
        List<double[]> items = List.of(
                new double[]{10.0, 2},
                new double[]{5.0, 3}
        );
        // Should not throw any exception
        assertDoesNotThrow(() ->
                cartService.saveCart(items.size(), 35.0, "en_US", items)
        );
    }

    @Test
    @Order(6)
    void testCartServiceMultipleLanguages() {
        List<double[]> items = List.of(new double[]{20.0, 1});

        assertDoesNotThrow(() -> cartService.saveCart(1, 20.0, "fi_FI", items));
        assertDoesNotThrow(() -> cartService.saveCart(1, 20.0, "ar_AR", items));
    }
}
