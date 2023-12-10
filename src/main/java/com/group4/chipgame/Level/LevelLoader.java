package com.group4.chipgame.Level;

import com.group4.chipgame.Direction;
import com.group4.chipgame.entities.actors.Entity;
import com.group4.chipgame.entities.actors.*;
import com.group4.chipgame.entities.actors.collectibles.Chip;
import com.group4.chipgame.entities.actors.collectibles.Collectible;
import com.group4.chipgame.entities.actors.collectibles.Key;
import com.group4.chipgame.entities.actors.tiles.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiFunction;

/**
 * A class for loading level data from a JSON file.
 * This class is responsible for parsing level data and instantiating corresponding game entities.
 */
public class LevelLoader {
    private static final String BUTTON_PREFIX = "B_";
    private static final String TRAP_PREFIX = "T_";
    private static final int CHIPSOCKET = 3;

    /**
     * Loads JSON content from a file and parses it into a JSONObject.
     *
     * @param path The path to the file containing JSON data.
     * @return A JSONObject representing the parsed data.
     * @throws IOException If an error occurs while reading the file.
     */
    private JSONObject loadJsonFromFile(String path) throws IOException {
        String content = Files.readString(Paths.get(path));
        return new JSONObject(content);
    }

    private final Map<String, BiFunction<Integer, Integer, Tile>> tileCreators = new HashMap<>() {{
        put("P", (x, y) -> new Path());
        put("W", (x, y) -> new Water());
        put("G", (x, y) -> new Wall());
        put("E", (x, y) -> new Exit());
        put("S", (x, y) -> new Dirt());

        put("RD", (x, y) -> new LockedDoor(Key.KeyColor.RED));
        put("GD", (x, y) -> new LockedDoor(Key.KeyColor.GREEN));
        put("YD", (x, y) -> new LockedDoor(Key.KeyColor.YELLOW));
        put("BD", (x, y) -> new LockedDoor(Key.KeyColor.BLUE));

        put("I", (x, y) -> new Ice(Direction.Corner.NONE));
        put("I_BL", (x, y) -> new Ice(Direction.Corner.BOTTOM_LEFT));
        put("I_BR", (x, y) -> new Ice(Direction.Corner.BOTTOM_RIGHT));
        put("I_TL", (x, y) -> new Ice(Direction.Corner.TOP_LEFT));
        put("I_TR", (x, y) -> new Ice(Direction.Corner.TOP_RIGHT));
    }};

    private final Map<String, BiFunction<Integer, Integer, Actor>> actorCreators = new HashMap<>() {{
        put("Frog", Frog::new);
        put("Player", Player::new);
        put("MovableBlock", MovableBlock::new);
        put("RPinkBall", (x, y) -> new PinkBall(x, y, Direction.RIGHT));
        put("LPinkBall", (x, y) -> new PinkBall(x, y, Direction.LEFT));
        put("UPinkBall", (x, y) -> new PinkBall(x, y, Direction.UP));
        put("DPinkBall", (x, y) -> new PinkBall(x, y, Direction.DOWN));
        put("leftBug", (x, y) -> new Bug(x, y, true));
        put("rightBug", (x, y) -> new Bug(x, y, false));
    }};

    private final Map<String, BiFunction<Integer, Integer, Collectible>> collectibleCreators = new HashMap<>() {{
        put("redKey", (x, y) -> new Key(Key.KeyColor.RED, x, y));
        put("blueKey", (x, y) -> new Key(Key.KeyColor.BLUE, x, y));
        put("greenKey", (x, y) -> new Key(Key.KeyColor.GREEN, x, y));
        put("yellowKey", (x, y) -> new Key(Key.KeyColor.YELLOW, x, y));

        put("Chip", Chip::new);
    }};

    /**
     * Loads and creates actors based on the JSON data in a level file.
     *
     * @param levelFilePath The file path for the level data.
     * @param levelRenderer The renderer for the game level.
     * @return A list of created actors.
     * @throws IOException If an error occurs while reading the level file.
     */
    public List<Actor> loadActors(String levelFilePath, LevelRenderer levelRenderer) throws IOException {
        JSONObject levelData = loadJsonFromFile(levelFilePath);
        return createEntities(levelData.getJSONArray("actors"), actorCreators, levelRenderer.getTiles());
    }

    /**
     * Loads and creates collectibles based on the JSON data in a level file.
     *
     * @param levelFilePath The file path for the level data.
     * @param levelRenderer The renderer for the game level.
     * @return A list of created collectibles.
     * @throws IOException If an error occurs while reading the level file.
     */
    public List<Collectible> loadCollectibles(String levelFilePath, LevelRenderer levelRenderer) throws IOException {
        JSONObject levelData = loadJsonFromFile(levelFilePath);
        return createEntities(levelData.getJSONArray("collectibles"), collectibleCreators, levelRenderer.getTiles());
    }

    /**
     * Loads and creates tiles for the level based on the JSON data in a level file.
     *
     * @param levelFilePath The file path for the level data.
     * @return A 2D array of created tiles.
     * @throws IOException If an error occurs while reading the level file.
     */
    public Tile[][] loadTiles(String levelFilePath) throws IOException {
        JSONObject levelData = loadJsonFromFile(levelFilePath);
        JSONArray tilesArray = levelData.getJSONArray("tiles");
        int width = tilesArray.getJSONArray(0).length();
        int height = tilesArray.length();
        Tile[][] levelTiles = new Tile[height][width];
        Map<String, Button> tempButtonMap = createButtons(tilesArray, levelTiles);
        createTraps(tilesArray, levelTiles, tempButtonMap);
        return levelTiles;
    }

    /**
     * Iterates through each tile specified in the JSONArray and applies the given action.
     *
     * @param tilesArray The JSONArray containing tile data.
     * @param levelTiles   The action to perform on each tile.
     */
    private Map<String, Button> createButtons(JSONArray tilesArray, Tile[][] levelTiles) {
        Map<String, Button> tempButtonMap = new HashMap<>();
        iterateTiles(tilesArray, (x, y, tileType) -> {
            if (tileType.startsWith(BUTTON_PREFIX)) {
                String buttonId = tileType.substring(BUTTON_PREFIX.length());
                Button button = new Button(buttonId);
                button.setConnection(buttonId);
                levelTiles[y][x] = button;
                tempButtonMap.put(buttonId, button);
            } else {
                levelTiles[y][x] = Optional.ofNullable(tileCreators.get(tileType)).orElse((a, b) -> null).apply(x, y);
            }
        });
        return tempButtonMap;
    }

    private void createTraps(JSONArray tilesArray, Tile[][] levelTiles, Map<String, Button> tempButtonMap) {
        iterateTiles(tilesArray, (x, y, tileType) -> {
            if (tileType.startsWith(TRAP_PREFIX)) {
                String[] parts = tileType.split("_");
                if (parts.length > 1) {
                    String buttonId = parts[1];
                    Button linkedButton = tempButtonMap.get(buttonId);
                    if (linkedButton != null) {
                        Trap trap = new Trap(linkedButton, tileType);
                        trap.setConnection(buttonId);
                        levelTiles[y][x] = trap;
                    }
                }
            }
        });
    }

    private <T> List<T> createEntities(
                    JSONArray dataArray, Map<String,
                    BiFunction<Integer,
                    Integer,
                    T>> creators, Tile[][] tiles) {
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
                Object tileElement = row.get(x);
                String tileType;
                if (tileElement instanceof JSONObject tileJson) {
                    tileType = tileJson.getString("type");
                } else if (tileElement instanceof String) {
                    tileType = (String) tileElement;
                } else {
                    continue;
                }

                if (tileType.startsWith("CS_")) {
                    int requiredChips = Integer.parseInt(tileType.substring(CHIPSOCKET));
                    tileCreators.put(tileType, (tx, ty) -> new ChipSocket(requiredChips));
                }

                iterator.execute(x, y, tileType);
            }
        }
    }

    /**
     * Interface for performing actions on individual tiles during iteration.
     */
    private interface TileIterator {
        void execute(int x, int y, String tileType);
    }
}
