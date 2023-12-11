package com.group4.chipgame.menu;

import com.group4.chipgame.Main;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This class is responsible for creating and managing the Settings menu UI,
 * providing options to the user for various settings.
 * @author William Buckley
 */
public class SettingsMenu extends BaseMenu {

    private static final double WIDTH_MULTIPLIER = 0.4;
    private static final double HEIGHT_MULTIPLIER = 0.4;
    private static final int SPACING = 5;
    private static final String BACKGROUND_STYLE =
            "-fx-background-color: rgba(102, 102, 102, 2);";
    private static final String LABEL_STYLE =
            "-fx-font-size: 20px; -fx-text-fill: #ffffff; -fx-font-weight: bold;";
    private static final int INSIDE_PADDING = 20;

    /**
     * Creates the settings menu UI as a StackPane.
     *
     * @param primaryStage The primary stage of the application.
     * @param mainApp      The main application object.
     * @return A StackPane containing the settings menu UI.
     */
    public StackPane createSettingsMenu(final Stage primaryStage, final Main mainApp) {
        VBox settingsBox = createMenuBox(SPACING, BACKGROUND_STYLE);
        settingsBox.setPadding(new Insets(
                INSIDE_PADDING, INSIDE_PADDING,
                INSIDE_PADDING, INSIDE_PADDING));
        populateSettingsBox(settingsBox, primaryStage, mainApp);

        StackPane settingsRootPane = createMenuRootPane(settingsBox);
        configureRootPaneSize(settingsRootPane, primaryStage);

        Label titleLabel = new Label("Settings");
        titleLabel.setStyle(LABEL_STYLE);
        settingsBox.getChildren().add(0, titleLabel);

        return settingsRootPane;
    }

    /**
     * Populates the provided VBox with buttons for different settings options.
     *
     * @param settingsBox   The VBox to populate with settings options.
     * @param primaryStage  The primary stage of the application.
     * @param mainApp       The main application object.
     */
    private void populateSettingsBox(final VBox settingsBox,
                                     final Stage primaryStage,
                                     final Main mainApp) {
        addButton("Save/Load Game",
                settingsBox,
                primaryStage,
                mainApp,
                mainApp::showSaveLoadMenu);
        addButton("Return to Main Menu",
                settingsBox,
                primaryStage,
                mainApp,
                () -> mainApp.showMainMenu(primaryStage));
        addButton("Option 1",
                settingsBox,
                primaryStage, mainApp, () -> { });
    }

    /**
     * Configures the size of the root
     * pane based on the primary stage dimensions.
     *
     * @param rootPane      The root StackPane to configure.
     * @param primaryStage  The primary stage of the application.
     */
    private void configureRootPaneSize(final StackPane rootPane, final Stage primaryStage) {
        rootPane.maxWidthProperty().
                bind(primaryStage.widthProperty().
                        multiply(WIDTH_MULTIPLIER));
        rootPane.maxHeightProperty().
                bind(primaryStage.heightProperty().
                        multiply(HEIGHT_MULTIPLIER));
        StackPane.setAlignment(rootPane, Pos.CENTER);
    }
}
