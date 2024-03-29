package com.group4.chipgame.menu;

import com.group4.chipgame.Main;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is responsible for creating
 * and displaying the level selection menu.
 * It allows users to scroll through
 * available levels and select one to play.
 * @author William Buckler
 */
public class LevelMenu extends BaseMenu {

    private static final int LEVELS_PER_PAGE = 5;
    private static int currentPage = 0;
    private static final String LEVEL_BUTTON_STYLE =
            "-fx-background-color: "
                    + "linear-gradient(to bottom, #4e4e4e, #3c3c3c);"
                    + " -fx-text-fill: white; -fx-font-size: 16px;"
                    + " -fx-background-radius: 10;";

    /**
     * Populates the provided VBox with buttons
     * corresponding to levels available for play.
     *
     * @param menuBox       The VBox container to add level buttons to.
     * @param primaryStage  The primary stage of the application.
     * @param mainApp       The main application class instance.
     */
    public static void populateLevelMenu(final VBox menuBox,
                                         final Stage primaryStage,
                                         final Main mainApp) {
        menuBox.getChildren().clear();
        try {
            List<File> allFiles = loadLevelFiles();

            int startIndex = currentPage
                    * LEVELS_PER_PAGE;
            int endIndex = Math.min(startIndex
                    + LEVELS_PER_PAGE,
                    allFiles.size());

            for (int i = startIndex; i < endIndex; i++) {
                File child = allFiles.get(i);
                String levelDisplayName =
                        formatLevelDisplayName(child.getName());
                addButtonForLevel(menuBox,
                        levelDisplayName,
                        child.getName(),
                        primaryStage,
                        mainApp);
            }

            addNavigationButtons(menuBox,
                    primaryStage,
                    mainApp,
                    allFiles.size());

            Button backButton = createBackButton(primaryStage, mainApp);
            backButton.setStyle(LEVEL_BUTTON_STYLE);
            menuBox.getChildren().add(backButton);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads level files from a specified directory path.
     */
    private static List<File> loadLevelFiles() throws IOException {
        String levelsPath = "src/main/java/levels";

        File dir = new File(levelsPath);
        List<File> allFiles = new ArrayList<>();

        if (dir.exists() && dir.isDirectory()) {
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                allFiles.addAll(Arrays.asList(directoryListing));
            }
        } else {
            throw new IOException(
                    "Levels directory not found or not a directory: "
                            + levelsPath);
        }

        return allFiles;
    }

    /**
     * Adds a button for the specified level to the menu.
     */
    private static void addButtonForLevel(
            final VBox menuBox,
            final String levelDisplayName,
            final String fileName,
            final Stage primaryStage,
            final Main mainApp) {
        Button levelButton =
                createButton(levelDisplayName,
                        menuBox.widthProperty(),
                        menuBox.heightProperty(), () -> {
            try {
                String filePath = "src/main/java/levels/" + fileName;
                mainApp.startLevel(filePath, primaryStage);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        levelButton.setStyle(LEVEL_BUTTON_STYLE);
        menuBox.getChildren().add(levelButton);
    }

    /**
     * Adds navigation buttons
     * (previous, next) for scrolling through the levels.
     */
    private static void addNavigationButtons(final VBox menuBox,
                                             final Stage primaryStage,
                                             final Main mainApp,
                                             final int totalLevels) {
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

    /**
     * Formats the display name for a level based on its file name.
     */
    private static String formatLevelDisplayName(final String fileName) {
        return "Start " + fileName.substring(0, fileName.lastIndexOf('.'));
    }
}
