package com.group4.chipgame;

import com.group4.chipgame.actors.Actor;
import com.group4.chipgame.tiles.Tile;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Main extends Application {
    public static final int TILE_SIZE = 64; // example size
    public static final int ACTOR_SIZE = TILE_SIZE - 10; // 10 pixels smaller

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        LevelLoader levelLoader = new LevelLoader();
        Tile[][] tiles = levelLoader.loadTiles("/levels/level.json");
        List<Actor> actors = levelLoader.loadActors("/levels/level.json");

        LevelRenderer levelRenderer = new LevelRenderer();
        levelRenderer.renderTiles(tiles);
        levelRenderer.renderActors(actors);

        Scene scene = new Scene(levelRenderer.getGamePane(), 10 * TILE_SIZE, 10 * TILE_SIZE);
        primaryStage.setScene(scene);
        levelRenderer.initializeBindings(scene);
        primaryStage.setResizable(true);
        primaryStage.setTitle("Chip Game");
        primaryStage.show();
    }
}
