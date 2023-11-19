package com.group4.chipgame.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Entity;
import com.group4.chipgame.LevelRenderer;
import com.group4.chipgame.Main;
import com.group4.chipgame.actors.Actor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public abstract class Tile extends ImageView {
    private final boolean isWalkable;
    private boolean occupied;
    private Entity occupiedBy;
    protected int gridX;
    protected int gridY;

    public Tile(String imagePath, boolean isWalkable) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        setImage(image);
        setSmooth(true);
        this.isWalkable = isWalkable;
        bindSize();
    }

    public void setGridPosition(int x, int y) {
        gridX = x;
        gridY = y;
        updatePosition();
    }

    private void bindSize() {
        fitWidthProperty().bind(Main.TILE_SIZE.asObject());
        fitHeightProperty().bind(Main.TILE_SIZE.asObject());
    }

    private void updatePosition() {
        setLayoutX(gridX * Main.TILE_SIZE.get());
        setLayoutY(gridY * Main.TILE_SIZE.get());
    }

    public void setOccupiedBy(Entity actor) {
        occupiedBy = actor;
        occupied = actor != null;
    }

    public Entity getOccupiedBy() { return occupiedBy; }
    public boolean isWalkable() { return isWalkable; }
    public boolean isOccupied() { return !occupied; }

    public void onStep(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {
        // To be implemented by subclasses
    }
}

