package com.group4.chipgame.Level.saving;

import com.group4.chipgame.entities.actors.*;
import com.group4.chipgame.Direction;
import com.group4.chipgame.entities.actors.collectibles.Key;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A factory class for creating Actor instances from JSON data.
 * This class is responsible for instantiating different types of Actor objects based on JSON input.
 */
public class ActorFactory {

    /**
     * Creates an Actor object from the provided JSON data.
     * The specific type of Actor (e.g., Player, Bug, Frog, etc.) is determined by the 'type' field in the JSON object.
     *
     * @param actorJson The JSON object containing the data for the actor.
     * @return An instance of an Actor, as specified in the JSON object.
     * @throws IllegalStateException If the actor type is not recognized.
     */
    public static Actor createActor(JSONObject actorJson) {
        String type = actorJson.getString("type");
        double x = actorJson.getDouble("x");
        double y = actorJson.getDouble("y");

        return switch (type) {
            case "Player" -> createPlayer(actorJson);
            case "Bug" -> new Bug(x, y, actorJson.getBoolean("followLeftEdge"));
            case "Frog" -> new Frog(x, y);
            case "MovableBlock" -> new MovableBlock(x, y);
            case "PinkBall" -> new PinkBall(x, y, Direction.valueOf(actorJson.getString("initialDirection")));
            default -> throw new IllegalStateException("Unexpected actor type: " + type);
        };
    }

    /**
     * Creates a Player object from the provided JSON data.
     * This method specifically handles the instantiation of a Player, including setting its properties based on the JSON object.
     *
     * @param actorJson The JSON object containing the data for the player.
     * @return An instance of a Player, with properties set as per the JSON object.
     */
    private static Player createPlayer(JSONObject actorJson) {
        Player player = new Player(actorJson.getDouble("x"), actorJson.getDouble("y"));
        player.setAlive(actorJson.getBoolean("isAlive"));
        player.setChipsCount(actorJson.getInt("chipsCount"));

        JSONArray keysArray = actorJson.getJSONArray("collectedKeys");
        for (int i = 0; i < keysArray.length(); i++) {
            String keyColorStr = keysArray.getString(i);
            Key.KeyColor keyColor = Key.KeyColor.valueOf(keyColorStr);
            player.addKey(new Key(keyColor, 0, 0));
        }
        return player;
    }
}
