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

public class Player extends Actor {
    private static final String PLAYER_IMAGE_PATH = "/images/chipgame/actors/steve.png";

    private boolean isAlive = true;
    private int chipsCount;
    private final Set<Key.KeyColor> collectedKeys = EnumSet.noneOf(Key.KeyColor.class);
    private static final int MOVE_INTERVAL = 10;

    public Player(double x, double y) {
        super(PLAYER_IMAGE_PATH, x, y);
        this.moveInterval = MOVE_INTERVAL;
    }

    public void addKey(Key key) {
        collectedKeys.add(key.getColor());
    }

    public boolean hasKey(Key.KeyColor keyColor) {
        return collectedKeys.contains(keyColor);
    }

    @Override
    public void move(double dx, double dy, LevelRenderer levelRenderer) {
        if (isMoving) return;

        Direction direction = Direction.fromDelta(dx, dy);
        double newX = getCurrentPosition().getX() + dx;
        double newY = getCurrentPosition().getY() + dy;

        processTileInteraction(newX, newY, dx, dy, levelRenderer, direction);

        if (canMove(dx, dy, levelRenderer)) {
            checkForCollectibles(newX, newY, levelRenderer);
            performMove(newX, newY, levelRenderer, direction);
        }
    }

    public void addChips(int count) {
        this.chipsCount += count;
        System.out.println("Chips count: " + this.chipsCount);
    }

    public void consumeChips(int count) {
        this.chipsCount -= count;
    }

    public int getChipsCount() {
        return this.chipsCount;
    }

    private void processTileInteraction(double newX, double newY, double dx, double dy, LevelRenderer levelRenderer, Direction direction) {
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

    public void kill(LevelRenderer levelRenderer) {
        isAlive = false;
        levelRenderer.remove(this);
        levelRenderer.getGamePane().getChildren().remove(this);
    }

    public boolean isAlive() {
        return isAlive;
    }

    private void onCollect(Entity collectible, LevelRenderer levelRenderer) {
        if (collectible instanceof Key key) {
            levelRenderer.remove(key);
            addKey(key);
        }
    }

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

    public void setChipsCount(int chipsCount) {
        this.chipsCount = chipsCount;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }
}
