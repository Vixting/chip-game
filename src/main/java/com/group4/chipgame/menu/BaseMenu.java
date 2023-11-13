package com.group4.chipgame.menu;

import com.group4.chipgame.Main;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public abstract class BaseMenu {

    protected void addButton(String label, VBox menuBox, Stage primaryStage, Main mainApp, Runnable action) {
        Button button = createButton(label, menuBox.widthProperty(), menuBox.heightProperty());
        button.setOnAction(e -> action.run());
        menuBox.getChildren().add(button);
    }

    protected static Button createButton(String label, javafx.beans.property.ReadOnlyDoubleProperty parentWidth, javafx.beans.property.ReadOnlyDoubleProperty parentHeight) {
        Button button = new Button(label);
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 12; -fx-background-radius: 12;");
        button.setPadding(new Insets(15, 30, 15, 30));

        button.prefWidthProperty().bind(parentWidth.multiply(0.6));
        button.prefHeightProperty().bind(parentHeight.multiply(0.15));

        button.styleProperty().bind(
                button.heightProperty().asString()
                        .concat("px; -fx-background-color: #4CAF50; -fx-text-fill: white;")
                        .concat("-fx-font-size: ")
                        .concat(button.heightProperty().divide(3).asString())
                        .concat("px;")
        );

        return button;
    }

    protected StackPane createMenuRootPane(VBox menuBox) {
        StackPane rootPane = new StackPane();
        rootPane.getChildren().add(menuBox);
        StackPane.setAlignment(menuBox, Pos.CENTER);
        rootPane.setStyle("-fx-background-color: #f4f4f4;");
        return rootPane;
    }

    protected VBox createMenuBox(int spacing, String style) {
        VBox menuBox = new VBox(spacing);
        menuBox.setStyle(style);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setPadding(new Insets(30, 30, 30, 30));
        return menuBox;
    }
}
