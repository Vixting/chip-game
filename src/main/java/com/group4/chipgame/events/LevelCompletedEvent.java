package com.group4.chipgame.events;

import javafx.event.Event;
import javafx.event.EventType;

public class LevelCompletedEvent extends Event {
    public static final EventType<LevelCompletedEvent> LEVEL_COMPLETED = new EventType<>(Event.ANY, "LEVEL_COMPLETED");

    public LevelCompletedEvent() {
        super(LEVEL_COMPLETED);
    }
}
