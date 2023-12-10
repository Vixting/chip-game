package com.group4.chipgame.menu;

import com.group4.chipgame.Main;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SettingsMenu extends BaseMenu {

    private static final double WIDTH_MULTIPLIER = 0.4;
    private static final double HEIGHT_MULTIPLIER = 0.4;
    private static final int SPACING = 5;
    private static final String BACKGROUND_STYLE = "-fx-background-color: rgba(102, 102, 102, 2);";

    public StackPane createSettingsMenu(Stage primaryStage, Main mainApp) {
        VBox settingsBox = createMenuBox(SPACING, BACKGROUND_STYLE);
        populateSettingsBox(settingsBox, primaryStage, mainApp);

        StackPane settingsRootPane = createMenuRootPane(settingsBox);
        configureRootPaneSize(settingsRootPane, primaryStage);

        return settingsRootPane;
    }

    private void populateSettingsBox(VBox settingsBox, Stage primaryStage, Main mainApp) {
        addButton("Save/Load Game", settingsBox, primaryStage, mainApp, mainApp::showSaveLoadMenu);
        addButton("Return to Main Menu", settingsBox, primaryStage, mainApp, () -> mainApp.showMainMenu(primaryStage));
        addButton("Option 1", settingsBox, primaryStage, mainApp, () -> {});
    }

    private void configureRootPaneSize(StackPane rootPane, Stage primaryStage) {
        rootPane.maxWidthProperty().bind(primaryStage.widthProperty().multiply(WIDTH_MULTIPLIER));
        rootPane.maxHeightProperty().bind(primaryStage.heightProperty().multiply(HEIGHT_MULTIPLIER));
        StackPane.setAlignment(rootPane, Pos.CENTER);
    }
}
