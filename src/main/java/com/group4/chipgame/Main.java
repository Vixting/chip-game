package com.group4.chipgame;

import com.group4.chipgame.Level.LevelData;
import com.group4.chipgame.Level.LevelLoader;
import com.group4.chipgame.Level.LevelRenderer;
import com.group4.chipgame.Level.LevelStateManager;
import com.group4.chipgame.entities.actors.Player;
import com.group4.chipgame.events.LevelCompletedEvent;
import com.group4.chipgame.menu.MainMenu;
import com.group4.chipgame.menu.SaveLoadMenu;
import com.group4.chipgame.menu.SettingsMenu;
import com.group4.chipgame.profile.ProfileManager;
import com.group4.chipgame.entities.actors.tiles.Tile;
import com.group4.chipgame.ui.TimerUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Main application class for the Chip Game.
 * Handles initialization and switching between different game scenes.
 */
public class Main extends Application {
    public static final SimpleIntegerProperty TILE_SIZE = new SimpleIntegerProperty(50);
    public static final SimpleIntegerProperty ACTOR_SIZE = new SimpleIntegerProperty((int) (TILE_SIZE.get() / 1.5));
    public static final String MAIN_MENU_TITLE = "Chip Game Main Menu";
    public static final String GAME_TITLE = "Chip Game";
    public static final String BACKGROUND_COLOR = "-fx-background-color: black;";
    private static final int MIN_WINDOW_SIZE = 300;
    private static final String LEVELS_BASE_DIR = "src/main/java/levels";
    private static final String SAVES_DIR = "src/main/java/saves";
    private static final long INPUT_DELAY = 200;
    private static final int MAX_QUEUE_SIZE = 5;
    private static final double TILE_SIZE_DIMENSION_RATIO = 1.5;
    private static final int TILE_MAIN = 100;
    private static final int TILE_DIVIDE = 10;
    private static final double DEFAULT_SCENE_WIDTH = 400;
    private static final double DEFAULT_SCENE_HEIGHT = 400;
    private static final double SCENE_MIN_WIDTH = 400;
    private static final double SCENE_MIN_HEIGHT = 400;
    private static final int DEFAULT_TIMER = 300;
    private static final int INSECT = 10;

    private GameLoop gameLoop;
    private Stage primaryStage;
    private StackPane settingsMenu;
    private ProfileManager profileManager;
    private LevelData currentLevelData;
    private TimerUI timerUI;
    private String currentLevelPath = LEVELS_BASE_DIR + "/level1.json";


    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the application and initializes the main game window.
     * @param primaryStage The primary stage for this application.
     * @throws IOException if there is an error loading level data.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        profileManager = new ProfileManager();
        primaryStage.addEventHandler(LevelCompletedEvent.LEVEL_COMPLETED, event -> {
            System.out.println("Level completed!");
            profileManager.markLevelAsCompleted(currentLevelPath);
            handleLevelCompletion();
            advanceToNextLevel();
        });
        showMainMenu(primaryStage);
        addStageSizeListeners(primaryStage);
    }

    /**
     * Adds listeners to the stage's size properties to update game elements' sizes.
     * @param stage The primary stage of the application.
     */
    private void addStageSizeListeners(Stage stage) {
        stage.widthProperty().addListener((obs, oldVal, newVal) -> updateSizes(stage));
        stage.heightProperty().addListener((obs, oldVal, newVal) -> updateSizes(stage));
    }

    /**
     * Updates the sizes of tiles and actors based on the stage size.
     * @param stage The primary stage of the application.
     */
    private void updateSizes(Stage stage) {
        int newTileSize = calculateTileSize(stage);
        TILE_SIZE.set(newTileSize);
        ACTOR_SIZE.set((int) (newTileSize / TILE_SIZE_DIMENSION_RATIO));
    }

    /**
     * Displays the save/load game menu.
     */
    public void showSaveLoadMenu() {
        SaveLoadMenu saveLoadMenu = new SaveLoadMenu(this, primaryStage, profileManager);
        VBox saveLoadMenuPane = saveLoadMenu.createSaveLoadMenu();
        primaryStage.getScene().setRoot(saveLoadMenuPane);
    }

    /**
     * Calculates the tile size based on the current stage size.
     * @param stage The primary stage of the application.
     * @return Calculated tile size.
     */
    private int calculateTileSize(Stage stage) {
        return Math.min((int) stage.getWidth(), (int) stage.getHeight() - TILE_MAIN) / TILE_DIVIDE;
    }

    /**
     * Shows the main menu of the game.
     * @param primaryStage The primary stage of the application.
     */
    public void showMainMenu(Stage primaryStage) {
        MainMenu mainMenu = new MainMenu();
        StackPane menuPane = mainMenu.createMainMenu(primaryStage, this);
        settingsMenu = new SettingsMenu().createSettingsMenu(primaryStage, this);
        settingsMenu.setVisible(false);
        menuPane.getChildren().add(settingsMenu);
        Scene scene = new Scene(menuPane, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Gets the profile manager instance.
     * @return The profile manager.
     */
    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public void saveGame(String saveName) throws IOException {
        String profileName = profileManager.getCurrentProfile().getName();
        String saveFilePath = SAVES_DIR + "/" + profileName + "_" + saveName + ".json";
        System.out.println(saveFilePath);
        currentLevelData.setTimer(timerUI.getTimeRemaining());
        LevelStateManager.saveLevel(currentLevelData, saveFilePath);
        profileManager.addSaveToProfile(profileName, saveFilePath);
    }

    public void loadGame(String saveName) throws IOException {
        this.currentLevelData = LevelStateManager.loadLevel(saveName, new LevelRenderer(currentLevelData));
        this.currentLevelPath = currentLevelData.getLevelPath();

        adjustWindowSizeForLevel(currentLevelData);
        reinitializeLevel(currentLevelData);
    }


    private void handleLevelCompletion() {
        currentLevelData.getActors().stream()
                .filter(actor -> actor instanceof Player)
                .findFirst()
                .ifPresent(actor -> {
                    Player player = (Player) actor;
                    int score = player.getChipsCount() + timerUI.getTimeRemaining();
                    profileManager.markLevelAsCompleted(currentLevelPath, score);
                });
    }

    private void adjustWindowSizeForLevel(LevelData levelData) {
        double requiredWidth = levelData.gridWidth * TILE_SIZE.get();
        double requiredHeight = levelData.gridHeight * TILE_SIZE.get();

        primaryStage.setWidth(requiredWidth);
        primaryStage.setHeight(requiredHeight);
        primaryStage.centerOnScreen();
    }


    private void initScene(StackPane root, Stage stage, int width, int height, String title) {
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    private void reinitializeLevel(LevelData levelData) {
        LevelRenderer levelRenderer = levelData.levelRenderer;
        adjustPrimaryStage(primaryStage, levelData);

        levelRenderer.setCurrentLevelData(levelData);

        levelRenderer.renderTiles(levelData.getTiles());
        levelRenderer.renderActors(levelData.getActors());
        levelRenderer.renderCollectibles(levelData.getCollectibles());

        levelRenderer.updateTileOccupation();

        double sceneWidth = calculateSceneDimension(
                levelData.gridWidth * TILE_SIZE.get(),
                primaryStage.getWidth(),
                SCENE_MIN_WIDTH);

        double sceneHeight = calculateSceneDimension(
                levelData.gridHeight * TILE_SIZE.get(),
                primaryStage.getHeight(), SCENE_MIN_HEIGHT);

        StackPane gamePane = initGamePane(levelData);
        initScene(gamePane, primaryStage, (int) sceneWidth, (int) sceneHeight, GAME_TITLE);
    }

    public void startLevel(String levelPath, Stage primaryStage) throws IOException {
        this.currentLevelData = loadLevel(levelPath);
        initGamePane(currentLevelData);
        adjustPrimaryStage(primaryStage, currentLevelData);
    }

    private void adjustPrimaryStage(Stage primaryStage, LevelData levelData) {
        primaryStage.setMaximized(true);
        Platform.runLater(() -> adjustStageAndStartGame(primaryStage, levelData));
    }

    private void adjustStageAndStartGame(Stage primaryStage, LevelData levelData) {
        double stageWidth = primaryStage.getWidth();
        double stageHeight = primaryStage.getHeight();
        double sceneWidth = calculateSceneDimension(levelData.gridWidth
                * TILE_SIZE.get(), stageWidth, MIN_WINDOW_SIZE);
        double sceneHeight = calculateSceneDimension(levelData.gridHeight
                * TILE_SIZE.get(), stageHeight, MIN_WINDOW_SIZE);
        StackPane gamePane = initGamePane(levelData);
        initScene(gamePane, primaryStage, (int) sceneWidth, (int) sceneHeight, GAME_TITLE);
        initGameLoop(levelData);
        primaryStage.setMaximized(false);
    }

    private double calculateSceneDimension(double calculatedSize, double stageSize, double minSize) {
        return Math.max(Math.max(calculatedSize, stageSize), minSize);
    }

    private LevelData loadLevel(String levelPath) throws IOException {
        String content = Files.readString(Paths.get(levelPath));
        JSONObject levelJson = new JSONObject(content);
        int timer = levelJson.optInt("timer", DEFAULT_TIMER);

        LevelLoader levelLoader = new LevelLoader();
        Tile[][] tiles = levelLoader.loadTiles(levelPath);
        int gridWidth = tiles[0].length;
        int gridHeight = tiles.length;

        LevelRenderer levelRenderer = new LevelRenderer(currentLevelData);
        levelRenderer.renderTiles(tiles);
        this.currentLevelData = new LevelData(
                tiles,
                gridWidth,
                gridHeight,
                levelLoader.loadActors(levelPath, levelRenderer),
                levelLoader.loadCollectibles(levelPath, levelRenderer),
                levelRenderer,
                levelPath,
                timer);
        levelRenderer.setCurrentLevelData(currentLevelData);

        this.currentLevelPath = levelPath;

        return currentLevelData;
    }

    private StackPane initGamePane(LevelData levelData) {
        StackPane rootPane = new StackPane(levelData.levelRenderer.getGamePane());

        rootPane.getChildren().add(settingsMenu);
        rootPane.setStyle(BACKGROUND_COLOR);
        levelData.levelRenderer.renderActors(levelData.actors);
        levelData.levelRenderer.renderCollectibles(levelData.collectibles);

        this.timerUI = new TimerUI(levelData.levelRenderer.getGamePane(), levelData.getTimer());
        rootPane.getChildren().add(timerUI.getTimerLabel());
        StackPane.setAlignment(timerUI.getTimerLabel(), Pos.TOP_RIGHT);
        StackPane.setMargin(timerUI.getTimerLabel(), new Insets(INSECT, INSECT, 0, 0));
        return rootPane;
    }

    private void initGameLoop(LevelData levelData) {
        if (gameLoop != null) {
            gameLoop.stop();
        }

        Camera camera = new Camera(levelData.levelRenderer.getGamePane(),
                levelData.gridWidth * TILE_SIZE.get(),
                levelData.gridHeight * TILE_SIZE.get());

        gameLoop = new GameLoop(levelData.getActors(), levelData.levelRenderer, camera, timerUI);


        KeybindHandler movementHandler =
                new KeybindHandler(gameLoop, profileManager, this, INPUT_DELAY, MAX_QUEUE_SIZE);
        levelData.levelRenderer.getGamePane().getScene().setOnKeyPressed(event
                -> movementHandler.handleKeyPress(event.getCode()));

        gameLoop.start();
    }

    public void advanceToNextLevel() {
        String nextLevelPath = getNextLevelPath();
        if (nextLevelPath != null && !nextLevelPath.equals(currentLevelPath)) {
            try {
                currentLevelData = loadLevel(nextLevelPath);
                currentLevelPath = nextLevelPath;
                reinitializeLevel(currentLevelData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getNextLevelPath() {
        String currentLevel = currentLevelPath.substring(currentLevelPath.lastIndexOf('/') + 1);
        int currentLevelNumber = Integer.parseInt(currentLevel.replaceAll("[^0-9]", ""));
        String nextLevel = "/level" + (currentLevelNumber + 1) + ".json";
        Path nextLevelPath = Paths.get(LEVELS_BASE_DIR + nextLevel);
        if (Files.exists(nextLevelPath)) {
            return nextLevelPath.toString();
        } else {
            return null;
        }
    }

    public enum SceneType {
        MAIN_MENU, SETTINGS, GAME
    }

    public void switchScene(SceneType sceneType) {
        switch (sceneType) {
            case MAIN_MENU -> showMainMenu(primaryStage);
            case SETTINGS -> settingsMenu.setVisible(true);
            case GAME -> settingsMenu.setVisible(false);
            default -> throw new IllegalArgumentException("Invalid scene type: " + sceneType);
        }
    }

    void toggleSettingsMenu() {
        if (settingsMenu.isVisible()) {
            switchScene(SceneType.GAME);
        } else {
            switchScene(SceneType.SETTINGS);
        }
    }

}
