package com.group4.chipgame.entities.actors.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Level.LevelRenderer;
import com.group4.chipgame.entities.actors.Actor;
import com.group4.chipgame.entities.actors.Player;
import org.json.JSONObject;

public class ChipSocket extends Tile {
    private final int requiredChips;

    public ChipSocket(int requiredChips) {
        super("/images/chipgame/tiles/chipSocket.png", false);
        this.requiredChips = requiredChips;
    }

    @Override
    public void onStep(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {
        if (actor instanceof Player player) {
            if (player.getChipsCount() >= requiredChips) {
                player.consumeChips(requiredChips);
                openSocket(levelRenderer);
            }
        }
    }

    private void openSocket(LevelRenderer levelRenderer) {
        levelRenderer.updateTile(this.gridX, this.gridY, new Path());
    }

    @Override
    public JSONObject serialize() {
        JSONObject json = super.serialize();
        json.put("requiredChips", this.requiredChips);
        return json;
    }
}
