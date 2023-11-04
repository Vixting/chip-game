package com.group4.chipgame.actors;

import com.group4.chipgame.Entity;
import com.group4.chipgame.collectibles.Key;
import javafx.scene.layout.Pane;

import java.util.EnumSet;
import java.util.Set;

public class Player extends Actor {
    private static final String PLAYER_IMAGE_PATH = "/images/chipgame/actors/steve.png";

    private boolean isAlive = true;
    private final Set<Key.KeyColor> collectedKeys = EnumSet.noneOf(Key.KeyColor.class);

    public Player(double x, double y) {
        super(PLAYER_IMAGE_PATH, x, y);
        this.moveInterval = 1;
    }

    public void addKey(Key key) {
        collectedKeys.add(key.getColor());
    }

    public boolean hasKey(Key.KeyColor keyColor) {
        return collectedKeys.contains(keyColor);
    }

    public void useKey(Key.KeyColor keyColor) {
        collectedKeys.remove(keyColor);
    }

    public void kill(Pane gamePane) {
        isAlive = false;
        gamePane.getChildren().remove(this);
        System.out.println("Player has been killed and removed from the game!");
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void onCollect(Entity actor) {
        if (actor instanceof Key key) {
            addKey(key);
            System.out.println("Player collected a " + key.getColor() + " key!");
        }
    }
}
