package com.group4.chipgame.entities.actors;

import javafx.geometry.Point2D;

/**
 * Represents a generic entity in the ChipGame.
 * This interface defines the basic functionality that all entities in the game should have.
 */
public interface Entity {

    /**
     * Gets the current position of the entity.
     * This method should return the position of the entity in the game world.
     *
     * @return The current position of the entity as a {@link Point2D} object.
     */
    Point2D getPosition();
}
