package com.group4.chipgame.actors;

import com.group4.chipgame.collectibles.Key;
import javafx.scene.layout.Pane;

import java.util.EnumSet;
import java.util.Set;

public class Player extends Actor {
    private boolean isAlive = true; // To keep track of the player's state
    private Set<Key.KeyColor> collectedKeys = EnumSet.noneOf(Key.KeyColor.class); // Store collected keys using a set.

    public Player(double x, double y) {
        super("/images/chipgame/actors/steve.png", x, y);
    }

    // Method to add a key to the player's collection
    public void addKey(Key key) {
        collectedKeys.add(key.getColor());
    }

    // Method to check if the player has a specific key
    public boolean hasKey(Key.KeyColor keyColor) {
        return collectedKeys.contains(keyColor);
    }

    // Method to use a key (this removes it from the player's collection)
    public void useKey(Key.KeyColor keyColor) {
        collectedKeys.remove(keyColor);
    }

    // Method to kill the player
    public void kill(Pane gamePane) {
        // Set the player's state to not alive
        isAlive = false;

        // Remove player from the game pane
        gamePane.getChildren().remove(this);

        System.out.println("Player has been killed and removed from the game!");
        // Further logic can be added here if needed
    }

    // Getter to check if the player is alive
    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public void onCollect(Actor actor) {
        if (actor instanceof Key) {
            Key key = (Key) actor;
            this.addKey(key);
            System.out.println("Player collected a " + key.getColor() + " key!");
        }
    }
}
