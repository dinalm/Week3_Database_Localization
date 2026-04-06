package com.shoppingcart;

import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.*;

public class MainController {

    @FXML private ComboBox<String> languageComboBox;
    @FXML private Button confirmLanguageBtn;
    @FXML private Label enterNumItemsLabel;
    @FXML private TextField numItemsField;
    @FXML private Button enterItemsBtn;
    @FXML private VBox itemsContainer;
    @FXML private Button calculateBtn;
    @FXML private Label totalLabel;
    @FXML private Label selectLanguageLabel;

    private Map<String, String> strings = new HashMap<>();
    private String currentLanguage = "en_US";

    private final ShoppingCartCalculator calculator = new ShoppingCartCalculator();
    private final LocalizationService localizationService = new LocalizationService();
    private final CartService cartService = new CartService();

    private final Map<String, String> localeMap = new LinkedHashMap<>();

    @FXML
    public void initialize() {
        localeMap.put("English",  "en_US");
        localeMap.put("Finnish",  "fi_FI");
        localeMap.put("Swedish",  "sv_SE");
        localeMap.put("Japanese", "ja_JP");
        localeMap.put("Arabic",   "ar_AR");

        languageComboBox.getItems().addAll(localeMap.keySet());
        languageComboBox.setValue("English");

        strings = localizationService.getStrings("en_US");
        applyStrings();
    }

    @FXML
    private void onConfirmLanguage() {
        String selected = languageComboBox.getValue();
        currentLanguage = localeMap.getOrDefault(selected, "en_US");
        strings = localizationService.getStrings(currentLanguage);
        applyStrings();

        if (selected.equals("Arabic")) {
            languageComboBox.getScene().getRoot().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        } else {
            languageComboBox.getScene().getRoot().setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        }
    }

    @FXML
    private void onEnterItems() {
        itemsContainer.getChildren().clear();
        try {
            int numItems = Integer.parseInt(numItemsField.getText().trim());
            if (numItems <= 0) throw new NumberFormatException();

            for (int i = 1; i <= numItems; i++) {
                Label priceLabel = new Label(strings.get("enter.price") + " " + i + ":");
                TextField priceField = new TextField();
                priceField.setPromptText(strings.get("price.prompt"));

                Label qtyLabel = new Label(strings.get("enter.quantity") + " " + i + ":");
                TextField qtyField = new TextField();
                qtyField.setPromptText(strings.get("quantity.prompt"));

                itemsContainer.getChildren().addAll(priceLabel, priceField, qtyLabel, qtyField);
            }
        } catch (NumberFormatException e) {
            showAlert(strings.get("error.invalid.number"));
        }
    }

    @FXML
    private void onCalculateTotal() {
        List<double[]> items = new ArrayList<>();
        var children = itemsContainer.getChildren();

        try {
            for (int i = 1; i < children.size(); i += 4) {
                TextField priceField = (TextField) children.get(i);
                TextField qtyField   = (TextField) children.get(i + 2);

                double price = Double.parseDouble(priceField.getText().trim());
                int qty      = Integer.parseInt(qtyField.getText().trim());
                items.add(new double[]{price, qty});
            }

            double total = calculator.calculateCartTotal(items);
            totalLabel.setText(strings.get("total.cost") + " " + String.format("%.2f", total));

            // Save to database
            cartService.saveCart(items.size(), total, currentLanguage, items);

        } catch (NumberFormatException e) {
            showAlert(strings.get("error.invalid.input"));
        } catch (IllegalArgumentException e) {
            showAlert(e.getMessage());
        }
    }

    private void applyStrings() {
        selectLanguageLabel.setText(strings.getOrDefault("select.language", "Select the language:"));
        confirmLanguageBtn.setText(strings.getOrDefault("confirm.language", "Confirm Language"));
        enterNumItemsLabel.setText(strings.getOrDefault("enter.num.items", "Enter number of items:"));
        enterItemsBtn.setText(strings.getOrDefault("enter.items.btn", "Enter Items"));
        calculateBtn.setText(strings.getOrDefault("calculate.btn", "Calculate Total"));
        totalLabel.setText(strings.getOrDefault("total.cost", "Total cost:") + " -");
        numItemsField.setPromptText(strings.getOrDefault("num.items.prompt", "Number of items"));
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
}