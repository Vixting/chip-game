package com.group4.chipgame.menu;

import com.group4.chipgame.Main;
import com.group4.chipgame.profile.Profile;
import com.group4.chipgame.profile.ProfileManager;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

/**
 * This class handles the creation and interaction of the Save/Load menu UI,
 * allowing the user to save their game to a new slot or load an existing one.
 */
public class SaveLoadMenu extends BaseMenu {
    private final Main mainApp;
    private final Stage primaryStage;
    private final ProfileManager profileManager;
    private VBox menuBox;
    private static final int MENU_SPACING = 10;

    /**
     * Constructor for the SaveLoadMenu.
     *
     * @param mainApp         The main application object.
     * @param primaryStage    The primary stage of the application.
     * @param profileManager  The profile manager handling profile operations.
     */
    public SaveLoadMenu(Main mainApp, Stage primaryStage, ProfileManager profileManager) {
        this.mainApp = mainApp;
        this.primaryStage = primaryStage;
        this.profileManager = profileManager;
    }

    /**
     * Creates the UI for the save/load menu.
     *
     * @return A VBox containing the save/load menu UI.
     */
    public VBox createSaveLoadMenu() {
        menuBox = createMenuBox(MENU_SPACING, "-fx-background-color: #333;");
        refreshSaveSlots();
        addButton("Save to New Slot", menuBox, primaryStage, mainApp, this::saveToNewSlot);
        menuBox.getChildren().add(createBackButton(primaryStage, mainApp));
        return menuBox;
    }

    /**
     * Refreshes the save slots displayed in the menu based on the current profile's saves.
     */
    private void refreshSaveSlots() {
        menuBox.getChildren().clear();
        Profile currentProfile = profileManager.getCurrentProfile();
        List<String> saveSlots = currentProfile.getSaveFilePaths();

        for (String saveSlotPath : saveSlots) {
            String saveName = Paths.get(saveSlotPath).getFileName().toString();
            addButton("Load: " + saveName, menuBox, primaryStage, mainApp, () -> {
                Alert confirmLoad = new Alert(AlertType.CONFIRMATION, "Load this game?");
                confirmLoad.showAndWait().ifPresent(response -> {
                    if (response.getButtonData().isDefaultButton()) {
                        try {
                            mainApp.loadGame(saveSlotPath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            });
        }
    }

    /**
     * Prompts the user for a new save slot name and saves the game to that slot.
     */
    private void saveToNewSlot() {
        TextInputDialog dialog = new TextInputDialog("Enter Slot Name");
        dialog.setTitle("Save Game");
        dialog.setHeaderText("Save to a New Slot");
        dialog.setContentText("Enter a name for the new save slot:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(slotName -> {
            try {
                mainApp.saveGame(slotName);
                refreshSaveSlots();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
