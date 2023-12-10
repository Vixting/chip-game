package com.group4.chipgame.entities.actors.tiles;

import com.group4.chipgame.entities.actors.Actor;
import com.group4.chipgame.Level.LevelRenderer;
import com.group4.chipgame.Direction;
import com.group4.chipgame.entities.actors.Player;
import com.group4.chipgame.events.LevelCompletedEvent;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

/**
 * Represents an Exit tile in the ChipGame.
 */
public class Exit extends Tile {
    private static final String EXIT_IMAGE_PATH = "/images/chipgame/tiles/exit.png";
    private static final double PAUSE_DURATION = 0.5;

    /**
     * Constructs a new Exit tile.
     */
    public Exit() {
        super(EXIT_IMAGE_PATH, true);
    }

    /**
     * Defines the action to be taken when an Actor steps on this Exit tile.
     *
     * @param actor The Actor stepping on the tile.
     * @param levelRenderer The renderer for the level.
     * @param incomingDirection The direction from which the Actor steps onto the tile.
     */
    @Override
    public void onStep(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {
        if (actor instanceof Player) {
            PauseTransition pause = new PauseTransition(Duration.seconds(PAUSE_DURATION));
            pause.setOnFinished(event -> fireEvent(new LevelCompletedEvent()));
            pause.play();
        }
    }
}
