package org.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class OnlineStoreGUI extends Application {

    private double totalAmount = 0.0;
    private Label totalLabel = new Label("Total: $0.00");
    private ObservableList<String> cartItems = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        // 1. Data Setup
        ObservableList<String> products = FXCollections.observableArrayList(
                "Laptop - $1200", "Smartphone - $800", "Headphones - $150", "Keyboard - $50"
        );

        // 2. UI Components
        ListView<String> productList = new ListView<>(products);
        ListView<String> cartList = new ListView<>(cartItems);
        Button addToCartBtn = new Button("Add to Cart");
        Button checkoutBtn = new Button("Checkout");

        // 3. Button Logic
        addToCartBtn.setOnAction(e -> {
            String selected = productList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                cartItems.add(selected);
                // Simple logic to extract price from string
                double price = Double.parseDouble(selected.split("\\$")[1]);
                totalAmount += price;
                totalLabel.setText(String.format("Total: $%.2f", totalAmount));
            }
        });

        checkoutBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Purchase Successful!");
            alert.showAndWait();
            cartItems.clear();
            totalAmount = 0.0;
            totalLabel.setText("Total: $0.00");
        });

        // 4. Layout
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        HBox lists = new HBox(20, new VBox(5, new Label("Available Products"), productList),
                new VBox(5, new Label("Your Cart"), cartList));

        root.getChildren().addAll(lists, addToCartBtn, totalLabel, checkoutBtn);

        // 5. Scene Setup
        Scene scene = new Scene(root, 500, 450);
        primaryStage.setTitle("Gristine Java Store");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}