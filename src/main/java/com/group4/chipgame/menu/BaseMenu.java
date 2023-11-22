package com.group4.chipgame.menu;

import com.group4.chipgame.Main;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public abstract class BaseMenu {

    private static final String BUTTON_STYLE = "-fx-background-color: linear-gradient(to bottom, #444, #555); -fx-text-fill: #EEE; -fx-font-size: 20px; -fx-background-radius: 15;";
    private static final String BUTTON_HOVER_STYLE = "-fx-background-color: linear-gradient(to bottom, #666, #777);";
    private static final DropShadow BUTTON_SHADOW = new DropShadow(20, javafx.scene.paint.Color.GREY);

    protected static void addButton(String label, VBox menuBox, Stage primaryStage, Main mainApp, Runnable action) {
        Button button = createButton(label, menuBox.widthProperty(), menuBox.heightProperty(), action);
        menuBox.getChildren().add(button);
    }

    protected static Button createButton(String label, javafx.beans.property.ReadOnlyDoubleProperty parentWidth, javafx.beans.property.ReadOnlyDoubleProperty parentHeight, Runnable action) {
        Button button = new Button(label);
        button.setStyle(BUTTON_STYLE);
        button.setEffect(BUTTON_SHADOW);

        button.prefWidthProperty().bind(parentWidth.multiply(0.6));
        button.prefHeightProperty().bind(parentHeight.multiply(0.15));

        button.setOnAction(e -> action.run());

        ScaleTransition stEnter = new ScaleTransition(Duration.millis(200), button);
        stEnter.setToX(1.1);
        stEnter.setToY(1.1);

        ScaleTransition stExit = new ScaleTransition(Duration.millis(200), button);
        stExit.setToX(1.0);
        stExit.setToY(1.0);

        button.setOnMouseEntered(e -> {
            button.setStyle(BUTTON_STYLE + BUTTON_HOVER_STYLE);
            stEnter.play();
        });
        button.setOnMouseExited(e -> {
            button.setStyle(BUTTON_STYLE);
            stExit.play();
        });

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

    protected static Button createBackButton(Stage primaryStage, Main mainApp) {
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #666; -fx-text-fill: white; -fx-font-size: 16px;");
        backButton.setOnAction(e -> mainApp.showMainMenu(primaryStage));
        return backButton;
    }
}
