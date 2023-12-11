package com.group4.chipgame.entities.actors.tiles;

import org.json.JSONObject;

/**
 * Represents a trap tile in the ChipGame.
 * A trap tile can be linked to a button
 * and changes its state based on the button's state.
 * @author William Buckley
 */
public class Trap extends Tile {
    private static final String TRAP_IMAGE_PATH =
            "/images/chipgame/tiles/trap.jpg";

    private Button linkedButton;
    private String id;

    /**
     * Constructs a new Trap tile linked to a
     * button and with a specific identifier.
     *
     * @param linkedButton The button linked to this trap.
     * @param id           The unique identifier for this trap.
     */
    public Trap(final Button linkedButton,
                final String id) {
        super(TRAP_IMAGE_PATH, true);
        this.linkedButton = linkedButton;
        this.id = id;
    }

    /**
     * Checks if the trap is active.
     * A trap is considered active if it is not linked to an active button.
     *
     * @return true if the trap is active, false otherwise.
     */
    public boolean isActive() {
        return linkedButton != null
                && !linkedButton.isActive();
    }

    /**
     * Gets the identifier of the connection.
     *
     * @return The unique identifier of the trap.
     */
    public String getConnection() {
        return id;
    }

    /**
     * Sets the identifier of the connection.
     *
     * @param id The new unique identifier for the trap.
     */
    public void setConnection(final String id) {
        this.id = id;
    }

    /**
     * Serializes the trap's state to a JSON object.
     *
     * @return A JSONObject representing the trap's state.
     */
    @Override
    public JSONObject serialize() {
        JSONObject json = super.serialize();
        json.put("id", this.getConnection());
        return json;
    }

    /**
     * Sets the button linked to this trap.
     *
     * @param linkedButton The button to link to the trap.
     */
    public void setLinkedButton(final Button linkedButton) {
        this.linkedButton = linkedButton;
    }
}
