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

/**
 * The BaseMenu class serves as a base class for other menu classes.
 * It provides common functionality for creating buttons, styling, and menu layout.
 */
public abstract class BaseMenu {

    // Button styling constants
    private static final String BUTTON_STYLE = "-fx-background-color: linear-gradient(to bottom, #444, #555); -fx-text-fill: #EEE; -fx-font-size: 20px; -fx-background-radius: 15;";
    private static final String BUTTON_HOVER_STYLE = "-fx-background-color: linear-gradient(to bottom, #666, #777);";
    private static final DropShadow BUTTON_SHADOW = new DropShadow(20, javafx.scene.paint.Color.GREY);

    /**
     * Adds a button to the menu.
     *
     * @param label        The label of the button.
     * @param menuBox      The VBox representing the menu.
     * @param primaryStage The primary stage of the application.
     * @param mainApp      The main application instance.
     * @param action       The action to be executed when the button is clicked.
     */
    protected static void addButton(String label, VBox menuBox, Stage primaryStage, Main mainApp, Runnable action) {
        Button button = createButton(label, menuBox.widthProperty(), menuBox.heightProperty(), action);
        menuBox.getChildren().add(button);
    }

    /**
     * Creates a styled button with hover effects.
     *
     * @param label        The label of the button.
     * @param parentWidth  The width property of the parent container.
     * @param parentHeight The height property of the parent container.
     * @param action       The action to be executed when the button is clicked.
     * @return The created button.
     */
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

    /**
     * Creates a StackPane to serve as the root container for the menu.
     *
     * @param menuBox The VBox representing the menu.
     * @return The created StackPane.
     */
    protected StackPane createMenuRootPane(VBox menuBox) {
        StackPane rootPane = new StackPane();
        rootPane.getChildren().add(menuBox);
        StackPane.setAlignment(menuBox, Pos.CENTER);
        rootPane.setStyle("-fx-background-color: #f4f4f4;");
        return rootPane;
    }

    /**
     * Creates a VBox with specified spacing and style for the menu.
     *
     * @param spacing The vertical spacing between menu items.
     * @param style   The style to be applied to the menu.
     * @return The created VBox.
     */
    protected VBox createMenuBox(int spacing, String style) {
        VBox menuBox = new VBox(spacing);
        menuBox.setStyle(style);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setPadding(new Insets(30, 30, 30, 30));
        return menuBox;
    }

    /**
     * Creates a "Back" button with a specific style.
     *
     * @param primaryStage The primary stage of the application.
     * @param mainApp      The main application instance.
     * @return The created "Back" button.
     */
    protected static Button createBackButton(Stage primaryStage, Main mainApp) {
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #666; -fx-text-fill: white; -fx-font-size: 16px;");
        backButton.setOnAction(e -> mainApp.showMainMenu(primaryStage));
        return backButton;
    }
}
