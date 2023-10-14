package com.group4.chipgame;

import com.group4.chipgame.actors.*;
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
        put("I", (x, y) -> new Wall());
        // Add more tiles as needed
    }};

    private final Map<String, BiFunction<Integer, Integer, Actor>> actorCreators = new HashMap<>() {{
        put("Player", Player::new);
        put("Creeper", Creeper::new);
        put("MovableBlock", MovableBlock::new);
        // Add more actors as needed
    }};

    public Tile[][] loadTiles(String levelFilePath) throws IOException {
        JSONObject levelData = loadJsonFromResource(levelFilePath);
        JSONArray tilesArray = levelData.getJSONArray("tiles");

        int width = tilesArray.getJSONArray(0).length();
        int height = tilesArray.length();

        Tile[][] levelTiles = new Tile[height][width];

        for (int y = 0; y < height; y++) {
            JSONArray row = tilesArray.getJSONArray(y);
            for (int x = 0; x < width; x++) {
                String tileType = row.getString(x);
                levelTiles[y][x] = tileCreators.getOrDefault(tileType, (a, b) -> null).apply(x, y);
            }
        }
        return levelTiles;
    }

    public List<Actor> loadActors(String levelFilePath) throws IOException {
        JSONObject levelData = loadJsonFromResource(levelFilePath);
        JSONArray actorsArray = levelData.getJSONArray("actors");

        List<Actor> actors = new ArrayList<>();

        for (int i = 0; i < actorsArray.length(); i++) {
            JSONObject actorData = actorsArray.getJSONObject(i);
            String type = actorData.getString("type");
            int x = actorData.getInt("x");
            int y = actorData.getInt("y");

            actors.add(actorCreators.getOrDefault(type, (a, b) -> null).apply(x, y));
        }

        return actors;
    }
}
