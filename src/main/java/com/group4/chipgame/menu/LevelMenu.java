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

/**
 * The LevelMenu class represents the menu for selecting game levels.
 * It dynamically loads available level files and displays buttons for each level.
 */
public class LevelMenu extends BaseMenu {

    // Constants for controlling the number of levels displayed per page
    private static final int LEVELS_PER_PAGE = 5;

    // Counter to keep track of the current page
    private static int currentPage = 0;

    // Style for level buttons
    private static final String LEVEL_BUTTON_STYLE = "-fx-background-color: linear-gradient(to bottom, #4e4e4e, #3c3c3c); -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 10;";

    /**
     * Populates the level menu with buttons for each available level.
     *
     * @param menuBox      The VBox representing the menu.
     * @param primaryStage The primary stage of the application.
     * @param mainApp      The main application instance.
     */
    public static void populateLevelMenu(VBox menuBox, Stage primaryStage, Main mainApp) {
        menuBox.getChildren().clear();
        try {
            List<File> allFiles = loadLevelFiles();

            int startIndex = currentPage * LEVELS_PER_PAGE;
            int endIndex = Math.min(startIndex + LEVELS_PER_PAGE, allFiles.size());

            // Add buttons for each level within the current page range
            for (int i = startIndex; i < endIndex; i++) {
                File child = allFiles.get(i);
                String levelDisplayName = formatLevelDisplayName(child.getName());
                addButtonForLevel(menuBox, levelDisplayName, child.getName(), primaryStage, mainApp);
            }

            // Add navigation buttons and back button
            addNavigationButtons(menuBox, primaryStage, mainApp, allFiles.size());
            Button backButton = createBackButton(primaryStage, mainApp);
            backButton.setStyle(LEVEL_BUTTON_STYLE);
            menuBox.getChildren().add(backButton);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the level files from the "levels" directory.
     *
     * @return A list of File objects representing the available level files.
     * @throws IOException If an I/O error occurs while loading the level files.
     */
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

    /**
     * Creates a button for a specific level and adds it to the menu.
     *
     * @param menuBox          The VBox representing the menu.
     * @param levelDisplayName The display name of the level.
     * @param fileName         The name of the level file.
     * @param primaryStage     The primary stage of the application.
     * @param mainApp          The main application instance.
     */
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

    /**
     * Adds navigation buttons (Previous and Next) to the menu.
     *
     * @param menuBox      The VBox representing the menu.
     * @param primaryStage The primary stage of the application.
     * @param mainApp      The main application instance.
     * @param totalLevels  The total number of available levels.
     */
    private static void addNavigationButtons(VBox menuBox, Stage primaryStage, Main mainApp, int totalLevels) {
        // Add Previous button if not on the first page
        if (currentPage > 0) {
            addButton("Previous", menuBox, primaryStage, mainApp, () -> {
                currentPage--;
                populateLevelMenu(menuBox, primaryStage, mainApp);
            });
        }

        // Add Next button if there are more levels beyond the current page
        if ((currentPage + 1) * LEVELS_PER_PAGE < totalLevels) {
            addButton("Next", menuBox, primaryStage, mainApp, () -> {
                currentPage++;
                populateLevelMenu(menuBox, primaryStage, mainApp);
            });
        }
    }

    /**
     * Formats the level file name into a display name.
     *
     * @param fileName The name of the level file.
     * @return The formatted display name for the level.
     */
    private static String formatLevelDisplayName(String fileName) {
        return "Start " + fileName.substring(0, fileName.lastIndexOf('.'));
    }
}
