package com.group4.chipgame;

import com.group4.chipgame.actors.Actor;
import com.group4.chipgame.collectibles.Collectible;
import com.group4.chipgame.menu.MainMenu;
import com.group4.chipgame.menu.SettingsMenu;
import com.group4.chipgame.tiles.Tile;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

/**
 * The main class for the Chip Game application. Extends JavaFX Application class.
 */
public class Main extends Application {

    // Constants for default sizes and titles
    public static SimpleIntegerProperty TILE_SIZE = new SimpleIntegerProperty(50);
    public static SimpleIntegerProperty ACTOR_SIZE = new SimpleIntegerProperty((int) (TILE_SIZE.get() / 1.5));
    public static final String MAIN_MENU_TITLE = "Chip Game Main Menu";
    public static final String GAME_TITLE = "Chip Game";
    public static final String BACKGROUND_COLOR = "-fx-background-color: black;";

    // Settings menu
    private StackPane settingsMenu;

    /**
     * The entry point of the application.
     *
     * @param args The command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * JavaFX application start method.
     *
     * @param primaryStage The primary stage for the application.
     */
    @Override
    public void start(Stage primaryStage) {
        // Initialize the main menu
        initMainMenu(primaryStage);

        // Add listeners to the width and height properties of the primaryStage
        // These listeners will be notified when the width or height of the primaryStage changes
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> updateSizes(primaryStage));
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> updateSizes(primaryStage));
    }

    /**
     * Update sizes of tiles and actors based on the size of the primaryStage.
     *
     * @param stage The primary stage for the application.
     */
    private void updateSizes(Stage stage) {
        int newTileSize = Math.min((int) stage.getWidth(), (int) stage.getHeight() - 100) / 10;
        TILE_SIZE.set(newTileSize);
        ACTOR_SIZE.set((int) (newTileSize / 1.5));
    }

    /**
     * Initialize the main menu.
     *
     * @param primaryStage The primary stage for the application.
     */
    private void initMainMenu(Stage primaryStage) {
        // Create the main menu and set it on the stage
        StackPane menuBox = createMainMenu(primaryStage);
        initScene(menuBox, primaryStage, 400, 400, MAIN_MENU_TITLE);
    }

    /**
     * Create the main menu.
     *
     * @param primaryStage The primary stage for the application.
     * @return The main menu as a StackPane.
     */
    private StackPane createMainMenu(Stage primaryStage) {
        // Create main menu and settings menu
        MainMenu mainMenu = new MainMenu();
        StackPane menuBox = mainMenu.createMainMenu(primaryStage, this);
        SettingsMenu settings = new SettingsMenu();
        settingsMenu = settings.createSettingsMenu(primaryStage, this);
        settingsMenu.setVisible(false);
        menuBox.getChildren().add(settingsMenu);
        return menuBox;
    }

    /**
     * Show the main menu.
     *
     * @param primaryStage The primary stage for the application.
     */
    public void showMainMenu(Stage primaryStage) {
        StackPane menuBox = createMainMenu(primaryStage);
        initScene(menuBox, primaryStage, 400, 400, MAIN_MENU_TITLE);
    }

    /**
     * Initialize the scene with the specified parameters.
     *
     * @param root   The root of the scene.
     * @param stage  The stage to set the scene on.
     * @param width  The width of the scene.
     * @param height The height of the scene.
     * @param title  The title of the scene.
     */
    private void initScene(StackPane root, Stage stage, int width, int height, String title) {
        Scene scene = new Scene(root, width, height);
        scene.setOnKeyPressed(this::handleKeyPress);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    /**
     * Handle key presses.
     *
     * @param event The KeyEvent representing the key press.
     */
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            // Toggle the visibility of the settings menu when ESC is pressed
            toggleSettingsMenu();
        }
    }

    /**
     * Start a game level.
     *
     * @param levelPath     The path to the level file.
     * @param primaryStage  The primary stage for the application.
     * @throws IOException If an I/O error occurs while loading the level.
     */
    public void startLevel(String levelPath, Stage primaryStage) throws IOException {
        LevelData levelData = loadLevel(levelPath);
        HUDRenderer hudRenderer = levelData.hudRenderer;

        StackPane gamePane = initGamePane(levelData);
        gamePane.getChildren().addAll(hudRenderer.getHUDPane());

        KeyFrame keyFrame = new KeyFrame(Duration.millis(16), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Score needs to be handled
                hudRenderer.updateScore(0);
            }
        });

        Timeline gameLoop = new Timeline(keyFrame);
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
        hudRenderer.startTimer();

        // Maximize the stage temporarily to get its size
        primaryStage.setMaximized(true);

        // Run on the JavaFX application thread to ensure correct size calculations
        Platform.runLater(() -> {
            double stageWidth = primaryStage.getWidth();
            double stageHeight = primaryStage.getHeight();

            double sceneWidth = Math.max(levelData.gridWidth * TILE_SIZE.get(), stageWidth);
            double sceneHeight = Math.max(levelData.gridHeight * TILE_SIZE.get(), stageHeight);

            double minSceneWidth = 300;
            double minSceneHeight = 300;
            sceneWidth = Math.max(sceneWidth, minSceneWidth);
            sceneHeight = Math.max(sceneHeight, minSceneHeight);

            // Initialize the scene with the calculated size
            initScene(gamePane, primaryStage, (int) sceneWidth, (int) sceneHeight, GAME_TITLE);

            // Initialize the game loop and set the stage to not be maximized
            initGameLoop(levelData);
            primaryStage.setMaximized(false);
        });
    }

    /**
     * Load level data from the specified level path.
     *
     * @param levelPath The path to the level file.
     * @return The loaded level data.
     * @throws IOException If an I/O error occurs while loading the level.
     */
    private LevelData loadLevel(String levelPath) throws IOException {
        LevelLoader levelLoader = new LevelLoader();
        Tile[][] tiles = levelLoader.loadTiles(levelPath);
        int gridWidth = tiles[0].length;
        int gridHeight = tiles.length;
        LevelRenderer levelRenderer = new LevelRenderer();
        levelRenderer.renderTiles(tiles);
        List<Actor> actors = levelLoader.loadActors(levelPath, levelRenderer);
        levelRenderer.renderActors(actors);
        List<Collectible> collectibles = levelLoader.loadCollectibles(levelPath, levelRenderer);
        levelRenderer.renderCollectibles(collectibles);
        HUDRenderer hudRenderer = new HUDRenderer();
        return new LevelData(gridWidth, gridHeight, actors, collectibles, levelRenderer, hudRenderer);
    }

    /**
     * Initialize the game pane with the specified level data.
     *
     * @param levelData The level data.
     * @return The initialized game pane.
     */
    private StackPane initGamePane(LevelData levelData) {
        StackPane rootPane = new StackPane(levelData.levelRenderer.getGamePane());
        rootPane.getChildren().add(settingsMenu);
        rootPane.setStyle(BACKGROUND_COLOR);
        return rootPane;
    }

    /**
     * Initialize the game loop with the specified level data.
     *
     * @param levelData The level data.
     */
    private void initGameLoop(LevelData levelData) {
        Scene currentScene = levelData.levelRenderer.getGamePane().getScene();

        // Initialize the camera, game loop, and handle input
        Camera camera = new Camera(levelData.levelRenderer.getGamePane(), levelData.gridWidth * TILE_SIZE.get(), levelData.gridHeight * TILE_SIZE.get());
        GameLoop gameLoop = new GameLoop(levelData.actors, levelData.levelRenderer, camera);
        camera.adjustCamera();
        final long[] lastInputTime = {0};
        long inputDelay = 200;
        int maxQueueSize = 5;

        // Handle key presses and add them to the move queue
        currentScene.setOnKeyPressed(event -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastInputTime[0] > inputDelay) {
                lastInputTime[0] = currentTime;

                KeyCode key = event.getCode();
                double[] delta = GameLoop.KEY_TO_DELTA.getOrDefault(key, null);
                if (delta != null) {
                    Direction direction = Direction.fromDelta(delta[0], delta[1]);
                    if (gameLoop.getMoveQueue().size() < maxQueueSize) {
                        gameLoop.getMoveQueue().add(direction);
                    }
                }
            }
        });

        // Start the game loop
        gameLoop.start();
    }

    /**
     * Toggle the visibility of the settings menu.
     */
    private void toggleSettingsMenu() {
        settingsMenu.setVisible(!settingsMenu.isVisible());
    }

    /**
     * Data structure to hold level data.
     */
    private static class LevelData {
        int gridWidth;
        int gridHeight;
        List<Actor> actors;
        List<Collectible> collectibles;
        LevelRenderer levelRenderer;

        HUDRenderer hudRenderer;

        /**
         * Constructor for LevelData.
         *
         * @param gridWidth     The width of the grid.
         * @param gridHeight    The height of the grid.
         * @param actors        The list of actors in the level.
         * @param collectibles  The list of collectibles in the level.
         * @param levelRenderer The renderer for the level.
         */
        LevelData(int gridWidth, int gridHeight, List<Actor> actors, List<Collectible> collectibles, LevelRenderer levelRenderer, HUDRenderer hudRenderer) {
            this.gridWidth = gridWidth;
            this.gridHeight = gridHeight;
            this.actors = actors;
            this.collectibles = collectibles;
            this.levelRenderer = levelRenderer;
            this.hudRenderer = hudRenderer;
        }
    }
}
