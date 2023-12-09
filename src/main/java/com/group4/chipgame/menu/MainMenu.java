package com.group4.chipgame.menu;

import com.group4.chipgame.Main;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainMenu extends BaseMenu {

    private static final String MENU_STYLE = "-fx-background-color: linear-gradient(to bottom, #222, #333); -fx-padding: 30; -fx-spacing: 20;";
    private static final String BUTTON_STYLE = "-fx-background-color: linear-gradient(to bottom, #444, #555); -fx-text-fill: #EEE; -fx-font-size: 20px; -fx-background-radius: 15;";
    private static final String BUTTON_HOVER_STYLE = "-fx-background-color: linear-gradient(to bottom, #666, #777);";
    private static final DropShadow BUTTON_SHADOW = new DropShadow(20, javafx.scene.paint.Color.GREY);

    public StackPane createMainMenu(Stage primaryStage, Main mainApp) {
        VBox menuBox = new VBox(20);
        menuBox.setStyle(MENU_STYLE);
        menuBox.setAlignment(Pos.CENTER);

        menuBox.getChildren().addAll(
                createButton("Load Game", () -> openSaveLoadMenu(primaryStage, mainApp)),
                createButton("Levels", () -> populateLevelMenu(menuBox, primaryStage, mainApp)),
                createButton("Profiles", () -> openProfileMenu(primaryStage, mainApp)),
                createButton("High Scores", () -> openHighScoreMenu(primaryStage, mainApp))
        );

        return createMenuRootPane(menuBox);
    }

    private void openProfileMenu(Stage primaryStage, Main mainApp) {
        ProfileMenu profileMenu = new ProfileMenu(mainApp.getProfileManager(), mainApp, primaryStage);
        VBox profileMenuPane = profileMenu.createProfileMenu();
        primaryStage.getScene().setRoot(profileMenuPane);
    }

    private void openHighScoreMenu(Stage primaryStage, Main mainApp) {
        HighScoreMenu highScoreMenu = new HighScoreMenu(mainApp, primaryStage, mainApp.getProfileManager());
        VBox highScoreMenuPane = highScoreMenu.createHighScoreMenu();
        primaryStage.getScene().setRoot(highScoreMenuPane);
    }

    private void openSaveLoadMenu(Stage primaryStage, Main mainApp) {
        SaveLoadMenu saveLoadMenu = new SaveLoadMenu(mainApp, primaryStage, mainApp.getProfileManager());
        VBox saveLoadMenuPane = saveLoadMenu.createSaveLoadMenu();
        primaryStage.getScene().setRoot(saveLoadMenuPane);
    }
    private Button createButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setStyle(BUTTON_STYLE);
        button.setEffect(BUTTON_SHADOW);
        button.setOnAction(e -> action.run());

        ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
        st.setToX(1.1);
        st.setToY(1.1);

        button.setOnMouseEntered(e -> {
            button.setStyle(BUTTON_STYLE + BUTTON_HOVER_STYLE);
            st.playFromStart();
        });
        button.setOnMouseExited(e -> {
            button.setStyle(BUTTON_STYLE);
            st.stop();
            button.setScaleX(1.0);
            button.setScaleY(1.0);
        });

        return button;
    }

    private static void populateLevelMenu(VBox menuBox, Stage primaryStage, Main mainApp) {
        menuBox.getChildren().clear();
        LevelMenu.populateLevelMenu(menuBox, primaryStage, mainApp);
    }
}
