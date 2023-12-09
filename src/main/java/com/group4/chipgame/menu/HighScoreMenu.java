package com.group4.chipgame.menu;

import com.group4.chipgame.Main;
import com.group4.chipgame.profile.ProfileManager;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class HighScoreMenu extends BaseMenu {
    private final Main mainApp;
    private final Stage primaryStage;
    private final ProfileManager profileManager;

    public HighScoreMenu(Main mainApp, Stage primaryStage, ProfileManager profileManager) {
        this.mainApp = mainApp;
        this.primaryStage = primaryStage;
        this.profileManager = profileManager;
    }

    public VBox createHighScoreMenu() {
        VBox menuBox = createMenuBox(10, "-fx-background-color: #333;");
        Map<String, List<Map.Entry<String, Integer>>> highScores = profileManager.getHighScores();

        if (highScores.isEmpty()) {
            addLabel("No high scores available.", menuBox);
        } else {
            highScores.forEach((levelName, scoreEntries) -> {
                addLabel("Level: " + levelName, menuBox);
                menuBox.getChildren().add(new Separator());

                scoreEntries.forEach(entry -> {
                    String profileName = entry.getKey();
                    Integer score = entry.getValue();
                    addLabel(profileName + ": Score - " + score, menuBox);
                });

                menuBox.getChildren().add(new Separator());
            });
        }

        menuBox.getChildren().add(createBackButton(primaryStage, mainApp));
        return menuBox;
    }


}
