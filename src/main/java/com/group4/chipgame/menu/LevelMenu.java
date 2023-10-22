package com.group4.chipgame.menu;

import com.group4.chipgame.Main;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class LevelMenu extends BaseMenu {

    public static void populateLevelMenu(VBox menuBox, Stage primaryStage, Main mainApp) {
        try {
            Enumeration<URL> levelFiles = LevelMenu.class.getClassLoader().getResources("levels");
            while (levelFiles.hasMoreElements()) {
                URL levelURL = levelFiles.nextElement();
                File dir = new File(levelURL.getFile());
                File[] directoryListing = dir.listFiles();
                if (directoryListing != null) {
                    for (File child : directoryListing) {
                        addButtonForLevel(menuBox, child.getName(), primaryStage, mainApp);
                    }
                }
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
