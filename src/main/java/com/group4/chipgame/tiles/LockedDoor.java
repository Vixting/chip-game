package com.group4.chipgame.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.LevelRenderer;
import com.group4.chipgame.actors.Actor;
import com.group4.chipgame.actors.Player;
import com.group4.chipgame.collectibles.Key;

public class LockedDoor extends Tile {

    private final Key.KeyColor requiredKeyColor;

    public LockedDoor(Key.KeyColor requiredKeyColor) {
        super(getImagePathForColor(requiredKeyColor), false);
        this.requiredKeyColor = requiredKeyColor;
    }

    public Key.KeyColor getRequiredKeyColor() {
        return requiredKeyColor;
    }

    private static String getImagePathForColor(Key.KeyColor color) {
        return switch (color) {
            case RED -> "/images/chipgame/tiles/doors/door_red.jpg";
            case GREEN -> "/images/chipgame/tiles/doors/door_green.jpg";
            case YELLOW -> "/images/chipgame/tiles/doors/door_yellow.jpg";
            case BLUE -> "/images/chipgame/tiles/doors/door.jpg";
            default -> "images/chipgame/tiles/doors/door.jpg";
        };
    }

    public boolean correctKey(Key key) {
        return key.getColor() == requiredKeyColor;
    }

    @Override
    public void onStep(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {
        System.out.println("Locked door!");
        if (actor instanceof Player player) {
            if (player.hasKey(requiredKeyColor)) {
                player.useKey(requiredKeyColor);
                levelRenderer.updateTile(this.gridX, this.gridY, new Path());
            }
        }
    }
}
