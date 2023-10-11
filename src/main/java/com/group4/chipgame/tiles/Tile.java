package com.group4.chipgame.tiles;

import com.group4.chipgame.actors.Actor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

import java.util.Objects;

public abstract class Tile extends ImageView {
    private final boolean isWalkable;

    public Tile(String imagePath, boolean isWalkable) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        setImage(image);
        this.isWalkable = isWalkable;
    }

    public void bindSize(Pane gamePane) {
        this.fitWidthProperty().bind(gamePane.widthProperty().divide(10)); // 10 assuming your level is 10x10
        this.fitHeightProperty().bind(gamePane.heightProperty().divide(10));
    }

    public boolean isWalkable() {
        return isWalkable;
    }

    // Can override this method in subclasses for specific behavior when stepped on.
    public void onStep(Actor actor) {

    }

}