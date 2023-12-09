package com.group4.chipgame.entities.actors.tiles;

import com.group4.chipgame.entities.actors.Actor;
import com.group4.chipgame.Level.LevelRenderer;
import com.group4.chipgame.Direction;
import com.group4.chipgame.entities.actors.Player;
import com.group4.chipgame.events.LevelCompletedEvent;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class Exit extends Tile {
    public Exit() {
        super("/images/chipgame/tiles/exit.png", true);
    }

    @Override
    public void onStep(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {
        if (actor instanceof Player) {
            PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
            pause.setOnFinished(event -> fireEvent(new LevelCompletedEvent()));
            pause.play();
        }
    }
}
