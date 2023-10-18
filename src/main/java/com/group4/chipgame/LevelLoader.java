package com.group4.chipgame;

import com.group4.chipgame.actors.*;
import com.group4.chipgame.collectibles.Key;
import com.group4.chipgame.tiles.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class LevelLoader {
    private JSONObject loadJsonFromResource(String path) throws IOException {
        try (InputStream resourceStream = Main.class.getResourceAsStream(path)) {
            if (resourceStream == null) {
                throw new IOException("Resource not found: " + path);
            }
            return new JSONObject(new String(resourceStream.readAllBytes()));
        }
    }

    private final Map<String, BiFunction<Integer, Integer, Tile>> tileCreators = new HashMap<>() {{
        put("P", (x, y) -> new Path());
        put("W", (x, y) -> new Water());
        put("G", (x, y) -> new Wall());
        put("D", (x, y) -> new LockedDoor(Key.KeyColor.BLUE));
        put("S", (x, y) -> new Dirt());

        put("I", (x, y) -> new Ice(Direction.Corner.NONE ));
        put("I_BL", (x, y) -> new Ice(Direction.Corner.BOTTOM_LEFT));
        put("I_BR", (x, y) -> new Ice(Direction.Corner.BOTTOM_RIGHT));
        put("I_TL", (x, y) -> new Ice(Direction.Corner.TOP_LEFT));
        put("I_TR", (x, y) -> new Ice(Direction.Corner.TOP_RIGHT));

        put("Button", (x, y) -> new Button());
    }};


    private final Map<String, BiFunction<Integer, Integer, Actor>> actorCreators = new HashMap<>() {{
        put("Player", Player::new);
        put("Creeper", Creeper::new);
        put("MovableBlock", MovableBlock::new);

        put("Key", (x, y) -> new Key(Key.KeyColor.BLUE, x, y));

        // Add more actors as needed
    }};

    public Tile[][] loadTiles(String levelFilePath) throws IOException {
        JSONObject levelData = loadJsonFromResource(levelFilePath);
        JSONArray tilesArray = levelData.getJSONArray("tiles");

        int width = tilesArray.getJSONArray(0).length();
        int height = tilesArray.length();

        Tile[][] levelTiles = new Tile[height][width];
        Map<String, Button> buttonMap = new HashMap<>();

        for (int y = 0; y < height; y++) {
            JSONArray row = tilesArray.getJSONArray(y);
            for (int x = 0; x < width; x++) {
                String tileType = row.getString(x);
                if (tileType.startsWith("B_")) {
                    Button button = new Button();
                    levelTiles[y][x] = button;
                    buttonMap.put(tileType, button);
                } else {
                    levelTiles[y][x] = tileCreators.getOrDefault(tileType, (a, b) -> null).apply(x, y);
                }
            }
        }

        for (int y = 0; y < height; y++) {
            JSONArray row = tilesArray.getJSONArray(y);
            for (int x = 0; x < width; x++) {
                String tileType = row.getString(x);
                if (tileType.startsWith("T_")) {
                    String buttonKey = "B" + tileType.substring(1);
                    Button linkedButton = buttonMap.get(buttonKey);
                    if (linkedButton != null) {
                        levelTiles[y][x] = new Trap(linkedButton);
                    }
                }
            }
        }

        return levelTiles;
    }

    public List<Actor> loadActors(String levelFilePath, LevelRenderer levelRenderer) throws IOException {
        JSONObject levelData = loadJsonFromResource(levelFilePath);
        JSONArray actorsArray = levelData.getJSONArray("actors");

        List<Actor> actors = new ArrayList<>();

        Tile[][] levelTiles = levelRenderer.getTiles();

        for (int i = 0; i < actorsArray.length(); i++) {
            JSONObject actorData = actorsArray.getJSONObject(i);
            String type = actorData.getString("type");
            int x = actorData.getInt("x");
            int y = actorData.getInt("y");

            Actor actor = actorCreators.getOrDefault(type, (a, b) -> null).apply(x, y);
            if (actor != null) {
                actors.add(actor);

                if (levelTiles[y][x] != null) {
                    levelTiles[y][x].setOccupiedBy(actor);
                    System.out.println("Setting tile (" + x + ", " + y + ") as occupied by " + type + ".");
                }
            }
        }
        return actors;
    }
}
