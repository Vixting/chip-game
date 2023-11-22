package com.group4.chipgame.tiles;

import com.group4.chipgame.Entity;

/**
 * The Button class represents a tile in the ChipGame that can be activated or deactivated.
 * It extends the Tile class and inherits its properties and behavior.
 */
public class Button extends Tile {
    private boolean isActive;

    /**
     * Constructs a Button with the default image path and sets its initial state to inactive.
     */
    public Button() {
        super("/images/chipgame/tiles/button.png", true);
        this.isActive = false;
    }

    /**
     * Sets the activation state of the button.
     *
     * @param active True if the button is to be set as active, false otherwise.
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Checks if the button is currently active.
     *
     * @return True if the button is active, false otherwise.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Overrides the setOccupiedBy method in the Tile class to update the button's activation state based on
     * whether it is occupied by an actor or not.
     *
     * @param actor The Entity (actor) that occupies the button. Null if the button is unoccupied.
     */
    @Override
    public void setOccupiedBy(Entity actor) {
        super.setOccupiedBy(actor);
        setActive(actor != null);
    }
}
