package com.group4.chipgame.menu;

import com.group4.chipgame.Main;
import com.group4.chipgame.profile.ProfileManager;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ProfileMenu extends BaseMenu {

    private static final String MENU_STYLE = "-fx-background-color: linear-gradient(to bottom, #222, #333); -fx-padding: 30; -fx-spacing: 20;";
    private static final int PROFILES_PER_PAGE = 5;
    private static int currentPage = 0;

    private final ProfileManager profileManager;
    private final Main mainApp;
    private final Stage primaryStage;
    private VBox menuBox;

    public ProfileMenu(ProfileManager profileManager, Main mainApp, Stage primaryStage) {
        this.profileManager = profileManager;
        this.mainApp = mainApp;
        this.primaryStage = primaryStage;
    }

    public VBox createProfileMenu() {
        menuBox = createMenuBox(20, MENU_STYLE);
        updateProfileList();
        return menuBox;
    }

    private void updateProfileList() {
        menuBox.getChildren().clear();

        List<String> profileNames = profileManager.getProfileNames();
        int startIndex = currentPage * PROFILES_PER_PAGE;
        int endIndex = Math.min(startIndex + PROFILES_PER_PAGE, profileNames.size());

        for (int i = startIndex; i < endIndex; i++) {
            String profileName = profileNames.get(i);
            StackPane profileStack = new StackPane();
            Button profileButton = createButton(profileName, menuBox.widthProperty(), menuBox.heightProperty(), () -> {
                profileManager.setCurrentProfile(profileManager.getProfileByName(profileName).orElse(null));
                try {
                    profileManager.saveProfiles();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mainApp.showMainMenu(primaryStage);
            });
            Button deleteButton = new Button("X");
            deleteButton.setOnAction(e -> deleteProfile(profileName));
            StackPane.setAlignment(deleteButton, Pos.TOP_RIGHT);
            profileStack.getChildren().addAll(profileButton, deleteButton);
            menuBox.getChildren().add(profileStack);
        }

        Button newProfileButton = new Button("Create New Profile");
        newProfileButton.setOnAction(event -> createNewProfile());
        menuBox.getChildren().add(newProfileButton);

        addNavigationButtons(menuBox, profileNames.size());
        menuBox.getChildren().add(createBackButton(primaryStage, mainApp));
    }

    private void createNewProfile() {
        TextInputDialog dialog = new TextInputDialog("New Profile");
        dialog.setTitle("Create New Profile");
        dialog.setHeaderText("Create a New Profile");
        dialog.setContentText("Please enter a name for the new profile:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            try {
                profileManager.addProfile(name);
                profileManager.saveProfiles();
                profileManager.setCurrentProfile(profileManager.getProfileByName(name).orElse(null));
                refreshProfileMenu();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void deleteProfile(String profileName) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + profileName + "?", ButtonType.YES, ButtonType.NO);
        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    profileManager.removeProfile(profileName);
                    profileManager.saveProfiles();
                    refreshProfileMenu();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void refreshProfileMenu() {
        updateProfileList();
    }

    private void addNavigationButtons(VBox menuBox, int totalProfiles) {
        if (currentPage > 0) {
            addButton("Previous", menuBox, primaryStage, mainApp, () -> {
                currentPage--;
                refreshProfileMenu();
            });
        }

        if ((currentPage + 1) * PROFILES_PER_PAGE < totalProfiles) {
            addButton("Next", menuBox, primaryStage, mainApp, () -> {
                currentPage++;
                refreshProfileMenu();
            });
        }
    }
}
