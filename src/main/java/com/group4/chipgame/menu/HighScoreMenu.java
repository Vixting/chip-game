package com.group4.chipgame.menu;

import com.group4.chipgame.Main;
import com.group4.chipgame.profile.ProfileManager;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * This class represents a high score menu for displaying player scores.
 * It extends the BaseMenu class, providing a UI to list the high scores
 * for different levels within the game.
 */
public class HighScoreMenu extends BaseMenu {
    private final Main mainApp;
    private final Stage primaryStage;
    private final ProfileManager profileManager;
    private static final String MENU_BACKGROUND_COLOR = "#333";
    private static final String SCROLLPANE_STYLE = "-fx-background: #222; -fx-border-color: #222;";
    private static final double MENU_SPACING = 10;
    private static final Insets SCROLLPANE_PADDING = new Insets(10);

    /**
     * Constructs a HighScoreMenu with references to the main application, the primary stage,
     * and the profile manager responsible for managing high scores.
     *
     * @param mainApp The main application class.
     * @param primaryStage The primary stage of the application.
     * @param profileManager The profile manager handling high scores.
     */
    public HighScoreMenu(Main mainApp, Stage primaryStage, ProfileManager profileManager) {
        this.mainApp = mainApp;
        this.primaryStage = primaryStage;
        this.profileManager = profileManager;
    }

    /**
     * Creates and returns a ScrollPane containing the high scores menu.
     * The menu lists all high scores per level, sorted by score value.
     *
     * @return A ScrollPane containing the formatted high scores display.
     */
    public ScrollPane createHighScoreMenu() {
        VBox menuBox = createMenuBox((int) MENU_SPACING, "-fx-background-color: " + MENU_BACKGROUND_COLOR + ";");
        Map<String, List<Map.Entry<String, Integer>>> highScores = profileManager.getHighScores();

        if (highScores.isEmpty()) {
            addLabel("No high scores available.", menuBox);
        } else {
            highScores.forEach((fullLevelPath, scoreEntries) -> {
                String levelName = extractLevelName(fullLevelPath);
                addLevelHeader("Level: " + levelName, menuBox);
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

        ScrollPane scrollPane = new ScrollPane(menuBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(SCROLLPANE_STYLE);
        scrollPane.setPadding(SCROLLPANE_PADDING);

        return scrollPane;
    }

    private String extractLevelName(String fullPath) {
        Path path = Paths.get(fullPath);
        return path.getFileName().toString();
    }

    /**
     * Adds a styled header label for the level name.
     *
     * @param text The text to display in the header.
     * @param menuBox The VBox to add the label to.
     */
    private void addLevelHeader(String text, VBox menuBox) {
        Label levelHeader = new Label(text);
        levelHeader.setStyle("-fx-font-size: 18px; -fx-text-fill: #FFF; -fx-font-weight: bold; -fx-background-color: #444; -fx-padding: 5;");
        menuBox.getChildren().add(levelHeader);
    }
}
