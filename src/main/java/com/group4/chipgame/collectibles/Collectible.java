package com.group4.chipgame.collectibles;

import com.group4.chipgame.actors.Actor;
import com.group4.chipgame.actors.Player;

public class Collectible extends Actor {

    public Collectible(String imagePath, double gridX, double gridY) {
        super(imagePath, gridX, gridY);
    }

    @Override
    public void onCollect(Actor actor) {
        if (actor instanceof Player) {
            this.setVisible(false);
        }
    }
}