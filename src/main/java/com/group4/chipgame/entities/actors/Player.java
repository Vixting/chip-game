package com.group4.chipgame.entities.actors;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Level.LevelRenderer;
import com.group4.chipgame.entities.actors.collectibles.Collectible;
import com.group4.chipgame.entities.actors.collectibles.Key;
import com.group4.chipgame.entities.actors.tiles.ChipSocket;
import com.group4.chipgame.entities.actors.tiles.LockedDoor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.EnumSet;
import java.util.Set;

/**
 * Represents the player character in the ChipGame.
 * The player can move around the level, collect items, and interact with various tiles.
 */
public class Player extends Actor {
    private static final String PLAYER_IMAGE_PATH = "/images/chipgame/actors/steve.png";

    private boolean isAlive = true;
    private int chipsCount;
    private final Set<Key.KeyColor> collectedKeys = EnumSet.noneOf(Key.KeyColor.class);
    private static final int MOVE_INTERVAL = 10;

    /**
     * Constructs a Player with a specified initial position.
     *
     * @param x The initial x-coordinate of the player.
     * @param y The initial y-coordinate of the player.
     */
    public Player(double x, double y) {
        super(PLAYER_IMAGE_PATH, x, y);
        this.setMoveInterval(MOVE_INTERVAL);
    }

    /**
     * Adds a key to the player's collection.
     *
     * @param key The key to add.
     */
    public void addKey(Key key) {
        collectedKeys.add(key.getColor());
    }

    /**
     * Checks if the player has a key of a specific color.
     *
     * @param keyColor The color of the key to check.
     * @return True if the player has the key, false otherwise.
     */
    public boolean hasKey(Key.KeyColor keyColor) {
        return collectedKeys.contains(keyColor);
    }

    /**
     * Moves the player by a specified delta in x and y direction, and processes interactions with tiles.
     *
     * @param dx            The change in the x-coordinate.
     * @param dy            The change in the y-coordinate.
     * @param levelRenderer The renderer for the game level.
     */
    @Override
    public void move(double dx, double dy, LevelRenderer levelRenderer) {
        if (isMoving()) {
            return;
        }

        Direction direction = Direction.fromDelta(dx, dy);
        double newX = getCurrentPosition().getX() + dx;
        double newY = getCurrentPosition().getY() + dy;

        processTileInteraction(newX, newY, dx, dy, levelRenderer, direction);

        if (canMove(dx, dy, levelRenderer)) {
            checkForCollectibles(newX, newY, levelRenderer);
            performMove(newX, newY, levelRenderer, direction);
        }
    }

    /**
     * Adds a specified number of chips to the player's chip count.
     *
     * @param count The number of chips to add.
     */
    public void addChips(int count) {
        this.chipsCount += count;
        System.out.println("Chips count: " + this.chipsCount);
    }

    /**
     * Consumes a specified number of chips from the player's chip count.
     *
     * @param count The number of chips to consume.
     */
    public void consumeChips(int count) {
        this.chipsCount -= count;
    }

    /**
     * Gets the player's current chip count.
     *
     * @return The number of chips the player currently has.
     */
    public int getChipsCount() {
        return this.chipsCount;
    }

    /**
     * Processes interactions when the player moves to a new tile.
     *
     * @param newX          The new x-coordinate.
     * @param newY          The new y-coordinate.
     * @param dx            The change in the x-coordinate.
     * @param dy            The change in the y-coordinate.
     * @param levelRenderer The renderer for the game level.
     * @param direction     The direction of the move.
     */
    private void processTileInteraction(
            double newX,
            double newY,
            double dx,
            double dy,
            LevelRenderer levelRenderer,
            Direction direction) {
        levelRenderer.getTileAtGridPosition((int) newX, (int) newY)
                .ifPresent(tile -> {
                    if (tile.getOccupiedBy() instanceof MovableBlock block) {
                        block.push(dx, dy, levelRenderer);
                    } else if (tile instanceof LockedDoor door) {
                        door.onStep(this, levelRenderer, direction);
                    } else if (tile instanceof ChipSocket chipSocket) {
                        chipSocket.onStep(this, levelRenderer, direction);
                    }
                });
    }

    /**
     * Checks for collectibles at the specified position and collects them if present.
     *
     * @param x             The x-coordinate to check for collectibles.
     * @param y             The y-coordinate to check for collectibles.
     * @param levelRenderer The renderer for the game level.
     */
    public void checkForCollectibles(double x, double y, LevelRenderer levelRenderer) {
        levelRenderer.getTileAtGridPosition((int) x, (int) y)
                .ifPresent(tile -> {
                    if (tile.getOccupiedBy() instanceof Collectible collectible) {
                        collectible.onCollect(this);
                        levelRenderer.remove(collectible);
                        tile.setOccupiedBy(null);
                        onCollect(collectible, levelRenderer);
                    }
                });
    }

    /**
     * Kills the player and performs necessary cleanup.
     *
     * @param levelRenderer The renderer for the game level.
     */
    public void kill(LevelRenderer levelRenderer) {
        isAlive = false;
        levelRenderer.remove(this);
        levelRenderer.getGamePane().getChildren().remove(this);
    }

    /**
     * Checks if the player is alive.
     *
     * @return True if the player is alive, false otherwise.
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Processes the collection of an entity, such as a key.
     *
     * @param collectible   The entity being collected.
     * @param levelRenderer The renderer for the game level.
     */
    private void onCollect(Entity collectible, LevelRenderer levelRenderer) {
        if (collectible instanceof Key key) {
            levelRenderer.remove(key);
            addKey(key);
        }
    }

    /**
     * Serializes the state of the player to a JSON object.
     *
     * @return A JSONObject representing the current state of the player.
     */
    public JSONObject serialize() {
        JSONObject json = new JSONObject();
        json.put("type", "Player");
        json.put("x", getCurrentPosition().getX());
        json.put("y", getCurrentPosition().getY());
        json.put("isAlive", isAlive);
        json.put("chipsCount", chipsCount);

        JSONArray keysArray = new JSONArray();
        for (Key.KeyColor keyColor : collectedKeys) {
            keysArray.put(keyColor.name());
        }
        json.put("collectedKeys", keysArray);

        return json;
    }

    /**
     * Sets the player's chip count.
     *
     * @param chipsCount The new chip count.
     */
    public void setChipsCount(int chipsCount) {
        this.chipsCount = chipsCount;
    }

    /**
     * Sets the player's alive status.
     *
     * @param isAlive The alive status to set.
     */
    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }
}
