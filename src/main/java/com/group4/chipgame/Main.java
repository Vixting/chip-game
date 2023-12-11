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
 * @author William Buckley
 */
public class Main extends Application {
    public static final SimpleIntegerProperty TILE_SIZE =
            new SimpleIntegerProperty(50);
    public static final SimpleIntegerProperty ACTOR_SIZE =
            new SimpleIntegerProperty((int) (TILE_SIZE.get() / 1.5));
    public static final String MAIN_MENU_TITLE = "Chip Game Main Menu";
    public static final String GAME_TITLE = "Chip Game";
    public static final String BACKGROUND_COLOR =
            "-fx-background-color: black;";
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


    public static void main(final String[] args) {
        launch(args);
    }

    /**
     * Starts the application and initializes the main game window.
     * @param primaryStage The primary stage for this application.
     * @throws IOException if there is an error loading level data.
     */
    @Override
    public void start(final Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        profileManager = new ProfileManager();
        primaryStage.addEventHandler(LevelCompletedEvent.
                LEVEL_COMPLETED, event -> {
            profileManager.markLevelAsCompleted(currentLevelPath);
            handleLevelCompletion();
            advanceToNextLevel();
        });
        showMainMenu(primaryStage);
        addStageSizeListeners(primaryStage);
    }

    /**
     * Adds listeners to the stage's size
     * properties to update game elements' sizes.
     * @param stage The primary stage of the application.
     */
    private void addStageSizeListeners(final Stage stage) {
        stage.widthProperty().addListener((obs, oldVal, newVal)
                -> updateSizes(stage));
        stage.heightProperty().addListener((obs, oldVal, newVal)
                -> updateSizes(stage));
    }

    /**
     * Updates the sizes of tiles and actors based on the stage size.
     * @param stage The primary stage of the application.
     */
    private void updateSizes(final Stage stage) {
        int newTileSize = calculateTileSize(stage);
        TILE_SIZE.set(newTileSize);
        ACTOR_SIZE.set((int) (newTileSize / TILE_SIZE_DIMENSION_RATIO));
    }

    /**
     * Displays the save/load game menu.
     */
    public void showSaveLoadMenu() {
        SaveLoadMenu saveLoadMenu =
                new SaveLoadMenu(this, primaryStage, profileManager);
        VBox saveLoadMenuPane = saveLoadMenu.createSaveLoadMenu();
        primaryStage.getScene().setRoot(saveLoadMenuPane);
    }

    /**
     * Calculates the tile size based on the current stage size.
     * @param stage The primary stage of the application.
     * @return Calculated tile size.
     */
    private int calculateTileSize(final Stage stage) {
        return Math.min((int) stage.getWidth(),
                (int) stage.getHeight() - TILE_MAIN)
                / TILE_DIVIDE;
    }

    /**
     * Shows the main menu of the game.
     * @param primaryStage The primary stage of the application.
     */
    public void showMainMenu(final Stage primaryStage) {
        MainMenu mainMenu = new MainMenu();
        StackPane menuPane = mainMenu.createMainMenu(
                primaryStage,
                this);
        settingsMenu = new SettingsMenu().
                createSettingsMenu(
                        primaryStage,
                        this);
        settingsMenu.setVisible(false);
        menuPane.getChildren().add(settingsMenu);
        Scene scene = new Scene(
                menuPane,
                DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);
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

    /**
     * Saves the current game state to a specified file.
     *
     * @param saveName the name of the save file
     * @throws IOException if an I/O error occurs
     */
    public void saveGame(final String saveName) throws IOException {
        String profileName =
                profileManager.getCurrentProfile().getName();
        String saveFilePath = SAVES_DIR
                + "/"
                + profileName
                + "_" + saveName
                + ".json";
        System.out.println(saveFilePath);
        currentLevelData.setTimer(timerUI.getTimeRemaining());
        LevelStateManager.saveLevel(currentLevelData, saveFilePath);
        profileManager.addSaveToProfile(profileName, saveFilePath);
    }

    /**
     * Loads a game state from a specified file.
     *
     * @param saveName the name of the save file to load
     * @throws IOException if an I/O error occurs
     */
    public void loadGame(final String saveName)
            throws IOException {
        this.currentLevelData =
                LevelStateManager.loadLevel(saveName,
                        new LevelRenderer(currentLevelData));
        this.currentLevelPath =
                currentLevelData.getLevelPath();

        reinitializeLevel(currentLevelData);
    }


    /**
     * Handles the completion of the current level, updating the player's score.
     */
    private void handleLevelCompletion() {
        currentLevelData.getActors().stream()
                .filter(actor -> actor instanceof Player)
                .findFirst()
                .ifPresent(actor -> {
                    Player player = (Player) actor;
                    int score = player.getChipsCount()
                            + timerUI.getTimeRemaining();
                    profileManager.markLevelAsCompleted(
                            currentLevelPath, score);
                });
    }

    /**
     * Initializes the game scene with specified parameters.
     *
     * @param root   the root pane of the scene
     * @param stage  the stage to which the scene is set
     * @param width  the width of the scene
     * @param height the height of the scene
     */
    private void initScene(final StackPane root,
                           final Stage stage,
                           final int width,
                           final int height) {
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.setTitle(Main.GAME_TITLE);
        stage.show();
    }


    /**
     * Reinitializes the level with updated level data.
     *
     * @param levelData the updated data of the level
     */
    public void reinitializeLevel(final LevelData levelData) {
        LevelRenderer levelRenderer = levelData.getLevelRenderer();

        levelRenderer.setCurrentLevelData(levelData);

        levelRenderer.renderTiles(
                levelData.getTiles());
        levelRenderer.renderActors(
                levelData.getActors());
        levelRenderer.renderCollectibles(
                levelData.getCollectibles());
        levelRenderer.updateTileOccupation();
        double sceneWidth = calculateSceneDimension(
                levelData.getGridWidth()
                        * TILE_SIZE.get(),
                primaryStage.getWidth(),
                SCENE_MIN_WIDTH);
        double sceneHeight = calculateSceneDimension(
                levelData.getGridHeight()
                        * TILE_SIZE.get(),
                primaryStage.getHeight(),
                SCENE_MIN_HEIGHT);

        StackPane gamePane = initGamePane(levelData);
        initScene(gamePane,
                primaryStage,
                (int) sceneWidth,
                (int) sceneHeight
        );
        adjustPrimaryStage(primaryStage, levelData);

    }


    /**
     * Starts a specified level.
     *
     * @param levelPath the path to the level file
     * @param primaryStage the primary stage of the application
     * @throws IOException if an I/O error occurs
     */
    public void startLevel(final String levelPath,
                           final Stage primaryStage) throws IOException {
        this.currentLevelData = loadLevel(levelPath);
        initGamePane(currentLevelData);
        adjustPrimaryStage(primaryStage, currentLevelData);
    }


    /**
     * Adjusts the primary stage based on the provided level data.
     *
     * @param primaryStage the primary stage of the application
     * @param levelData the data of the level to adjust the stage for
     */
    private void adjustPrimaryStage(final Stage primaryStage,
                                    final LevelData levelData) {
        primaryStage.setMaximized(true);
        Platform.runLater(()
                -> adjustStageAndStartGame(primaryStage, levelData));
    }

    /**
     * Adjusts the stage size based on the
     * level data and starts the game.
     * It calculates the size of the scene based on level
     * dimensions, initializes the game pane,
     * and starts the game loop.
     *
     * @param primaryStage the primary stage of the application
     * @param levelData the data of the level to be played
     */
    private void adjustStageAndStartGame(final Stage primaryStage,
                                         final LevelData levelData) {
        double stageWidth = primaryStage.getWidth();
        double stageHeight = primaryStage.getHeight();
        double sceneWidth = calculateSceneDimension(
                levelData.getGridWidth()
                * TILE_SIZE.get(), stageWidth, MIN_WINDOW_SIZE);
        double sceneHeight = calculateSceneDimension(
                levelData.getGridHeight()
                * TILE_SIZE.get(), stageHeight, MIN_WINDOW_SIZE);
        StackPane gamePane = initGamePane(levelData);
        initScene(gamePane, primaryStage,
                (int) sceneWidth,
                (int) sceneHeight
        );
        initGameLoop(levelData);
        primaryStage.setMaximized(false);
    }

    private double calculateSceneDimension(final double calculatedSize,
                                           final double stageSize,
                                           final double minSize) {
        return Math.max(Math.max(
                calculatedSize, stageSize), minSize);
    }

    /**
     * Loads level data from the specified path.
     * This includes reading level configuration,
     * initializing tiles, actors, collectibles,
     * and setting up the level renderer with the loaded data.
     *
     * @param levelPath The path to the level file.
     * @return An instance of LevelData containing all thedata for the loaded level.
     * @throws IOException If there is an error reading the level file.
     */
    private LevelData loadLevel(final String levelPath)
            throws IOException {
        String content = Files.readString(Paths.get(levelPath));
        JSONObject levelJson = new JSONObject(content);
        int timer = levelJson.optInt("timer", DEFAULT_TIMER);

        LevelLoader levelLoader = new LevelLoader();
        Tile[][] tiles = levelLoader.loadTiles(levelPath);
        int gridWidth = tiles[0].length;
        int gridHeight = tiles.length;

        LevelRenderer levelRenderer =
                new LevelRenderer(currentLevelData);
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

    /**
     * Initializes the game pane with the provided level data.
     * This includes setting up the game pane's children, style, and rendering actors and collectibles.
     *
     * @param levelData The data of the level for which the game pane is initialized.
     * @return The initialized StackPane representing the game pane.
     */
    private StackPane initGamePane(final LevelData levelData) {
        StackPane rootPane = new StackPane(
                levelData.getLevelRenderer().getGamePane());

        rootPane.getChildren().add(settingsMenu);
        rootPane.setStyle(BACKGROUND_COLOR);
        levelData.getLevelRenderer().renderActors(
                levelData.getActors());
        levelData.getLevelRenderer().renderCollectibles(
                levelData.getCollectibles());
        this.timerUI = new
                TimerUI(levelData.getLevelRenderer().getGamePane(),
                levelData.getTimer());
        rootPane.getChildren().add(timerUI.getTimerLabel());
        StackPane.setAlignment(timerUI.getTimerLabel(),
                Pos.TOP_RIGHT);
        StackPane.setMargin(timerUI.getTimerLabel(),
                new Insets(INSECT, INSECT, 0, 0));
        return rootPane;
    }

    /**
     * Initializes and starts the game loop for the provided level data.
     * Stops any existing game loop before starting a new one. Sets up the camera,
     * game loop, and keybind handler for the level.
     *
     * @param levelData The data of the level for which the game loop is initialized.
     */
    private void initGameLoop(final LevelData levelData) {
        if (gameLoop != null) {
            gameLoop.stop();
        }

        Camera camera = new Camera(
                levelData.getLevelRenderer().getGamePane(),
                levelData.getGridWidth() * TILE_SIZE.get(),
                levelData.getGridHeight() * TILE_SIZE.get());

        gameLoop = new GameLoop(levelData.getActors(),
                levelData.getLevelRenderer(),
                camera, timerUI);


        KeybindHandler movementHandler =
                new KeybindHandler(gameLoop,
                        profileManager,
                        this,
                        INPUT_DELAY, MAX_QUEUE_SIZE);
        levelData.getLevelRenderer().
                getGamePane().getScene().setOnKeyPressed(event
                -> movementHandler.handleKeyPress(
                        event.getCode()));

        gameLoop.start();
    }

    /**
     * Advances the game to the
     * next level based on the current level's path.
     * Loads the next level and
     * reinitializes the game with the new level data if available.
     */
    public void advanceToNextLevel() {
        String nextLevelPath = getNextLevelPath();
        if (nextLevelPath != null
                && !nextLevelPath.equals(currentLevelPath)) {
            try {
                currentLevelData = loadLevel(nextLevelPath);
                currentLevelPath = nextLevelPath;
                reinitializeLevel(currentLevelData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Calculates and returns the path to
     * the next level based on the current level's path.
     * Determines the next level number
     * and constructs the path for the next level.
     *
     * @return The file path to the next level or null
     * if the next level does not exist.
     */
    private String getNextLevelPath() {
        String currentLevel =
                currentLevelPath.substring(
                        currentLevelPath.lastIndexOf('/') + 1);
        int currentLevelNumber =
                Integer.parseInt(
                        currentLevel.replaceAll(
                                "[^0-9]", ""));
        String nextLevel = "/level" + (
                currentLevelNumber + 1) + ".json";
        Path nextLevelPath = Paths.get(
                LEVELS_BASE_DIR + nextLevel);
        if (Files.exists(nextLevelPath)) {
            return nextLevelPath.toString();
        } else {
            return null;
        }
    }

    public enum SceneType {
        MAIN_MENU, SETTINGS, GAME
    }

    /**
     * Switches the current scene to the specified type.
     * Changes the visibility of menus and
     * initializes the appropriate scene based on the type.
     *
     * @param sceneType The type of scene to switch to.
     * @throws IllegalArgumentException if an invalid scene type is provided.
     */
    public void switchScene(final SceneType sceneType) {
        switch (sceneType) {
            case MAIN_MENU -> showMainMenu(primaryStage);
            case SETTINGS -> settingsMenu.setVisible(true);
            case GAME -> settingsMenu.setVisible(false);
            default -> throw new
                    IllegalArgumentException("Invalid scene type: "
                    + sceneType);
        }
    }

    /**
     * Toggles the visibility of the settings menu.
     * Switches between the game and settings scenes
     * based on the current visibility of the settings menu.
     */
    void toggleSettingsMenu() {
        if (settingsMenu.isVisible()) {
            switchScene(SceneType.GAME);
        } else {
            switchScene(SceneType.SETTINGS);
        }
    }

}
