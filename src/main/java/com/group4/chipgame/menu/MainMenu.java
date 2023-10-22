package com.group4.chipgame.menu;

import com.group4.chipgame.Main;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu extends BaseMenu {

    public StackPane createMainMenu(Stage primaryStage, Main mainApp) {
        VBox menuBox = createMenuBox(10, "-fx-background-color: #333;");
        addButton("Continue", menuBox, null, primaryStage, mainApp, () -> {
        });
        addButton("Levels", menuBox, null, primaryStage, mainApp, () -> {
            menuBox.getChildren().clear();
            LevelMenu.populateLevelMenu(menuBox, primaryStage, mainApp);
        });        addButton("Settings", menuBox, null, primaryStage, mainApp, () -> {
        });
        addButton("Profile", menuBox, null, primaryStage, mainApp, () -> {
        });

        return createMenuRootPane(menuBox);
    }
}

