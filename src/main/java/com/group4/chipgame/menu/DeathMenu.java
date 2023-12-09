package com.group4.chipgame.menu;

import com.group4.chipgame.Main;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class DeathMenu extends BaseMenu {

    private static final String SORRY_LABEL_STYLE = "-fx-text-fill: white; -fx-font-size: 20px;";
    private static final String SCORE_LABEL_STYLE = "-fx-text-fill: white; -fx-font-size: 16px;";
    private static final String BACK_BUTTON_STYLE = "-fx-background-color: linear-gradient(to bottom, #4e4e4e, #3c3c3c); -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 10;";

    public static void displayDeathMenu(VBox menuBox, Stage primaryStage, Main mainApp, int playerScore) {
        menuBox.getChildren().clear();

        Label sorryLabel = new Label("Sorry, you're dead!");
        sorryLabel.setStyle(SORRY_LABEL_STYLE);

        Label scoreLabel = new Label("Your Score: " + playerScore);
        scoreLabel.setStyle(SCORE_LABEL_STYLE);

        Button backButton = createBackButton(primaryStage, mainApp);
        backButton.setStyle(BACK_BUTTON_STYLE);

        menuBox.getChildren().addAll(sorryLabel, scoreLabel, backButton);
    }
}