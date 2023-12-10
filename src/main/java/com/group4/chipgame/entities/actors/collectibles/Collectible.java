package com.group4.chipgame.entities.actors.collectibles;

import com.group4.chipgame.EffectManager;
import com.group4.chipgame.Main;
import com.group4.chipgame.entities.actors.Actor;
import com.group4.chipgame.entities.actors.Entity;
import com.group4.chipgame.entities.actors.Player;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONObject;

import java.util.Objects;

/**
 * Represents a collectible entity in the game.
 */
public class Collectible extends ImageView implements Entity {
    protected Point2D currentPosition;

    /**
     * Creates a collectible entity with a specified image and position.
     *
     * @param imagePath The path to the image file.
     * @param x         The x-coordinate of the collectible's initial position.
     * @param y         The y-coordinate of the collectible's initial position.
     */
    public Collectible(final String imagePath, final double x, final double y) {
        Image image = new Image(Objects.
                requireNonNull(getClass().
                getResourceAsStream(imagePath)));
        setImage(image);
        setSmooth(true);
        fitWidthProperty().bind(Main.ACTOR_SIZE);
        fitHeightProperty().bind(Main.ACTOR_SIZE);

        this.currentPosition = new Point2D(x, y);
        updatePosition();

        Main.ACTOR_SIZE.addListener((obs, oldVal, newVal) -> updatePosition());
    }

    /**
     * Serializes the current state of the collectible into JSON format.
     *
     * @return JSONObject representing the collectible's current state.
     */
    public JSONObject serialize() {
        JSONObject object = new JSONObject();
        object.put("type", this.getClass().getSimpleName());
        object.put("x", currentPosition.getX());
        object.put("y", currentPosition.getY());
        return object;
    }

    /**
     * Updates the position of the collectible based on its current coordinates.
     */
    private void updatePosition() {
        double offset = (Main.TILE_SIZE.get() - Main.ACTOR_SIZE.get()) / 2.0;
        setLayoutX(currentPosition.getX() * Main.TILE_SIZE.get() + offset);
        setLayoutY(currentPosition.getY() * Main.TILE_SIZE.get() + offset);
        EffectManager.applyDynamicShadowEffect(this);
    }

    /**
     * Returns the current position of the collectible.
     *
     * @return The current position as a Point2D object.
     */
    @Override
    public Point2D getPosition() {
        return currentPosition;
    }

    /**
     * Defines the action to be taken when
     * this collectible is collected by an actor.
     * If the actor is a player, it makes the collectible invisible.
     *
     * @param actor The actor that collected the collectible.
     */
    public void onCollect(final Actor actor) {
        if (actor instanceof Player) {
            this.setVisible(false);
        }
    }
}
