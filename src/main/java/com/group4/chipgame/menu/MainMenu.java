package com.group4.chipgame.menu;

import com.group4.chipgame.Main;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The MainMenu class represents the main menu of the game.
 * It provides buttons for various actions such as continuing the game, accessing levels, adjusting settings, and managing profiles.
 */
public class MainMenu extends BaseMenu {

    // Styling constants for the menu and buttons
    private static final String MENU_STYLE = "-fx-background-color: linear-gradient(to bottom, #222, #333); -fx-padding: 30; -fx-spacing: 20;";
    private static final String BUTTON_STYLE = "-fx-background-color: linear-gradient(to bottom, #444, #555); -fx-text-fill: #EEE; -fx-font-size: 20px; -fx-background-radius: 15;";
    private static final String BUTTON_HOVER_STYLE = "-fx-background-color: linear-gradient(to bottom, #666, #777);";
    private static final DropShadow BUTTON_SHADOW = new DropShadow(20, javafx.scene.paint.Color.GREY);

    /**
     * Creates the main menu UI components and returns a StackPane containing the menu.
     *
     * @param primaryStage The primary stage of the application.
     * @param mainApp      The main application instance.
     * @return A StackPane containing the main menu.
     */
    public StackPane createMainMenu(Stage primaryStage, Main mainApp) {
        // Create the main menu VBox
        VBox menuBox = new VBox(20);
        menuBox.setStyle(MENU_STYLE);
        menuBox.setAlignment(Pos.CENTER);

        // Add buttons to the menu VBox
        menuBox.getChildren().addAll(
                createButton("Continue", () -> {}),
                createButton("Levels", () -> populateLevelMenu(menuBox, primaryStage, mainApp)),
                createButton("Settings", () -> {}),
                createButton("Profile", () -> {})
        );

        return createMenuRootPane(menuBox);
    }

    /**
     * Creates a styled button with a specified text and action.
     * The button has a hover effect and a scaling animation.
     *
     * @param text   The text to be displayed on the button.
     * @param action The action to be executed when the button is clicked.
     * @return A styled Button with the specified text and action.
     */
    private Button createButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setStyle(BUTTON_STYLE);
        button.setEffect(BUTTON_SHADOW);
        button.setOnAction(e -> action.run());

        // Add scaling animation on mouse hover
        ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
        st.setToX(1.1);
        st.setToY(1.1);

        // Set button styles and play animation on mouse enter
        button.setOnMouseEntered(e -> {
            button.setStyle(BUTTON_STYLE + BUTTON_HOVER_STYLE);
            st.playFromStart();
        });

        // Reset styles and stop animation on mouse exit
        button.setOnMouseExited(e -> {
            button.setStyle(BUTTON_STYLE);
            st.stop();
            button.setScaleX(1.0);
            button.setScaleY(1.0);
        });

        return button;
    }

    /**
     * Clears the menu and populates it with level-specific options.
     *
     * @param menuBox      The VBox representing the menu.
     * @param primaryStage The primary stage of the application.
     * @param mainApp      The main application instance.
     */
    private static void populateLevelMenu(VBox menuBox, Stage primaryStage, Main mainApp) {
        menuBox.getChildren().clear();
        LevelMenu.populateLevelMenu(menuBox, primaryStage, mainApp);
    }
}
