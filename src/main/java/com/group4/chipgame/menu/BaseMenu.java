package com.group4.chipgame.menu;

import com.group4.chipgame.Main;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public abstract class BaseMenu {

    private static final String BUTTON_STYLE = "-fx-background-color: linear-gradient(to bottom, #444, #555); -fx-text-fill: #EEE; -fx-font-size: 20px; -fx-background-radius: 15;";
    private static final String BUTTON_HOVER_STYLE = "-fx-background-color: linear-gradient(to bottom, #666, #777);";
    private static final DropShadow BUTTON_SHADOW = new DropShadow(20, javafx.scene.paint.Color.GREY);
    private static final String LABEL_STYLE = "-fx-font-size: 16px; -fx-text-fill: #EEE; -fx-padding: 5;";
    private static final double BUTTON_PREF_WIDTH = 100;
    private static final double BUTTON_WIDTH_MULTIPLIER = 0.6;
    private static final double BUTTON_HEIGHT_MULTIPLIER = 0.15;
    private static final double SCALE_FACTOR = 1.1;
    private static final Duration ANIMATION_DURATION = Duration.millis(200);
    private static final double NORMAL_SCALE = 1.0;
    private static final String BACK_BUTTON_STYLE = "-fx-background-color: #666; -fx-text-fill: white; -fx-font-size: 16px;";
    private static final Insets MENU_BOX_PADDING = new Insets(30, 30, 30, 30);

    protected static void addButton(String label, VBox menuBox, Stage primaryStage, Main mainApp, Runnable action) {
        Button button = createButton(label, menuBox.widthProperty(), menuBox.heightProperty(), action);
        menuBox.getChildren().add(button);
    }

    protected static Button createButton(String label, javafx.beans.property.ReadOnlyDoubleProperty parentWidth, javafx.beans.property.ReadOnlyDoubleProperty parentHeight, Runnable action) {
        Button button = new Button(label);
        button.setStyle(BUTTON_STYLE);
        button.setEffect(BUTTON_SHADOW);
        button.setPrefWidth(BUTTON_PREF_WIDTH);

        button.prefWidthProperty().bind(parentWidth.multiply(BUTTON_WIDTH_MULTIPLIER));
        button.prefHeightProperty().bind(parentHeight.multiply(BUTTON_HEIGHT_MULTIPLIER));

        button.setOnAction(e -> action.run());

        ScaleTransition stEnter = new ScaleTransition(ANIMATION_DURATION, button);
        stEnter.setToX(SCALE_FACTOR);
        stEnter.setToY(SCALE_FACTOR);

        ScaleTransition stExit = new ScaleTransition(ANIMATION_DURATION, button);
        stExit.setToX(NORMAL_SCALE);
        stExit.setToY(NORMAL_SCALE);

        button.setOnMouseEntered(e -> {
            button.setStyle(BUTTON_STYLE + BUTTON_HOVER_STYLE);
            stEnter.play();
        });
        button.setOnMouseExited(e -> {
            button.setStyle(BUTTON_STYLE);
            stExit.play();
        });

        stExit.setOnFinished(e -> {
            button.setScaleX(NORMAL_SCALE);
            button.setScaleY(NORMAL_SCALE);
        });

        return button;
    }

    protected static Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle(LABEL_STYLE);
        return label;
    }

    protected static void addLabel(String text, VBox menuBox) {
        Label label = createLabel(text);
        menuBox.getChildren().add(label);
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
        menuBox.setPadding(MENU_BOX_PADDING);
        return menuBox;
    }

    protected static Button createBackButton(Stage primaryStage, Main mainApp) {
        Button backButton = new Button("Back");
        backButton.setStyle(BACK_BUTTON_STYLE);
        backButton.setOnAction(e -> mainApp.showMainMenu(primaryStage));
        return backButton;
    }
}
