package com.group4.chipgame.entities.actors.tiles;

import com.group4.chipgame.entities.actors.Entity;
import org.json.JSONObject;

/**
 * Represents a Button tile.
 * @author William Buckley
 */
public class Button extends Tile {
    private static final String BUTTON_IMAGE_PATH =
            "/images/chipgame/tiles/button.png";
    private String id;
    private boolean isActive = false;

    /**
     * Constructs a Button with a given ID.
     *
     * @param id The ID of the Button.
     */
    public Button(final String id) {
        super(BUTTON_IMAGE_PATH, true);
        this.id = id;
    }

    /**
     * Gets the ID associated with this Button.
     *
     * @return The Button's ID.
     */
    public String getConnection() {
        return id;
    }

    /**
     * Sets a new ID for this Button.
     *
     * @param id The new ID to be set.
     */
    public void setConnection(final String id) {
        this.id = id;
    }

    /**
     * Sets the active state of the Button.
     *
     * @param active The active state to be set.
     */
    public void setActive(final boolean active) {
        isActive = active;
    }

    /**
     * Checks if the Button is active.
     *
     * @return True if active, false otherwise.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the Entity occupying this Button and updates its active state.
     *
     * @param actor The Entity to occupy the Button.
     */
    @Override
    public void setOccupiedBy(final Entity actor) {
        super.setOccupiedBy(actor);
        setActive(actor != null);
    }

    /**
     * Serializes the Button into a JSONObject.
     *
     * @return A JSONObject representing the Button's state.
     */
    @Override
    public JSONObject serialize() {
        JSONObject json = super.serialize();
        json.put("id", this.getConnection());
        json.put("isActive", this.isActive());
        return json;
    }
}

