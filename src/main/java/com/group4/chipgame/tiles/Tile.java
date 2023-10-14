package com.group4.chipgame.tiles;

import com.group4.chipgame.Main;
import com.group4.chipgame.actors.Actor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Objects;

public abstract class Tile extends ImageView {
    private final boolean isWalkable;
    private boolean occupied;

    public Tile(String imagePath, boolean isWalkable) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        setImage(image);
        this.setSmooth(true);
        this.isWalkable = isWalkable;

    }

    public void bindSize(Pane gamePane) {
        this.setFitWidth(Main.TILE_SIZE);
        this.setFitHeight(Main.TILE_SIZE);

    }

    public boolean isWalkable() {
        return isWalkable;
    }

    // Can override this method in subclasses for specific behavior when stepped on.
    public void onStep(Actor actor) {

    }

}