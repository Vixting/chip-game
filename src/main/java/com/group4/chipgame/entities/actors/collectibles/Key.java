package com.group4.chipgame.entities.actors.collectibles;

import org.json.JSONObject;

/**
 * Represents a key collectible in the game.
 * Keys can have different colors and are used to unlock specific doors.
 * @author William Buckley
 */
public class Key extends Collectible {

    /**
     * Enum representing the available colors for keys.
     */
    public enum KeyColor {
        RED, GREEN, YELLOW, BLUE
    }

    private final KeyColor color;

    /**
     * Constructs a new Key object with a specified color and position.
     *
     * @param color The color of the key.
     * @param gridX The x-coordinate of the key's position.
     * @param gridY The y-coordinate of the key's position.
     */
    public Key(final KeyColor color, final double gridX, final double gridY) {
        super(getImagePathForColor(color), gridX, gridY);
        this.color = color;
    }

    /**
     * Serializes the current state of the key into JSON format.
     *
     * @return JSONObject representing the key's current state.
     */
    @Override
    public JSONObject serialize() {
        JSONObject object = super.serialize();
        object.put("keyColor", this.color.name());
        return object;
    }

    /**
     * Provides the image path for a given key color.
     */
    private static String getImagePathForColor(final KeyColor color) {
        return switch (color) {
            case RED -> "/images/chipgame/collectibles/keys/redKey.png";
            case GREEN -> "/images/chipgame/collectibles/keys/greenKey.png";
            case YELLOW -> "/images/chipgame/collectibles/keys/yellowKey.png";
            case BLUE -> "/images/chipgame/collectibles/keys/blueKey.png";
        };
    }

    /**
     * Gets the color of the key.
     *
     * @return The color of the key.
     */
    public KeyColor getColor() {
        return color;
    }
}
