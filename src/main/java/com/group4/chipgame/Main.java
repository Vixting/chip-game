package com.group4.chipgame;

import com.group4.chipgame.actors.Actor;
import com.group4.chipgame.actors.Player;
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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load tiles and actors from a level file
        LevelLoader levelLoader = new LevelLoader();
        Tile[][] tiles = levelLoader.loadTiles("/levels/island.json");

        int gridWidth = tiles[0].length;
        int gridHeight = tiles.length;

        // Create a level renderer to render game components
        LevelRenderer levelRenderer = new LevelRenderer();
        levelRenderer.renderTiles(tiles);
        List<Actor> actors = levelLoader.loadActors("/levels/island.json", levelRenderer);
        levelRenderer.renderActors(actors);

        // Create a scene with the game rendering components
        StackPane rootPane = new StackPane(levelRenderer.getGamePane());
        Scene scene = new Scene(rootPane, gridWidth * TILE_SIZE, gridHeight * TILE_SIZE);

        // Set up the primary stage
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setTitle("Chip Game");
        rootPane.setStyle("-fx-background-color: black;");
        primaryStage.show();

        // Set up input handling with pressedKeys set
        Set<KeyCode> pressedKeys = new HashSet<>();
        scene.setOnKeyPressed(event -> pressedKeys.add(event.getCode()));
        scene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));

        // Create a camera to follow the player actor
        Camera camera = new Camera(
                levelRenderer.getGamePane(),
                gridWidth * TILE_SIZE,
                gridHeight * TILE_SIZE
        );

        // Create a collision handler to manage collisions
        CollisionHandler collisionHandler = new CollisionHandler();
        levelRenderer.initializeBindings(scene);
        primaryStage.setMaximized(true);

        // Start the game loop with input handling and collision checking
        startGameLoop(actors, pressedKeys, levelRenderer, tiles, collisionHandler, camera);
    }

    private void startGameLoop(List<Actor> actors, Set<KeyCode> pressedKeys, LevelRenderer levelRenderer, Tile[][] tiles, CollisionHandler collisionHandler, Camera camera) {
        // Find the player actor to attach the camera
        for (Actor actor : actors) {
            if (actor instanceof Player) {
                camera.setTarget(actor);
                long[] lastMoveTime = {0};
                final long MOVE_INTERVAL = 250_000_000;

                // Create an AnimationTimer for game logic and player movement
                AnimationTimer gameLoop = new AnimationTimer() {
                    @Override
                    public void handle(long now) {
                        if (now - lastMoveTime[0] < MOVE_INTERVAL) {
                            return;
                        }

                        // Handle player movement based on pressed keys
                        if (pressedKeys.contains(KeyCode.UP)) {
                            checkCollisions(actor, actors, tiles, collisionHandler, 0, -1, levelRenderer);
                            actor.move(0, -1, levelRenderer, collisionHandler);
                            pressedKeys.remove(KeyCode.UP);
                        } else if (pressedKeys.contains(KeyCode.DOWN)) {
                            checkCollisions(actor, actors, tiles, collisionHandler, 0, 1, levelRenderer);
                            actor.move(0, 1, levelRenderer, collisionHandler);
                            pressedKeys.remove(KeyCode.DOWN);
                        } else if (pressedKeys.contains(KeyCode.LEFT)) {
                            checkCollisions(actor, actors, tiles, collisionHandler, -1, 0, levelRenderer);
                            actor.move(-1, 0, levelRenderer, collisionHandler);
                            pressedKeys.remove(KeyCode.LEFT);
                        } else if (pressedKeys.contains(KeyCode.RIGHT)) {
                            checkCollisions(actor, actors, tiles, collisionHandler, 1, 0, levelRenderer);
                            actor.move(1, 0, levelRenderer, collisionHandler);
                            pressedKeys.remove(KeyCode.RIGHT);
                        } else {
                            return;
                        }


                        // Check for collisions between the player and other game components
                        lastMoveTime[0] = now;
                    }
                };

                gameLoop.start();
                break;
            }
        }
    }

    private void checkCollisions(Actor actor, List<Actor> actors, Tile[][] tiles, CollisionHandler collisionHandler, double dx, double dy, LevelRenderer levelRenderer) {
        // Check for collisions between the player actor and tiles FIRST
        for (Tile[] tileRow : tiles) {
            for (Tile tile : tileRow) {
                if (collisionHandler.actorTileCollide(actor, tile, dx, dy)) {
                    System.out.println("Actor-tile collision!");
                    collisionHandler.handleTileInteraction(actor, dx, dy, levelRenderer);
                    collisionHandler.handleActorOnTileCollision(actor, tile, levelRenderer);
                }
            }
        }

        // THEN check for collisions between actors
        for (Actor otherActor : actors) {
            if (otherActor != actor && collisionHandler.actorsCollide(actor, otherActor)) {
                collisionHandler.handleActorOnActorCollision(actor, otherActor, dx, dy, levelRenderer);
            }
        }
    }


}