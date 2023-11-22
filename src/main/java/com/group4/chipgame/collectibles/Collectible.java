package com.group4.chipgame.collectibles;

import com.group4.chipgame.Entity;
import com.group4.chipgame.Main;
import com.group4.chipgame.actors.Actor;
import com.group4.chipgame.actors.Player;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * The Collectible class represents an item that can be collected in the game.
 * It extends ImageView and implements the Entity interface.
 */
public class Collectible extends ImageView implements Entity {
    protected Point2D currentPosition;

    /**
     * Constructs a Collectible object with the specified image path and grid position.
     *
     * @param imagePath The path to the image representing the collectible.
     * @param x          The x-coordinate of the collectible on the grid.
     * @param y          The y-coordinate of the collectible on the grid.
     */
    public Collectible(String imagePath, double x, double y) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        setImage(image);
        setSmooth(true);
        fitWidthProperty().bind(Main.ACTOR_SIZE);
        fitHeightProperty().bind(Main.ACTOR_SIZE);

        this.currentPosition = new Point2D(x, y);
        updatePosition();

        Main.ACTOR_SIZE.addListener((obs, oldVal, newVal) -> updatePosition());
    }

    /**
     * Updates the position of the collectible based on the grid coordinates.
     */
    private void updatePosition() {
        double offset = (Main.TILE_SIZE.get() - Main.ACTOR_SIZE.get()) / 2.0;
        setLayoutX(currentPosition.getX() * Main.TILE_SIZE.get() + offset);
        setLayoutY(currentPosition.getY() * Main.TILE_SIZE.get() + offset);
    }

    /**
     * Gets the current position of the collectible on the grid.
     *
     * @return The grid coordinates of the collectible.
     */
    @Override
    public Point2D getPosition() {
        return currentPosition;
    }

    /**
     * Handles the collection of the collectible by an actor.
     * If the actor is a Player, the collectible becomes invisible.
     *
     * @param actor The actor collecting the collectible.
     */
    public void onCollect(Actor actor) {
        if (actor instanceof Player) {
            this.setVisible(false);
        }
    }
}
