package com.group4.chipgame.collectibles;

public class Key extends Collectible {

    public enum KeyColor {
        RED, GREEN, YELLOW, BLUE
    }

    private final KeyColor color;

    public Key(KeyColor color, double gridX, double gridY) {
        super(getImagePathForColor(color), gridX, gridY);
        this.color = color;
    }

    private static String getImagePathForColor(KeyColor color) {
        return switch (color) {
            case RED -> "/images/chipgame/collectibles/key_red.jpg";
            case GREEN -> "/images/chipgame/collectibles/key_green.jpg";
            case YELLOW -> "/images/chipgame/collectibles/key_yellow.jpg";
            case BLUE -> "/images/chipgame/collectibles/keys/key.jpg";
            default -> "/images/chipgame/collectibles/keys/key.jpg";
        };
    }

    public KeyColor getColor() {
        return color;
    }
}
