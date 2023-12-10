package com.group4.chipgame.menu;

import com.group4.chipgame.Main;
import com.group4.chipgame.profile.ProfileManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * This class is responsible for displaying and managing the profile menu.
 * It allows users to create, select, and delete profiles.
 */
public class ProfileMenu extends BaseMenu {

    private static final String MENU_STYLE = "-fx-background-color: linear-gradient(to bottom, #222, #333);";
    private static final String BUTTON_DELETE_STYLE = "-fx-background-color: #FF6347; -fx-text-fill: white;";
    private static final String BUTTON_CREATE_STYLE = "-fx-background-color: #2E8B57; -fx-text-fill: white;"; // Slightly darker green
    private static final int PROFILES_PER_PAGE = 5;
    private static final int MENU_SPACING = 20;
    private static final Insets STACK_PANE_INSETS = new Insets(5);
    private static int currentPage = 0;

    private final ProfileManager profileManager;
    private final Main mainApp;
    private final Stage primaryStage;
    private VBox menuBox;

    /**
     * Constructor for ProfileMenu.
     *
     * @param profileManager The profile manager to handle profile operations.
     * @param mainApp        The main application instance.
     * @param primaryStage   The primary stage of the application.
     */
    public ProfileMenu(ProfileManager profileManager, Main mainApp, Stage primaryStage) {
        this.profileManager = profileManager;
        this.mainApp = mainApp;
        this.primaryStage = primaryStage;
    }

    /**
     * Creates and returns a ScrollPane containing the profile menu UI.
     *
     * @return A ScrollPane containing the profile menu.
     */
    public ScrollPane createProfileMenu() {
        menuBox = createMenuBox(MENU_SPACING, MENU_STYLE);
        updateProfileList();

        ScrollPane scrollPane = new ScrollPane(menuBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #000;");


        return scrollPane;
    }

    /**
     * Updates the list of profiles displayed in the menu.
     */
    private void updateProfileList() {
        menuBox.getChildren().clear();

        List<String> profileNames = profileManager.getProfileNames();
        int startIndex = currentPage * PROFILES_PER_PAGE;
        int endIndex = Math.min(startIndex + PROFILES_PER_PAGE, profileNames.size());

        for (int i = startIndex; i < endIndex; i++) {
            String profileName = profileNames.get(i);
            StackPane profileStack = new StackPane();
            profileStack.setPadding(STACK_PANE_INSETS);
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
            deleteButton.setStyle(BUTTON_DELETE_STYLE);
            deleteButton.setOnAction(e -> deleteProfile(profileName));
            StackPane.setAlignment(deleteButton, Pos.TOP_RIGHT);
            profileStack.getChildren().addAll(profileButton, deleteButton);
            menuBox.getChildren().add(profileStack);
        }

        Button newProfileButton = new Button("Create New Profile");
        newProfileButton.setStyle(BUTTON_CREATE_STYLE);
        newProfileButton.setOnAction(event -> createNewProfile());
        menuBox.getChildren().add(newProfileButton);

        addNavigationButtons(menuBox, profileNames.size());
        menuBox.getChildren().add(createBackButton(primaryStage, mainApp));
    }

    /**
     * Creates a new profile using a TextInputDialog for user input.
     */
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

    /**
     * Deletes a profile after confirmation from the user.
     *
     * @param profileName The name of the profile to delete.
     */
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

    /**
     * Refreshes the profile menu to update the list of profiles.
     */
    private void refreshProfileMenu() {
        updateProfileList();
    }

    /**
     * Adds navigation buttons to the menu for scrolling through profiles.
     *
     * @param menuBox       The VBox container for the buttons.
     * @param totalProfiles The total number of profiles available.
     */
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
