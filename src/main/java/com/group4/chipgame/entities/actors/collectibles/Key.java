package com.group4.chipgame.entities.actors.collectibles;

import org.json.JSONObject;

public class Key extends Collectible {

    public enum KeyColor {
        RED, GREEN, YELLOW, BLUE
    }

    private final KeyColor color;

    public Key(KeyColor color, double gridX, double gridY) {
        super(getImagePathForColor(color), gridX, gridY);
        this.color = color;
    }

    @Override
    public JSONObject serialize() {
        JSONObject object = super.serialize();
        object.put("keyColor", this.color.name());
        return object;
    }


    private static String getImagePathForColor(KeyColor color) {
        return switch (color) {
            case RED -> "/images/chipgame/collectibles/keys/redKey.png";
            case GREEN -> "/images/chipgame/collectibles/keys/greenKey.png";
            case YELLOW -> "/images/chipgame/collectibles/keys/yellowKey.png";
            case BLUE -> "/images/chipgame/collectibles/keys/blueKey.png";
        };
    }

    public KeyColor getColor() {
        return color;
    }
}
