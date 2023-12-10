package com.group4.chipgame.entities.actors.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Level.LevelRenderer;
import com.group4.chipgame.entities.actors.Actor;
import com.group4.chipgame.entities.actors.Player;
import org.json.JSONObject;

/**
 * Represents a ChipSocket tile in the ChipGame.
 */
public class ChipSocket extends Tile {
    private static final String CHIP_SOCKET_IMAGE_PATH = "/images/chipgame/tiles/chipSocket.png";
    private final int requiredChips;

    /**
     * Constructs a ChipSocket with the specified number of required chips.
     *
     * @param requiredChips The number of chips required to activate this ChipSocket.
     */
    public ChipSocket(int requiredChips) {
        super(CHIP_SOCKET_IMAGE_PATH, false);
        this.requiredChips = requiredChips;
    }

    /**
     * Defines the action to be taken when an Actor steps on this ChipSocket.
     *
     * @param actor The Actor stepping on the tile.
     * @param levelRenderer The renderer for the level.
     * @param incomingDirection The direction from which the Actor steps onto the tile.
     */
    @Override
    public void onStep(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {
        if (actor instanceof Player player) {
            if (player.getChipsCount() >= requiredChips) {
                player.consumeChips(requiredChips);
                openSocket(levelRenderer);
            }
        }
    }

    /**
     * Opens the socket, changing its state in the level renderer.
     *
     * @param levelRenderer The renderer for the level.
     */
    private void openSocket(LevelRenderer levelRenderer) {
        levelRenderer.updateTile((int) this.getGridX(), (int) this.getGridY(), new Path());
    }

    /**
     * Serializes the ChipSocket into a JSONObject.
     *
     * @return A JSONObject representing the ChipSocket's state.
     */
    @Override
    public JSONObject serialize() {
        JSONObject json = super.serialize();
        json.put("requiredChips", this.requiredChips);
        return json;
    }

    /**
     * Gets the number of chips required to activate this ChipSocket.
     *
     * @return The required number of chips.
     */
    public int getRequiredChips() {
        return requiredChips;
    }
}
