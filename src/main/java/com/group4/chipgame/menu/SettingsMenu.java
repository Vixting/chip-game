package com.group4.chipgame.menu;

import com.group4.chipgame.Main;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SettingsMenu extends BaseMenu {

    public StackPane createSettingsMenu(Stage primaryStage, Main mainApp) {
        VBox settingsBox = createMenuBox(5, "-fx-background-color: rgba(102, 102, 102, 2);");

        addButton("Save/Load Game", settingsBox, primaryStage, mainApp, mainApp::showSaveLoadMenu);
        addButton("Return to Main Menu", settingsBox, primaryStage, mainApp, () -> mainApp.showMainMenu(primaryStage));
        addButton("Option 1", settingsBox, primaryStage, mainApp, () -> {});

        StackPane settingsRootPane = createMenuRootPane(settingsBox);
        settingsRootPane.maxWidthProperty().bind(primaryStage.widthProperty().multiply(0.4));
        settingsRootPane.maxHeightProperty().bind(primaryStage.heightProperty().multiply(0.4));
        StackPane.setAlignment(settingsBox, javafx.geometry.Pos.CENTER);

        return settingsRootPane;
    }
}
