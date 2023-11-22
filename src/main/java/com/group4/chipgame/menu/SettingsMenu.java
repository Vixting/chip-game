package com.group4.chipgame.menu;

import com.group4.chipgame.Main;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The SettingsMenu class represents a menu for displaying and handling game settings.
 * It extends the BaseMenu class and provides methods to create and customize the settings menu.
 */
public class SettingsMenu extends BaseMenu {

    /**
     * Creates the settings menu with specified options and styling.
     *
     * @param primaryStage The primary stage of the application.
     * @param mainApp      The main application instance.
     * @return A StackPane representing the root node of the settings menu.
     */
    public StackPane createSettingsMenu(Stage primaryStage, Main mainApp) {
        VBox settingsBox = createMenuBox(5, "-fx-background-color: rgba(102, 102, 102, 2);");

        addButton("Option 1", settingsBox, primaryStage, mainApp, () -> {});
        addButton("Option 2", settingsBox, primaryStage, mainApp, () -> {});
        addButton("Option 3", settingsBox, primaryStage, mainApp, () -> {});
        addButton("Option 4", settingsBox, primaryStage, mainApp, () -> {});

        StackPane settingsRootPane = createMenuRootPane(settingsBox);
        settingsRootPane.setMaxSize(400, 300);
        StackPane.setAlignment(settingsBox, javafx.geometry.Pos.CENTER);

        return settingsRootPane;
    }
}
