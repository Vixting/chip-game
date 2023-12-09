package com.group4.chipgame.entities.actors.collectibles;

import com.group4.chipgame.entities.actors.Actor;
import com.group4.chipgame.entities.actors.Player;

public class Chip extends Collectible {

    public Chip(double x, double y) {
        super("/images/chipgame/collectibles/keys/chip.png", x, y);
    }

    @Override
    public void onCollect(Actor actor) {
        if (actor instanceof Player player) {
            super.onCollect(actor);
            player.addChips(1);
            this.setVisible(false);
        }
    }
}
