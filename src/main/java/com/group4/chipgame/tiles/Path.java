package com.group4.chipgame.tiles;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Path extends Tile {
    public Path() {
        super("/images/chipgame/tiles/path.png", true); // Assuming the second argument for Tile's constructor is 'isWalkable'.
    }
}
