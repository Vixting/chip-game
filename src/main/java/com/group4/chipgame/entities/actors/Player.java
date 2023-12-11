package com.group4.chipgame.entities.actors;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Level.LevelRenderer;
import com.group4.chipgame.entities.actors.collectibles.Collectible;
import com.group4.chipgame.entities.actors.collectibles.Key;
import com.group4.chipgame.entities.actors.tiles.ChipSocket;
import com.group4.chipgame.entities.actors.tiles.Ice;
import com.group4.chipgame.entities.actors.tiles.LockedDoor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.EnumSet;
import java.util.Set;

/**
 * Represents the player character in the ChipGame.
 * The player can move around the level,
 * collect items, and interact with various tiles.
 * @author William Buckley
 */
public class Player extends Actor {
    private static final String PLAYER_IMAGE_PATH =
            "/images/chipgame/actors/steve.png";

    private boolean isAlive = true;
    private int chipsCount;
    private final Set<Key.KeyColor> collectedKeys =
            EnumSet.noneOf(Key.KeyColor.class);
    private static final int MOVE_INTERVAL = 10;

    /**
     * Constructs a Player with a specified initial position.
     *
     * @param x The initial x-coordinate of the player.
     * @param y The initial y-coordinate of the player.
     */
    public Player(final double x,
                  final double y) {
        super(PLAYER_IMAGE_PATH, x, y);
        this.setMoveInterval(MOVE_INTERVAL);
    }

    /**
     * Adds a key to the player's collection.
     *
     * @param key The key to add.
     */
    public void addKey(final Key key) {
        collectedKeys.add(key.getColor());
    }

    /**
     * Checks if the player has a key of a specific color.
     *
     * @param keyColor The color of the key to check.
     * @return True if the player has the key, false otherwise.
     */
    public boolean hasKey(final Key.KeyColor keyColor) {
        return collectedKeys.contains(keyColor);
    }

    /**
     * Moves the player by a specified delta in x and y direction,
     * and processes interactions with tiles. Movement is restricted
     * on ice tiles.
     *
     * @param dx            The change in the x-coordinate.
     * @param dy            The change in the y-coordinate.
     * @param levelRenderer The renderer for the game level.
     */
    @Override
    public void move(final double dx,
                     final double dy,
                     final LevelRenderer levelRenderer) {

        if (isMoving() || isOnIce(levelRenderer)) {
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
     * Checks if the player is currently on an ice tile.
     */
    private boolean isOnIce(final LevelRenderer levelRenderer) {
        return levelRenderer.getTileAtGridPosition((int) getCurrentPosition().getX(),
                        (int) getCurrentPosition().getY())
                .map(tile -> tile instanceof Ice)
                .orElse(false);
    }

    /**
     * Adds a specified number of chips to the player's chip count.
     *
     * @param count The number of chips to add.
     */
    public void addChips(final int count) {
        this.chipsCount += count;
        System.out.println("Chips count: " + this.chipsCount);
    }

    /**
     * Consumes a specified number of chips from the player's chip count.
     *
     * @param count The number of chips to consume.
     */
    public void consumeChips(final int count) {
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
     */
    private void processTileInteraction(
            final double newX,
            final double newY,
            final double dx,
            final double dy,
            final LevelRenderer levelRenderer,
            final Direction direction) {
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
     * Checks for collectibles at the specified position
     * and collects them if present.
     *
     * @param x             The x-coordinate to check for collectibles.
     * @param y             The y-coordinate to check for collectibles.
     * @param levelRenderer The renderer for the game level.
     */
    public void checkForCollectibles(final double x,
                                     final double y,
                                     final LevelRenderer levelRenderer) {
        levelRenderer.getTileAtGridPosition(
                (int) x, (int) y)
                .ifPresent(tile -> {
                    if (tile.getOccupiedBy()
                            instanceof Collectible collectible) {
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
    public void kill(final LevelRenderer levelRenderer) {
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
     */
    private void onCollect(final Entity collectible,
                           final LevelRenderer levelRenderer) {
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
    public void setChipsCount(final int chipsCount) {
        this.chipsCount = chipsCount;
    }

    /**
     * Sets the player's alive status.
     *
     * @param isAlive The alive status to set.
     */
    public void setAlive(final boolean isAlive) {
        this.isAlive = isAlive;
    }
}
