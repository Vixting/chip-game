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

import java.io.IOException;
import java.util.List;

public class Main extends Application {
    public static SimpleIntegerProperty TILE_SIZE = new SimpleIntegerProperty(50);
    public static SimpleIntegerProperty ACTOR_SIZE = new SimpleIntegerProperty((int) (TILE_SIZE.get() / 1.5));
    public static final String MAIN_MENU_TITLE = "Chip Game Main Menu";
    public static final String GAME_TITLE = "Chip Game";
    public static final String BACKGROUND_COLOR = "-fx-background-color: black;";

    private StackPane settingsMenu;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        initMainMenu(primaryStage);
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> updateSizes(primaryStage));
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> updateSizes(primaryStage));
    }

    private void updateSizes(Stage stage) {
        int newTileSize = Math.min((int) stage.getWidth() , (int) stage.getHeight() - 100) / 10;
        TILE_SIZE.set(newTileSize);
        ACTOR_SIZE.set((int) (newTileSize / 1.5));
    }

    private void initMainMenu(Stage primaryStage) {
        StackPane menuBox = createMainMenu(primaryStage);
        initScene(menuBox, primaryStage, 400, 400, MAIN_MENU_TITLE);
    }

    private StackPane createMainMenu(Stage primaryStage) {
        MainMenu mainMenu = new MainMenu();
        StackPane menuBox = mainMenu.createMainMenu(primaryStage, this);
        SettingsMenu settings = new SettingsMenu();
        settingsMenu = settings.createSettingsMenu(primaryStage, this);
        settingsMenu.setVisible(false);
        menuBox.getChildren().add(settingsMenu);
        return menuBox;
    }

    public void showMainMenu(Stage primaryStage) {
        StackPane menuBox = createMainMenu(primaryStage);
        initScene(menuBox, primaryStage, 400, 400, MAIN_MENU_TITLE);
    }

    private void initScene(StackPane root, Stage stage, int width, int height, String title) {
        Scene scene = new Scene(root, width, height);
        scene.setOnKeyPressed(this::handleKeyPress);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            toggleSettingsMenu();
        }
    }

    public void startLevel(String levelPath, Stage primaryStage) throws IOException {
        LevelData levelData = loadLevel(levelPath);
        StackPane gamePane = initGamePane(levelData);

        primaryStage.setMaximized(true);

        Platform.runLater(() -> {
            double stageWidth = primaryStage.getWidth();
            double stageHeight = primaryStage.getHeight();

            double sceneWidth = Math.max(levelData.gridWidth * TILE_SIZE.get(), stageWidth);
            double sceneHeight = Math.max(levelData.gridHeight * TILE_SIZE.get(), stageHeight);

            double minSceneWidth = 300;
            double minSceneHeight = 300;
            sceneWidth = Math.max(sceneWidth, minSceneWidth);
            sceneHeight = Math.max(sceneHeight, minSceneHeight);

            initScene(gamePane, primaryStage, (int) sceneWidth, (int) sceneHeight, GAME_TITLE);
            initGameLoop(levelData);
            primaryStage.setMaximized(false);
        });
    }

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
        return new LevelData(gridWidth, gridHeight, actors, collectibles, levelRenderer);
    }

    private StackPane initGamePane(LevelData levelData) {
        StackPane rootPane = new StackPane(levelData.levelRenderer.getGamePane());
        rootPane.getChildren().add(settingsMenu);
        rootPane.setStyle(BACKGROUND_COLOR);
        return rootPane;
    }

    private void initGameLoop(LevelData levelData) {
        Scene currentScene = levelData.levelRenderer.getGamePane().getScene();

        Camera camera = new Camera(levelData.levelRenderer.getGamePane(), levelData.gridWidth * TILE_SIZE.get(), levelData.gridHeight * TILE_SIZE.get());
        GameLoop gameLoop = new GameLoop(levelData.actors, levelData.levelRenderer, camera);
        camera.adjustCamera();
        final long[] lastInputTime = {0};
        long inputDelay = 200;
        int maxQueueSize = 5;

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

        gameLoop.start();
    }

    private void toggleSettingsMenu() {
        settingsMenu.setVisible(!settingsMenu.isVisible());
    }

    private static class LevelData {
        int gridWidth;
        int gridHeight;
        List<Actor> actors;
        List<Collectible> collectibles;
        LevelRenderer levelRenderer;

        LevelData(int gridWidth, int gridHeight, List<Actor> actors, List<Collectible> collectibles, LevelRenderer levelRenderer) {
            this.gridWidth = gridWidth;
            this.gridHeight = gridHeight;
            this.actors = actors;
            this.collectibles = collectibles;
            this.levelRenderer = levelRenderer;
        }
    }
}
