package com.group4.chipgame;

import com.group4.chipgame.actors.*;
import com.group4.chipgame.collectibles.Collectible;
import com.group4.chipgame.collectibles.Key;
import com.group4.chipgame.tiles.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.BiFunction;

public class LevelLoader {
    private static final String BUTTON_PREFIX = "B_";
    private static final String TRAP_PREFIX = "T_";

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

    }};

    private final Map<String, BiFunction<Integer, Integer, Collectible>> collectibleCreators = new HashMap<>() {{
        put("Key", (x, y) -> new Key(Key.KeyColor.BLUE, x, y));
    }};

    public List<Collectible> loadCollectibles(String levelFilePath, LevelRenderer levelRenderer) throws IOException {
        JSONObject levelData = loadJsonFromResource(levelFilePath);
        return levelData.has("collectibles")
                ? createEntities(levelData.getJSONArray("collectibles"), collectibleCreators, levelRenderer.getTiles())
                : new ArrayList<>();
    }

    public List<Actor> loadActors(String levelFilePath, LevelRenderer levelRenderer) throws IOException {
        JSONObject levelData = loadJsonFromResource(levelFilePath);
        return createEntities(levelData.getJSONArray("actors"), actorCreators, levelRenderer.getTiles());
    }

    public Tile[][] loadTiles(String levelFilePath) throws IOException {
        JSONObject levelData = loadJsonFromResource(levelFilePath);
        JSONArray tilesArray = levelData.getJSONArray("tiles");
        int width = tilesArray.getJSONArray(0).length();
        int height = tilesArray.length();
        Tile[][] levelTiles = new Tile[height][width];
        Map<String, Button> buttonMap = createButtons(tilesArray, levelTiles);
        createTraps(tilesArray, levelTiles, buttonMap);

        return levelTiles;
    }

    private Map<String, Button> createButtons(JSONArray tilesArray, Tile[][] levelTiles) {
        Map<String, Button> buttonMap = new HashMap<>();
        iterateTiles(tilesArray, (x, y, tileType) -> {
            if (tileType.startsWith(BUTTON_PREFIX)) {
                Button button = new Button();
                levelTiles[y][x] = button;
                buttonMap.put(tileType, button);
            } else {
                levelTiles[y][x] = Optional.ofNullable(tileCreators.get(tileType)).orElse((a, b) -> null).apply(x, y);
            }
        });
        return buttonMap;
    }

    private void createTraps(JSONArray tilesArray, Tile[][] levelTiles, Map<String, Button> buttonMap) {
        iterateTiles(tilesArray, (x, y, tileType) -> {
            if (tileType.startsWith(TRAP_PREFIX)) {
                String[] trapNumbers = tileType.split("_");
                for (String trapNumber : trapNumbers) {
                    if (!trapNumber.equals(TRAP_PREFIX.replace("_", ""))) {
                        Button linkedButton = buttonMap.get(BUTTON_PREFIX + trapNumber);
                        if (linkedButton != null) {
                            levelTiles[y][x] = new Trap(linkedButton);
                        }
                    }
                }
            }
        });
    }

    private <T> List<T> createEntities(JSONArray dataArray, Map<String, BiFunction<Integer, Integer, T>> creators, Tile[][] tiles) {
        List<T> entities = new ArrayList<>();
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject data = dataArray.getJSONObject(i);
            String type = data.getString("type");
            int x = data.getInt("x");
            int y = data.getInt("y");

            T entity = Optional.ofNullable(creators.get(type)).orElse((a, b) -> null).apply(x, y);
            if (entity != null) {
                entities.add(entity);
                if (tiles[y][x] != null) {
                    tiles[y][x].setOccupiedBy((Entity) entity);
                }
            }
        }
        return entities;
    }

    private void iterateTiles(JSONArray tilesArray, TileIterator iterator) {
        for (int y = 0; y < tilesArray.length(); y++) {
            JSONArray row = tilesArray.getJSONArray(y);
            for (int x = 0; x < row.length(); x++) {
                String tileType = row.getString(x);
                iterator.execute(x, y, tileType);
            }
        }
    }

    private interface TileIterator {
        void execute(int x, int y, String tileType);
    }
}
