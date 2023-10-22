package com.group4.chipgame.collectibles;

import com.group4.chipgame.Entity;
import com.group4.chipgame.Main;
import com.group4.chipgame.actors.Actor;
import com.group4.chipgame.actors.Player;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class Collectible extends ImageView implements Entity {
    protected Point2D currentPosition;

    public Collectible(String imagePath, double x, double y) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)), Main.ACTOR_SIZE, Main.ACTOR_SIZE, true, true);
        setImage(image);
        setSmooth(true);

        this.currentPosition = new Point2D(x, y);
    }

    @Override
    public Point2D getPosition() {
        return currentPosition;
    }


    public void onCollect(Actor actor) {
        if (actor instanceof Player) {
            this.setVisible(false);
        }
    }
}
