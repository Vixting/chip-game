package com.group4.chipgame;

import com.group4.chipgame.tiles.Tile;
import com.group4.chipgame.actors.Actor;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.List;

public class LevelRenderer {

    private Pane gamePane;

    public LevelRenderer() {
        this.gamePane = new Pane();
        gamePane.setManaged(false);
    }

    public void initializeBindings(Scene scene) {
        // Assuming the original size of the game (before scaling) is defined by the constants
        final double ORIGINAL_GAME_WIDTH = Main.TILE_SIZE * 10; // Assuming 10 tiles horizontally
        final double ORIGINAL_GAME_HEIGHT = Main.TILE_SIZE * 10; // Assuming 10 tiles vertically

        // Calculate the scaling factor.
        DoubleBinding scaleBinding = Bindings.createDoubleBinding(
                () -> {
                    double scaleX = scene.getWidth() / ORIGINAL_GAME_WIDTH;
                    double scaleY = scene.getHeight() / ORIGINAL_GAME_HEIGHT;
                    return Math.min(scaleX, scaleY);  // Use the smaller scale
                },
                scene.widthProperty(), scene.heightProperty()
        );

        // Apply the scaling factor to the gamePane.
        gamePane.scaleXProperty().bind(scaleBinding);
        gamePane.scaleYProperty().bind(scaleBinding);

        // Set the size of gamePane according to its original dimensions.
        gamePane.setPrefWidth(ORIGINAL_GAME_WIDTH);
        gamePane.setPrefHeight(ORIGINAL_GAME_HEIGHT);

        // Center the gamePane.
        gamePane.layoutXProperty().bind(Bindings.divide(Bindings.subtract(scene.widthProperty(), Bindings.multiply(gamePane.widthProperty(), scaleBinding)), 2));
        gamePane.layoutYProperty().bind(Bindings.divide(Bindings.subtract(scene.heightProperty(), Bindings.multiply(gamePane.heightProperty(), scaleBinding)), 2));
    }

    public Pane getGamePane() {
        return gamePane;
    }

    // Render the tiles on the pane
    public void renderTiles(Tile[][] tiles) {
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                Tile tile = tiles[y][x];
                tile.bindSize(gamePane);  // Bind the size here
                tile.setLayoutX(x * Main.TILE_SIZE);
                tile.setLayoutY(y * Main.TILE_SIZE);
                gamePane.getChildren().add(tile);
            }
        }
    }

    // Render the actors on the pane
    public void renderActors(List<Actor> actors) {
        for (Actor actor : actors) {
            actor.bindSize(gamePane);  // Bind the size here
            actor.setLayoutX(actor.getPosition().getX() * Main.TILE_SIZE + (Main.TILE_SIZE - Main.ACTOR_SIZE) / 2);
            actor.setLayoutY(actor.getPosition().getY() * Main.TILE_SIZE + (Main.TILE_SIZE - Main.ACTOR_SIZE) / 2);
            gamePane.getChildren().add(actor);
        }
    }


    // Optional: clear the pane, e.g., for re-rendering
    public void clear() {
        gamePane.getChildren().clear();
    }

}
