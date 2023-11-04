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

    public static void populateLevelMenu(VBox menuBox, Stage primaryStage, Main mainApp) {
        try {
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

            int startIndex = currentPage * LEVELS_PER_PAGE;
            int endIndex = Math.min(startIndex + LEVELS_PER_PAGE, allFiles.size());

            for (int i = startIndex; i < endIndex; i++) {
                File child = allFiles.get(i);
                addButtonForLevel(menuBox, child.getName(), primaryStage, mainApp);
            }

            if (currentPage > 0) {
                Button prevButton = createButton("Previous", menuBox.widthProperty(), menuBox.heightProperty());
                prevButton.setOnAction(e -> {
                    currentPage--;
                    menuBox.getChildren().clear();
                    populateLevelMenu(menuBox, primaryStage, mainApp);
                });
                menuBox.getChildren().add(prevButton);
            }

            if (endIndex < allFiles.size()) {
                Button nextButton = createButton("Next", menuBox.widthProperty(), menuBox.heightProperty());
                nextButton.setOnAction(e -> {
                    currentPage++;
                    menuBox.getChildren().clear();
                    populateLevelMenu(menuBox, primaryStage, mainApp);
                });
                menuBox.getChildren().add(nextButton);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addButtonForLevel(VBox menuBox, String fileName, Stage primaryStage, Main mainApp) {
        Button levelButton = createButton("Start " + fileName, menuBox.widthProperty(), menuBox.heightProperty());

        levelButton.setOnAction(e -> {
            try {
                mainApp.startLevel("/levels/" + fileName, primaryStage);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        menuBox.getChildren().add(levelButton);
    }

}
