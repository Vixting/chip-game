package com.group4.chipgame.tiles;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Water extends Tile {
    public Water() {
        super("/images/chipgame/tiles/water.jpg", true); // Assuming the second argument for Tile's constructor is 'isWalkable'.
    }
}
