package com.group4.chipgame;

import com.group4.chipgame.tiles.Tile;
import com.group4.chipgame.tiles.Path;
import com.group4.chipgame.tiles.Water;
import com.group4.chipgame.actors.Actor;
import com.group4.chipgame.actors.Player;
import com.group4.chipgame.actors.Creeper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LevelLoader {
    private JSONObject loadJsonFromResource(String path) throws IOException {
        try (InputStream resourceStream = Main.class.getResourceAsStream(path)) {
            if (resourceStream == null) {
                throw new IOException("Resource not found: " + path);
            }
            String content = new String(resourceStream.readAllBytes());
            return new JSONObject(content);
        }
    }

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
                switch (tileType) {
                    case "P" -> levelTiles[y][x] = new Path();
                    case "W" -> levelTiles[y][x] = new Water();
                    // Add more cases as needed
                }
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

            if ("Player".equals(type)) {
                System.out.println("Player found at " + x + ", " + y);
                actors.add(new Player(x, y));
            } else if ("Creeper".equals(type)) {
                actors.add(new Creeper(x, y));

            }
        }

        return actors;
    }
}