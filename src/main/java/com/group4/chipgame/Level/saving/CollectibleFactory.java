package com.group4.chipgame.Level.saving;

import com.group4.chipgame.entities.actors.collectibles.Chip;
import com.group4.chipgame.entities.actors.collectibles.Collectible;
import com.group4.chipgame.entities.actors.collectibles.Key;
import org.json.JSONObject;

/**
 * A factory class for creating Collectible instances from JSON data.
 * This class is responsible for instantiating different types of Collectible objects based on JSON input.
 */
public class CollectibleFactory {

    /**
     * Creates a Collectible object from the provided JSON data.
     * The specific type of Collectible (e.g., Key, Chip, etc.) is determined by the 'type' field in the JSON object.
     *
     * @param collectibleJson The JSON object containing the data for the collectible.
     * @return An instance of a Collectible, as specified in the JSON object.
     * @throws IllegalArgumentException If the collectible type is not recognized or other data is invalid.
     */
    public static Collectible createCollectible(JSONObject collectibleJson) {
        String type = collectibleJson.optString("type", "Unknown");
        double x = collectibleJson.optDouble("x", -1);
        double y = collectibleJson.optDouble("y", -1);

        return switch (type) {
            case "Key" -> createKey(collectibleJson, x, y);
            case "Chip" -> new Chip(x, y);
            default -> new Collectible(collectibleJson.optString("imagePath", "default/path.jpg"), x, y);
        };
    }

    /**
     * Creates a Key object from the provided JSON data.
     * This method specifically handles the instantiation of a Key,
     * including setting its properties based on the JSON object.
     *
     * @param collectibleJson The JSON object containing the data for the key.
     * @param x               The x-coordinate for the key's position.
     * @param y               The y-coordinate for the key's position.
     * @return An instance of a Key, with properties set as per the JSON object.
     * @throws IllegalArgumentException If the key color specified in the JSON is invalid.
     */
    private static Key createKey(JSONObject collectibleJson, double x, double y) {
        String keyColorStr = collectibleJson.optString("keyColor", "BLUE");
        try {
            Key.KeyColor keyColor = Key.KeyColor.valueOf(keyColorStr);
            return new Key(keyColor, x, y);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid key color: " + keyColorStr);
            return null;
        }
    }
}
