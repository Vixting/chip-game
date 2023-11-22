package com.group4.chipgame.menu;

import com.group4.chipgame.Main;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class LevelMenu extends BaseMenu {

    private static final int LEVELS_PER_PAGE = 5;
    private static int currentPage = 0;
    private static final String LEVEL_BUTTON_STYLE = "-fx-background-color: linear-gradient(to bottom, #4e4e4e, #3c3c3c); -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 10;";

    public static void populateLevelMenu(VBox menuBox, Stage primaryStage, Main mainApp) {
        menuBox.getChildren().clear();
        try {
            List<File> allFiles = loadLevelFiles();

            int startIndex = currentPage * LEVELS_PER_PAGE;
            int endIndex = Math.min(startIndex + LEVELS_PER_PAGE, allFiles.size());

            for (int i = startIndex; i < endIndex; i++) {
                File child = allFiles.get(i);
                String levelDisplayName = formatLevelDisplayName(child.getName());
                addButtonForLevel(menuBox, levelDisplayName, child.getName(), primaryStage, mainApp);
            }

            addNavigationButtons(menuBox, primaryStage, mainApp, allFiles.size());

            Button backButton = createBackButton(primaryStage, mainApp);
            backButton.setStyle(LEVEL_BUTTON_STYLE);
            menuBox.getChildren().add(backButton);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<File> loadLevelFiles() throws IOException {
        Enumeration<URL> levelFiles = LevelMenu.class.getClassLoader().getResources("levels");
        List<File> allFiles = new ArrayList<>();
        while (levelFiles.hasMoreElements()) {
            URL levelURL = levelFiles.nextElement();
            File dir = new File(levelURL.getFile());
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                allFiles.addAll(Arrays.asList(directoryListing));
            }
        }
        return allFiles;
    }

    private static void addButtonForLevel(VBox menuBox, String levelDisplayName, String fileName, Stage primaryStage, Main mainApp) {
        Button levelButton = createButton(levelDisplayName, menuBox.widthProperty(), menuBox.heightProperty(), () -> {
            try {
                mainApp.startLevel("/levels/" + fileName, primaryStage);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        levelButton.setStyle(LEVEL_BUTTON_STYLE);
        menuBox.getChildren().add(levelButton);
    }

    private static void addNavigationButtons(VBox menuBox, Stage primaryStage, Main mainApp, int totalLevels) {
        if (currentPage > 0) {
            addButton("Previous", menuBox, primaryStage, mainApp, () -> {
                currentPage--;
                populateLevelMenu(menuBox, primaryStage, mainApp);
            });
        }

        if ((currentPage + 1) * LEVELS_PER_PAGE < totalLevels) {
            addButton("Next", menuBox, primaryStage, mainApp, () -> {
                currentPage++;
                populateLevelMenu(menuBox, primaryStage, mainApp);
            });
        }
    }

    private static String formatLevelDisplayName(String fileName) {
        return "Start " + fileName.substring(0, fileName.lastIndexOf('.'));
    }
}
