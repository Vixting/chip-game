package com.group4.chipgame.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.LevelRenderer;
import com.group4.chipgame.Main;
import com.group4.chipgame.actors.Actor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public abstract class Tile extends ImageView {
    private final boolean isWalkable;
    private boolean occupied;
    private Actor occupiedBy;
    protected int gridX;
    protected int gridY;

    public Tile(String imagePath, boolean isWalkable) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        setImage(image);
        this.setSmooth(true);
        this.isWalkable = isWalkable;
    }

    public void setGridPosition(int x, int y) {
        this.gridX = x;
        this.gridY = y;
    }

    public void bindSize() {
        this.setFitWidth(Main.TILE_SIZE);
        this.setFitHeight(Main.TILE_SIZE);
    }

    public void setOccupiedBy(Actor actor) {
        this.occupiedBy = actor;
        setOccupied(actor != null);
    }

    public Actor getOccupiedBy() {
        return occupiedBy;
    }

    public boolean isWalkable() {
        return isWalkable;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    // Abstract method to be implemented by subclasses with specific behavior when stepped on.
    public void onStep(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {
    }
}
