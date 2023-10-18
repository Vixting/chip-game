package com.group4.chipgame.tiles;

import com.group4.chipgame.actors.Actor;

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

    @Override
    public void setOccupiedBy(Actor actor) {
        super.setOccupiedBy(actor);
        setActive(actor != null);
    }
}