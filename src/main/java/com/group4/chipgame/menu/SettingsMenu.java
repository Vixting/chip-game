package com.group4.chipgame.menu;

import com.group4.chipgame.Main;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SettingsMenu extends BaseMenu {

    public StackPane createSettingsMenu(Stage primaryStage, Main mainApp) {
        VBox settingsBox = createMenuBox(5, "-fx-background-color: rgba(102, 102, 102, 0.8);");

        addButton("Option 1", settingsBox, null, primaryStage, mainApp, () -> {});
        addButton("Option 2", settingsBox, null, primaryStage, mainApp, () -> {});

        StackPane settingsRootPane = createMenuRootPane(settingsBox);
        settingsRootPane.setMaxSize(400, 300);
        StackPane.setAlignment(settingsBox, javafx.geometry.Pos.CENTER);

        return settingsRootPane;
    }
}


