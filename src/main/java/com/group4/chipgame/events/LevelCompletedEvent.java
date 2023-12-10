package com.group4.chipgame.events;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Event indicating that a level in the ChipGame has been completed.
 * This event can be used to trigger level completion logic such as updating the game state or UI.
 */
public class LevelCompletedEvent extends Event {
    public static final EventType<LevelCompletedEvent> LEVEL_COMPLETED = new EventType<>(Event.ANY, "LEVEL_COMPLETED");

    /**
     * Constructs a new LevelCompletedEvent.
     * This event is used to signal that the current level of the game has been successfully completed.
     */
    public LevelCompletedEvent() {
        super(LEVEL_COMPLETED);
    }
}
