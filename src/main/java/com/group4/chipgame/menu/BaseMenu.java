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

/**
 * Represents a base class for creating menus in the ChipGame.
 * This class provides common functionalities for menus such as
 * creating buttons, labels, and handling their layout and animations.
 * @author William Buckley
 */

public abstract class BaseMenu {

    private static final String BUTTON_STYLE =
            "-fx-background-color: linear-gradient(to bottom, #444, #555);"
                    + " -fx-text-fill: #EEE;"
                    + " -fx-font-size: 20px;"
                    + " -fx-background-radius: 15;";
    private static final String BUTTON_HOVER_STYLE =
            "-fx-background-color:"
                    + " linear-gradient(to bottom, #666, #777);";
    private static final DropShadow BUTTON_SHADOW = new DropShadow(
            20, javafx.scene.paint.Color.GREY);
    private static final String LABEL_STYLE =
            "-fx-font-size: 16px;"
                    + " -fx-text-fill: #EEE;"
                    + " -fx-padding: 5;";
    private static final double BUTTON_PREF_WIDTH = 100;
    private static final double BUTTON_WIDTH_MULTIPLIER = 0.6;
    private static final double BUTTON_HEIGHT_MULTIPLIER = 0.15;
    private static final double SCALE_FACTOR = 1.1;
    private static final Duration ANIMATION_DURATION =
            Duration.millis(200);
    private static final double NORMAL_SCALE = 1.0;
    private static final String BACK_BUTTON_STYLE =
            "-fx-background-color: #666;"
                    + " -fx-text-fill: white;"
                    + " -fx-font-size: 16px;";
    private static final Insets MENU_BOX_PADDING =
            new Insets(30, 30, 30, 30);

    /**
     * Adds a button with a specified label to a VBox menu.
     * The button, when clicked, performs the given action.
     *
     * @param label        The text label for the button.
     * @param menuBox      The VBox to which the button will be added.
     * @param primaryStage The primary stage of the application.
     * @param mainApp      The main application instance.
     * @param action       The action to be performed when the button is clicked.
     */
    protected static void addButton(final String label,
                                    final VBox menuBox,
                                    final Stage primaryStage,
                                    final Main mainApp,
                                    final Runnable action) {
        Button button = createButton(label,
                menuBox.widthProperty(),
                menuBox.heightProperty(),
                action);
        menuBox.getChildren().add(button);
    }

    /**
     * Creates and returns a styled button with specified label and dimensions.
     * The button performs the given action when clicked.
     *
     * @param label        The label for the button.
     * @param parentWidth  The width property of the button's parent container.
     * @param parentHeight The height property of the button's parent container.
     * @param action       The action to be performed when the button is clicked.
     * @return             A styled Button object.
     */
    protected static Button createButton(final String label,
                                         final javafx.beans.property.
                                                 ReadOnlyDoubleProperty parentWidth,
                                         final javafx.beans.property.
                                                 ReadOnlyDoubleProperty parentHeight,
                                         final Runnable action) {
        Button button = new Button(label);
        button.setStyle(BUTTON_STYLE);
        button.setEffect(BUTTON_SHADOW);
        button.setPrefWidth(BUTTON_PREF_WIDTH);

        button.prefWidthProperty().bind(
                parentWidth.multiply(BUTTON_WIDTH_MULTIPLIER));
        button.prefHeightProperty().bind(
                parentHeight.multiply(BUTTON_HEIGHT_MULTIPLIER));

        button.setOnAction(e -> action.run());

        ScaleTransition stEnter = new ScaleTransition(
                ANIMATION_DURATION, button);
        stEnter.setToX(SCALE_FACTOR);
        stEnter.setToY(SCALE_FACTOR);

        ScaleTransition stExit = new ScaleTransition(
                ANIMATION_DURATION, button);
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

    /**
     * Creates and returns a styled label with the specified text.
     *
     * @param text The text to be displayed in the label.
     * @return     A styled Label object.
     */
    protected static Label createLabel(final String text) {
        Label label = new Label(text);
        label.setStyle(LABEL_STYLE);
        return label;
    }

    /**
     * Adds a label with specified text to a VBox menu.
     *
     * @param text    The text for the label.
     * @param menuBox The VBox to which the label will be added.
     */
    protected static void addLabel(final String text,
                                   final VBox menuBox) {
        Label label = createLabel(text);
        menuBox.getChildren().add(label);
    }

    /**
     * Creates and returns the root StackPane for the menu with the provided VBox.
     *
     * @param menuBox The VBox containing menu items to be added to the root pane.
     * @return        The StackPane serving as the root for the menu.
     */
    protected StackPane createMenuRootPane(final VBox menuBox) {
        StackPane rootPane = new StackPane();
        rootPane.getChildren().add(menuBox);
        StackPane.setAlignment(menuBox, Pos.CENTER);
        rootPane.setStyle("-fx-background-color: #f4f4f4;");
        return rootPane;
    }

    /**
     * Creates and returns a VBox styled as a menu box with specified spacing and style.
     *
     * @param spacing The spacing between elements in the VBox.
     * @param style   The CSS style string for the VBox.
     * @return        A styled VBox object.
     */
    protected VBox createMenuBox(final int spacing,
                                 final String style) {
        VBox menuBox = new VBox(spacing);
        menuBox.setStyle(style);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setPadding(MENU_BOX_PADDING);
        return menuBox;
    }

    /**
     * Creates and returns a 'Back' button that navigates to the main menu when clicked.
     *
     * @param primaryStage The primary stage of the application.
     * @param mainApp      The main application instance.
     * @return             A styled Button object for navigating back to the main menu.
     */
    protected static Button createBackButton(final Stage primaryStage,
                                             final Main mainApp) {
        Button backButton = new Button("Back");
        backButton.setStyle(BACK_BUTTON_STYLE);
        backButton.setOnAction(e -> mainApp.showMainMenu(primaryStage));
        return backButton;
    }
}
