package org.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.layout.Priority;
import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import javafx.util.Callback;

public class OnlineStoreGUI extends Application {

    private double totalAmount = 0.0;
    private Label totalLabel = new Label("Total: $0.00");
    private ObservableList<Product> cartItems = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        // 1. Initialize the DAO to fetch from your PostgreSQL database
        ProductDAO productDAO = new ProductDAO();

        // 2. Load products into a single ObservableList
        ObservableList<Product> products = FXCollections.observableArrayList(productDAO.getAllProducts());

        // 3. Setup the FilteredList for your search feature
        FilteredList<Product> filteredData = new FilteredList<>(products, p -> true);

        // 4. UI Components - Main Store
        TextField searchField = new TextField();
        searchField.setPromptText("Search products...");

        ListView<Product> productList = new ListView<>(filteredData);
        ListView<Product> cartList = new ListView<>(cartItems);

        // --- CUSTOM MODERN CELL FORMATTING ---
        Callback<ListView<Product>, ListCell<Product>> cellFactory = param -> new ListCell<>() {
            @Override
            protected void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);
                if (empty || product == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create separate labels for the left and right sides
                    Label nameLabel = new Label(product.getName());
                    Label priceLabel = new Label(String.format("$%.2f  |  Stock: %d", product.getPrice(), product.getStockCount()));

                    // Apply dark mode styling directly to the text
                    nameLabel.setStyle("-fx-text-fill: #E0E0E0; -fx-font-size: 15px;");
                    priceLabel.setStyle("-fx-text-fill: #BB86FC; -fx-font-weight: bold; -fx-font-size: 14px;");

                    // This Region acts as a spring, pushing the price label to the far right
                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    // Pack them into a horizontal row
                    HBox row = new HBox(nameLabel, spacer, priceLabel);
                    row.setAlignment(Pos.CENTER_LEFT);

                    setGraphic(row); // Show our custom layout
                    setText(null);   // Hide the default plain text
                }
            }
        };

        // Apply this beautiful new design to both lists!
        productList.setCellFactory(cellFactory);
        cartList.setCellFactory(cellFactory);
        // --------------------------------------

        Button addToCartBtn = new Button("Add to Cart");
        Button checkoutBtn = new Button("Checkout");
        Button removeFromCartBtn = new Button("Remove Selected");

        // 5. UI Components - Admin Dashboard (Moved up!)
        TextField nameInput = new TextField();
        nameInput.setPromptText("New Product Name");
        TextField priceInput = new TextField();
        priceInput.setPromptText("Price");
        TextField stockInput = new TextField();
        stockInput.setPromptText("Initial Stock");
        Button addProductBtn = new Button("Add to Inventory");

        // 6. Component Logic (All event handlers grouped together)

        // Search Logic
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                return product.getName().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        // Add to Cart Logic
        addToCartBtn.setOnAction(e -> {
            Product selected = productList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                cartItems.add(selected);
                totalAmount += selected.getPrice();
                totalLabel.setText(String.format("Total: $%.2f", totalAmount));
            }
        });

        // Remove from Cart Logic
        removeFromCartBtn.setOnAction(e -> {
            Product selectedInCart = cartList.getSelectionModel().getSelectedItem();
            if (selectedInCart != null) {
                cartItems.remove(selectedInCart);
                totalAmount -= selectedInCart.getPrice();
                totalLabel.setText(String.format("Total: $%.2f", totalAmount));
            }
        });

        // Checkout Logic
        checkoutBtn.setOnAction(e -> {
            if (cartItems.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Your cart is empty!").show();
                return;
            }

            // 1. Build the Receipt String BEFORE clearing the cart
            StringBuilder receipt = new StringBuilder();
            receipt.append("Thank you for your purchase!\n\n");
            receipt.append("Items Bought:\n");

            int itemCount = 0;
            for (Product p : cartItems) {
                // Add each item to the receipt text
                receipt.append("- ").append(p.getName()).append(String.format(" ($%.2f)\n", p.getPrice()));

                // Update the database
                productDAO.reduceStock(p.getId());
                itemCount++;
            }

            receipt.append("\nTotal Items: ").append(itemCount);
            receipt.append(String.format("\nTotal Paid: $%.2f", totalAmount));

            // 2. Fetch the new stock counts from PostgreSQL
            products.setAll(productDAO.getAllProducts());

            // 3. Reset the UI
            cartItems.clear();
            totalAmount = 0.0;
            totalLabel.setText("Total: $0.00");

            // 4. Display the Detailed Receipt
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Checkout Successful");
            alert.setHeaderText("Order Confirmation");
            alert.setContentText(receipt.toString());
            alert.showAndWait();
        });

        // Admin Add Product Logic (Moved up!)
        addProductBtn.setOnAction(e -> {
            try {
                String name = nameInput.getText();
                double price = Double.parseDouble(priceInput.getText());
                int stock = Integer.parseInt(stockInput.getText());

                productDAO.addProduct(name, price, stock);
                products.setAll(productDAO.getAllProducts());

                nameInput.clear();
                priceInput.clear();
                stockInput.clear();
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Please enter valid numbers!").show();
            }
        });

        // 7. Layout Organization (Updated for 1080x720)
        VBox root = new VBox(20); // Increased spacing between major sections
        root.setPadding(new Insets(30)); // Wider margins around the edges

        // Tell the ListViews to stretch vertically to fill the screen
        VBox.setVgrow(productList, Priority.ALWAYS);
        VBox.setVgrow(cartList, Priority.ALWAYS);

        VBox productBox = new VBox(10, new Label("Available Products"), searchField, productList);
        VBox cartBox = new VBox(10, new Label("Your Cart"), cartList);

        // Tell the Product and Cart columns to share the 1080 width equally
        HBox.setHgrow(productBox, Priority.ALWAYS);
        HBox.setHgrow(cartBox, Priority.ALWAYS);

        HBox lists = new HBox(30, productBox, cartBox);
        // Tell the lists container to push the bottom buttons down
        VBox.setVgrow(lists, Priority.ALWAYS);

        // Group the cart buttons horizontally for a cleaner look
        HBox actionButtons = new HBox(15, addToCartBtn, removeFromCartBtn, checkoutBtn);
        actionButtons.setAlignment(Pos.CENTER_LEFT);

        // Make the total label pop a bit more on the big screen
        totalLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #BB86FC;");

        // Redesign the Admin Box to be horizontal so it fits perfectly across the bottom
        HBox adminInputs = new HBox(15, nameInput, priceInput, stockInput, addProductBtn);
        VBox adminBox = new VBox(10, new Label("--- Admin Dashboard ---"), adminInputs);
        adminBox.setPadding(new Insets(20, 0, 0, 0)); // Extra padding above the admin section
        adminBox.setStyle("-fx-border-color: #333333; -fx-border-width: 1 0 0 0; -fx-padding: 20 0 0 0;"); // Top border divider

        // Add everything to the main root layout
        root.getChildren().addAll(lists, actionButtons, totalLabel, adminBox);

        // 8. Scene Setup
        Scene scene = new Scene(root, 1080, 720);

        // 8. Scene Setup
        // Increased height slightly to fit the new Admin tools comfortably
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setTitle("University Online Store");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}