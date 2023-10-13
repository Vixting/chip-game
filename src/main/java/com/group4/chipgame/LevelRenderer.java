package com.group4.chipgame;

import com.group4.chipgame.tiles.Tile;
import com.group4.chipgame.actors.Actor;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Optional;

public class LevelRenderer {

    // Constants for offset calculations
    private static final double OFFSET_X = (Main.TILE_SIZE - Main.ACTOR_SIZE) / 2.0;
    private static final double OFFSET_Y = (Main.TILE_SIZE - Main.ACTOR_SIZE) / 2.0;

    // Panes for rendering the game components
    private final Pane cameraPane;
    private Camera camera;
    private Pane gamePane;
    private Pane tilesPane;
    private Pane actorsPane;

    // 2D array to store tiles
    private Tile[][] tiles;

    // Constructor for LevelRenderer
    public LevelRenderer() {
        this.gamePane = new Pane();
        this.tilesPane = new Pane();
        this.actorsPane = new Pane();
        this.gamePane.getChildren().addAll(tilesPane, actorsPane);

        this.cameraPane = new Pane(this.gamePane); // Initialize cameraPane to contain gamePane
    }

    // Initialize bindings for scaling and positioning game components
    public void initializeBindings(Scene scene) {
        DoubleBinding scaleBinding = calculateScaleBinding(scene);
        gamePane.scaleXProperty().bind(scaleBinding);
        gamePane.scaleYProperty().bind(scaleBinding);

        // Bind the translation properties for centering the game within the scene
        gamePane.translateXProperty().bind(Bindings.createDoubleBinding(() -> {
            double scaleX = scene.getWidth() / (Main.TILE_SIZE * tiles[0].length);
            return (scene.getWidth() - (Main.TILE_SIZE * tiles[0].length) * scaleX) / 2;
        }, scene.widthProperty(), scaleBinding));

        gamePane.translateYProperty().bind(Bindings.createDoubleBinding(() -> {
            double scaleY = scene.getHeight() / (Main.TILE_SIZE * tiles.length);
            return (scene.getHeight() - (Main.TILE_SIZE * tiles.length) * scaleY) / 2;
        }, scene.heightProperty(), scaleBinding));
    }

    // Calculate a binding for scaling the game components based on the scene size
    private DoubleBinding calculateScaleBinding(Scene scene) {
        final double HORIZONTAL_TILE_COUNT = tiles[0].length;
        final double VERTICAL_TILE_COUNT = tiles.length;
        final double ORIGINAL_GAME_WIDTH = Main.TILE_SIZE * HORIZONTAL_TILE_COUNT;
        final double ORIGINAL_GAME_HEIGHT = Main.TILE_SIZE * VERTICAL_TILE_COUNT;

        return Bindings.createDoubleBinding(
                () -> {
                    double scaleX = scene.getWidth() / ORIGINAL_GAME_WIDTH;
                    double scaleY = scene.getHeight() / ORIGINAL_GAME_HEIGHT;
                    return Math.min(scaleX, scaleY);
                },
                scene.widthProperty(), scene.heightProperty()
        );
    }

    // Getter for the gamePane
    public Pane getGamePane() {
        return gamePane;
    }

    // Getter for the cameraPane
    public Pane getCameraPane() {
        return cameraPane;
    }

    // Render the tiles on the gamePane
    public void renderTiles(Tile[][] tiles) {
        this.tiles = tiles;
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                Tile tile = tiles[y][x];
                tile.bindSize(gamePane);
                tile.setLayoutX(x * Main.TILE_SIZE);
                tile.setLayoutY(y * Main.TILE_SIZE);
                tilesPane.getChildren().add(tile);
            }
        }
    }

    // Retrieve a tile at a given grid position
    public Optional<Tile> getTileAtGridPosition(int x, int y) {
        if (isOutOfBounds(x, y)) {
            return Optional.empty();
        }
        return Optional.of(tiles[y][x]);
    }

    // Check if a grid position is out of bounds
    private boolean isOutOfBounds(int x, int y) {
        return x < 0 || x >= tiles[0].length || y < 0 || y >= tiles.length;
    }

    // Render actors on the gamePane
    public void renderActors(List<Actor> actors) {
        Optional.ofNullable(actors).ifPresent(actorsList -> actorsList.forEach(this::positionAndAddActor));
    }

    // Position and add an actor to the actorsPane
    private void positionAndAddActor(Actor actor) {
        Point2D actorPosition = actor.getPosition();
        if (actorPosition != null) {
            double actorLayoutX = actorPosition.getX() * Main.TILE_SIZE + OFFSET_X;
            double actorLayoutY = actorPosition.getY() * Main.TILE_SIZE + OFFSET_Y;
            actor.setLayoutX(actorLayoutX);
            actor.setLayoutY(actorLayoutY);
            actorsPane.getChildren().add(actor);
        }
    }

    // Clear the tiles and actors from the renderer
    public void clear() {
        tilesPane.getChildren().clear();
        actorsPane.getChildren().clear();
    }
}
