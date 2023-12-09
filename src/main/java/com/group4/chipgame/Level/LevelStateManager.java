package com.group4.chipgame.Level;

import com.group4.chipgame.Level.saving.ActorFactory;
import com.group4.chipgame.Level.saving.CollectibleFactory;
import com.group4.chipgame.Level.saving.TileFactory;
import com.group4.chipgame.entities.actors.Actor;
import com.group4.chipgame.entities.actors.collectibles.Collectible;
import com.group4.chipgame.entities.actors.tiles.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelStateManager {

    public static void saveLevel(LevelData levelData, String filePath) throws IOException {
        JSONObject levelState = new JSONObject();
        JSONArray tilesArray = new JSONArray();
        JSONArray actorsArray = new JSONArray();
        JSONArray collectiblesArray = new JSONArray();

        serializeTiles(levelData, tilesArray);
        serializeActors(levelData, actorsArray);
        serializeCollectibles(levelData, collectiblesArray);

        levelState.put("timer", levelData.getTimer());
        levelState.put("levelPath", levelData.getLevelPath());
        levelState.put("tiles", tilesArray);
        levelState.put("actors", actorsArray);
        levelState.put("collectibles", collectiblesArray);

        saveToFile(filePath, levelState.toString(4));
    }

    private static void serializeTiles(LevelData levelData, JSONArray tilesArray) {
        for (Tile[] row : levelData.getTiles()) {
            JSONArray rowArray = new JSONArray();
            for (Tile tile : row) {
                JSONObject tileJson = (tile != null) ? tile.serialize() : new JSONObject().put("type", "Empty");
                rowArray.put(tileJson);
            }
            tilesArray.put(rowArray);
        }
    }

    private static void serializeActors(LevelData levelData, JSONArray actorsArray) {
        for (Actor actor : levelData.getActors()) {
            actorsArray.put(actor.serialize());
        }
    }

    private static void serializeCollectibles(LevelData levelData, JSONArray collectiblesArray) {
        for (Collectible collectible : levelData.getCollectibles()) {
            collectiblesArray.put(collectible.serialize());
        }
    }

    private static void saveToFile(String filePath, String content) throws IOException {
        Path path = Paths.get(filePath);
        Files.deleteIfExists(path);
        Files.writeString(path, content);
    }

    public static LevelData loadLevel(String filePath, LevelRenderer renderer) throws IOException {
        String fileContent = Files.readString(Paths.get(filePath));
        JSONObject levelState = new JSONObject(fileContent);

        int timer = levelState.optInt("timer", 60);
        String levelFilePath = levelState.optString("levelPath", "null");

        JSONArray tilesArray = levelState.getJSONArray("tiles");
        JSONArray actorsArray = levelState.getJSONArray("actors");
        JSONArray collectiblesArray = levelState.getJSONArray("collectibles");

        int numRows = tilesArray.length();
        int numCols = numRows > 0 ? tilesArray.getJSONArray(0).length() : 0;

        Tile[][] tiles = new Tile[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            JSONArray rowArray = tilesArray.getJSONArray(i);
            for (int j = 0; j < rowArray.length(); j++) {
                JSONObject tileJson = rowArray.getJSONObject(j);
                tiles[i][j] = TileFactory.createTile(tileJson);
            }
        }
        List<Actor> actors = new ArrayList<>();
        for (int i = 0; i < actorsArray.length(); i++) {
            JSONObject actorJson = actorsArray.getJSONObject(i);
            actors.add(ActorFactory.createActor(actorJson));
        }

        List<Collectible> collectibles = new ArrayList<>();
        for (int i = 0; i < collectiblesArray.length(); i++) {
            JSONObject collectibleJson = collectiblesArray.getJSONObject(i);
            collectibles.add(CollectibleFactory.createCollectible(collectibleJson));
        }

        return new LevelData(tiles, numCols, numRows, actors, collectibles, renderer, levelFilePath, timer);
    }

}
