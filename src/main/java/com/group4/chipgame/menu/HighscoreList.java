package com.group4.chipgame.menu;

import com.group4.chipgame.Main;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HighscoreList extends BaseMenu {
    private static final int SCORES_PER_PAGE = 5;
    private static final int TOTAL_SCORES = 10;
    private static int currentPage = 0;
    private static final String LEVEL_LABEL_STYLE = "-fx-text-fill: white; -fx-font-size: 16px;";
    private static final String LEVEL_BUTTON_STYLE = "-fx-background-color: linear-gradient(to bottom, #4e4e4e, #3c3c3c); -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 10;";
    public static void populateHighscoreList(VBox menuBox, Stage primaryStage, Main mainApp) {
        menuBox.getChildren().clear();
        List<Integer> highscores = generateRandomHighscores(TOTAL_SCORES);
        int startIndex = currentPage * SCORES_PER_PAGE;
        int endIndex = Math.min(startIndex + SCORES_PER_PAGE, TOTAL_SCORES);
        for (int i = startIndex; i < endIndex; i++) {
            Label highscoreLabel = new Label("Player " + (i + 1) + ": " + highscores.get(i));
            highscoreLabel.setStyle(LEVEL_LABEL_STYLE);
            menuBox.getChildren().add(highscoreLabel);
        }
        addNavigationButtons(menuBox, primaryStage, mainApp, TOTAL_SCORES);
        Button backButton = createBackButton(menuBox, primaryStage, mainApp);
        backButton.setStyle(LEVEL_BUTTON_STYLE);
        menuBox.getChildren().add(backButton);
    }
    private static List<Integer> generateRandomHighscores(int count) {
        List<Integer> highscores = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            highscores.add(random.nextInt(10000));
        }
        return highscores;
    }
    private static void addNavigationButtons(VBox menuBox, Stage primaryStage, Main mainApp, int totalScores) {
        if (currentPage > 0) {
            addButton("Previous", menuBox, primaryStage, mainApp, () -> {
                currentPage--;
                populateHighscoreList(menuBox, primaryStage, mainApp);
            });
        }
        if ((currentPage + 1) * SCORES_PER_PAGE < totalScores) {
            addButton("Next", menuBox, primaryStage, mainApp, () -> {
                currentPage++;
                populateHighscoreList(menuBox, primaryStage, mainApp);
            });
        }
    }
    protected static Button createBackButton(VBox menuBox, Stage primaryStage, Main mainApp) {
        Button backButton = new Button("Back");
        backButton.setStyle(LEVEL_BUTTON_STYLE);
        backButton.setOnAction(e -> populateHighscoreMenu(menuBox, primaryStage, mainApp));
        return backButton;
    }
    private static void populateHighscoreMenu(VBox menuBox, Stage primaryStage, Main mainApp) {
        menuBox.getChildren().clear();
        HighscoreMenu.populateHighscoreMenu(menuBox, primaryStage, mainApp);
    }
}