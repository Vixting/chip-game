package com.group4.chipgame.menu;

import com.group4.chipgame.Main;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class LevelCompleteMenu extends BaseMenu {

    private static final String CONGRATULATIONS_LABEL_STYLE = "-fx-text-fill: white; -fx-font-size: 20px;";
    private static final String HIGHSCORE_LABEL_STYLE = "-fx-text-fill: white; -fx-font-size: 16px;";
    private static final String LEVEL_BUTTON_STYLE = "-fx-background-color: linear-gradient(to bottom, #4e4e4e, #3c3c3c); -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 10;";

    public static void displayLevelCompleteMenu(VBox menuBox, Stage primaryStage, Main mainApp, int playerScore) {
        menuBox.getChildren().clear();

        Label congratulationsLabel = new Label("Congratulations!");
        congratulationsLabel.setStyle(CONGRATULATIONS_LABEL_STYLE);

        Label highscoreLabel = new Label("Your Highscore: " + playerScore);
        highscoreLabel.setStyle(HIGHSCORE_LABEL_STYLE);

        Button backButton = createBackButton(primaryStage, mainApp);
        backButton.setStyle(LEVEL_BUTTON_STYLE);

        menuBox.getChildren().addAll(congratulationsLabel, highscoreLabel, backButton);
    }
}
