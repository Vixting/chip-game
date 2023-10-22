package com.group4.chipgame.tiles;

import com.group4.chipgame.Entity;

public class Button extends Tile {
    private boolean isActive;

    public Button() {
        super("/images/chipgame/tiles/button.png", true);
        this.isActive = false;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setOccupiedBy(Entity actor) {
        super.setOccupiedBy(actor);
        setActive(actor != null);
    }
}