package com.group4.chipgame;

import javafx.geometry.Point2D;

/**
 * The Entity interface represents an entity within the game that has a position.
 */
public interface Entity {

    /**
     * Gets the current position of the entity.
     *
     * @return The Point2D representing the position of the entity.
     */
    Point2D getPosition();
}
