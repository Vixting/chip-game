package com.group4.chipgame.collectibles;

/**
 * The Key class represents a key collectible in the game with various colors.
 * Each key has an associated color, and the image path is determined by the color.
 */
public class Key extends Collectible {

    /**
     * Enumeration representing possible key colors.
     */
    public enum KeyColor {
        RED, GREEN, YELLOW, BLUE
    }

    private final KeyColor color;

    /**
     * Constructs a Key object with the specified color and grid position.
     *
     * @param color The color of the key.
     * @param gridX The x-coordinate of the key on the grid.
     * @param gridY The y-coordinate of the key on the grid.
     */
    public Key(KeyColor color, double gridX, double gridY) {
        super(getImagePathForColor(color), gridX, gridY);
        this.color = color;
    }

    /**
     * Gets the image path for the specified key color.
     *
     * @param color The color of the key.
     * @return The image path for the key color.
     */
    private static String getImagePathForColor(KeyColor color) {
        return switch (color) {
            case RED -> "/images/chipgame/collectibles/key_red.jpg";
            case GREEN -> "/images/chipgame/collectibles/key_green.jpg";
            case YELLOW -> "/images/chipgame/collectibles/key_yellow.jpg";
            case BLUE -> "/images/chipgame/collectibles/keys/key.jpg";
            default -> "/images/chipgame/collectibles/keys/key.jpg";
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
