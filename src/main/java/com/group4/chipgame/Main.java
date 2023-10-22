package com.group4.chipgame;

import com.group4.chipgame.actors.Actor;
import com.group4.chipgame.actors.Player;
import com.group4.chipgame.collectibles.Collectible;
import com.group4.chipgame.menu.MainMenu;
import com.group4.chipgame.menu.SettingsMenu;
import com.group4.chipgame.tiles.Tile;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main extends Application {
    public static final int TILE_SIZE = 200;
    public static final int ACTOR_SIZE = (int) (TILE_SIZE / 1.5);
    private StackPane settingsMenu;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        MainMenu mainMenu = new MainMenu();
        StackPane menuBox = mainMenu.createMainMenu(primaryStage, this);
        SettingsMenu settings = new SettingsMenu();
        settingsMenu = settings.createSettingsMenu(primaryStage, this);
        settingsMenu.setVisible(false);
        menuBox.getChildren().add(settingsMenu);
        Scene scene = new Scene(menuBox, 400, 400);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                toggleSettingsMenu();
            }
        });
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chip Game Main Menu");
        primaryStage.show();
    }

    public void startLevel(String levelPath, Stage primaryStage) throws IOException {
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
        StackPane rootPane = new StackPane(levelRenderer.getGamePane());
        rootPane.getChildren().add(settingsMenu);
        Scene scene = new Scene(rootPane, gridWidth * TILE_SIZE, gridHeight * TILE_SIZE);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setTitle("Chip Game");
        rootPane.setStyle("-fx-background-color: black;");
        primaryStage.show();
        Set<KeyCode> pressedKeys = new HashSet<>();
        scene.setOnKeyPressed(event -> {
            pressedKeys.add(event.getCode());
            if (event.getCode() == KeyCode.ESCAPE) {
                toggleSettingsMenu();
            }
        });
        scene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));
        Camera camera = new Camera(levelRenderer.getGamePane(), gridWidth * TILE_SIZE, gridHeight * TILE_SIZE);
        CollisionHandler collisionHandler = new CollisionHandler();
        levelRenderer.initializeBindings(scene);
        primaryStage.setMaximized(true);
        startGameLoop(actors, collectibles, pressedKeys, levelRenderer, collisionHandler, camera);
    }

    private void startGameLoop(List<Actor> actors, List<Collectible> collectibles, Set<KeyCode> pressedKeys, LevelRenderer levelRenderer, CollisionHandler collisionHandler, Camera camera) {
        for (Actor actor : actors) {
            if (actor instanceof Player) {
                camera.setTarget(actor);
                long[] lastMoveTime = {0};
                final long MOVE_INTERVAL = 250_000_000;
                AnimationTimer gameLoop = new AnimationTimer() {
                    @Override
                    public void handle(long now) {
                        if (now - lastMoveTime[0] < MOVE_INTERVAL) {
                            return;
                        }
                        if (pressedKeys.contains(KeyCode.UP)) {
                            checkCollisions(actor, levelRenderer.getTiles(), collisionHandler, 0, -1, levelRenderer, collectibles);
                            actor.move(0, -1, levelRenderer, collisionHandler);
                            pressedKeys.remove(KeyCode.UP);
                        } else if (pressedKeys.contains(KeyCode.DOWN)) {
                            checkCollisions(actor, levelRenderer.getTiles(), collisionHandler, 0, 1, levelRenderer, collectibles);
                            actor.move(0, 1, levelRenderer, collisionHandler);
                            pressedKeys.remove(KeyCode.DOWN);
                        } else if (pressedKeys.contains(KeyCode.LEFT)) {
                            checkCollisions(actor, levelRenderer.getTiles(), collisionHandler, -1, 0, levelRenderer, collectibles);
                            actor.move(-1, 0, levelRenderer, collisionHandler);
                            pressedKeys.remove(KeyCode.LEFT);
                        } else if (pressedKeys.contains(KeyCode.RIGHT)) {
                            checkCollisions(actor, levelRenderer.getTiles(), collisionHandler, 1, 0, levelRenderer, collectibles);
                            actor.move(1, 0, levelRenderer, collisionHandler);
                            pressedKeys.remove(KeyCode.RIGHT);
                        } else {
                            return;
                        }
                        lastMoveTime[0] = now;
                    }
                };
                gameLoop.start();
                break;
            }
        }
    }

    private void checkCollisions(Actor actor, Tile[][] tiles, CollisionHandler collisionHandler, double dx, double dy, LevelRenderer levelRenderer, List<Collectible> collectibles) {
        for (Actor otherActor : levelRenderer.getActors()) {
            if (otherActor != actor && collisionHandler.actorsCollide(actor, otherActor)) {
                collisionHandler.handleActorOnActorCollision(actor, otherActor, dx, dy, levelRenderer);
            }
        }
        for (Tile[] tileRow : tiles) {
            for (Tile tile : tileRow) {
                if (collisionHandler.actorTileCollide(actor, tile, dx, dy)) {
                    collisionHandler.handleTileInteraction(actor, dx, dy, levelRenderer);
                }
            }
        }
        for (Collectible collectible : collectibles) {
            if (collisionHandler.actorCollidesWithCollectible(actor, collectible)) {
                collisionHandler.handleActorOnCollectibleCollision(actor, collectible, levelRenderer);
            }
        }

    }

    private void toggleSettingsMenu() {
        settingsMenu.setVisible(!settingsMenu.isVisible());
    }
}
