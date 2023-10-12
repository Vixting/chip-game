package com.group4.chipgame;

import com.group4.chipgame.actors.Actor;
import com.group4.chipgame.actors.Player;
import com.group4.chipgame.CollisionHandler;
import com.group4.chipgame.tiles.Tile;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main extends Application {
    public static final int TILE_SIZE = 100;
    public static final int ACTOR_SIZE = TILE_SIZE - 20;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        LevelLoader levelLoader = new LevelLoader();
        Tile[][] tiles = levelLoader.loadTiles("/levels/island.json");
        List<Actor> actors = levelLoader.loadActors("/levels/island.json");

        int gridWidth = tiles[0].length;
        int gridHeight = tiles.length;

        LevelRenderer levelRenderer = new LevelRenderer();
        levelRenderer.renderTiles(tiles);
        levelRenderer.renderActors(actors);

        Scene scene = new Scene(levelRenderer.getGamePane(), gridWidth * TILE_SIZE, gridHeight * TILE_SIZE);

        // Initialize bindings before setting the scene.
        levelRenderer.initializeBindings(scene);

        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setTitle("Chip Game");
        primaryStage.show();

        Set<KeyCode> pressedKeys = new HashSet<>();

        scene.setOnKeyPressed(event -> pressedKeys.add(event.getCode()));

        scene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));

        Camera camera = new Camera(levelRenderer.getGamePane(), gridWidth * Main.TILE_SIZE, gridHeight * Main.TILE_SIZE);
        CollisionHandler collisionHandler = new CollisionHandler();

        for (Actor actor : actors) {
            if (actor instanceof Player) {
                camera.setTarget(actor);
                long[] lastMoveTime = {0}; // To store the timestamp of the last movement
                final long MOVE_INTERVAL = 250_000_000; // Cooldown period, 250ms in nanoseconds

                AnimationTimer gameLoop = new AnimationTimer() {
                    @Override
                    public void handle(long now) {
                        if (now - lastMoveTime[0] < MOVE_INTERVAL) {
                            return;
                        }

                        if (pressedKeys.contains(KeyCode.UP)) {
                            actor.move(0, -1, levelRenderer);
                            pressedKeys.remove(KeyCode.UP); // Prevent continuous movement when key is held down
                        } else if (pressedKeys.contains(KeyCode.DOWN)) {
                            actor.move(0, 1, levelRenderer);
                            pressedKeys.remove(KeyCode.DOWN);
                        } else if (pressedKeys.contains(KeyCode.LEFT)) {
                            actor.move(-1, 0, levelRenderer);
                            pressedKeys.remove(KeyCode.LEFT);
                        } else if (pressedKeys.contains(KeyCode.RIGHT)) {
                            actor.move(1, 0, levelRenderer);
                            pressedKeys.remove(KeyCode.RIGHT);
                        } else {
                            return;
                        }

                        // Check for collisions after the movement
                        for (Actor otherActor : actors) {
                            if (otherActor != actor && collisionHandler.actorsCollide(actor, otherActor)) {
                                collisionHandler.handleActorOnActorCollision(actor, otherActor);
                            }
                        }

                        // Check for collisions with tiles
                        for (Tile[] tile : tiles) {
                            for (Tile value : tile) {
                                if (collisionHandler.actorTileCollide(actor, value)) {
                                    collisionHandler.handleActorOnTileCollision(actor, value);
                                }
                            }
                        }

                        lastMoveTime[0] = now;
                    }
                };

                gameLoop.start();
                break;
            }
        }
    }
}
