package com.group4.chipgame.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * TimerUI is a user interface component for displaying a countdown timer.
 * It extends BaseUI and provides functionalities
 * to update and display remaining time.
 * @author William Buckley
 */
public class TimerUI extends BaseUI {
    private Label timerLabel;
    private int timeRemaining;
    private final int maxTime;
    private Color currentColor;

    private static final double FONT_SIZE_RATIO = 20.0;
    private static final Insets TIMER_LABEL_MARGINS =
            new Insets(10, 10, 0, 0);
    private static final Pos TIMER_LABEL_ALIGNMENT = Pos.TOP_RIGHT;

    /**
     * Constructs a TimerUI instance.
     *
     * @param rootPane The root pane to which the timer UI will be added.
     * @param initialTime The initial time for the timer, in seconds.
     */
    public TimerUI(final Pane rootPane,
                   final int initialTime) {
        super(rootPane);
        this.timeRemaining = initialTime;
        this.maxTime = initialTime;
        this.currentColor = interpolateColor(1.0);
    }

    /**
     * Creates the timer UI components and adds them to the root pane.
     */
    @Override
    protected void createUI() {
        timerLabel = new Label("Time: " + timeRemaining);
        timerLabel.setTextFill(currentColor);
        StackPane.setAlignment(timerLabel, TIMER_LABEL_ALIGNMENT);
        StackPane.setMargin(timerLabel, TIMER_LABEL_MARGINS);
        getRootPane().getChildren().add(timerLabel);
    }

    /**
     * Adjusts the UI based on the size of the
     * root pane, particularly the font size of the timer label.
     */
    @Override
    protected void adjustUI() {
        setFontSizeFor(timerLabel, FONT_SIZE_RATIO);
    }

    /**
     * Updates the time displayed on the timer label.
     *
     * @param time The new time to display, in seconds.
     */
    public void updateTime(final int time) {
        Platform.runLater(() -> {
            this.timeRemaining = time;
            timerLabel.setText("Time: " + time);
            updateLabelColor(time);
        });
    }

    /**
     * Updates the color of the timer label
     * based on the fraction of time remaining.
     *
     * @param time The current time, used to
     *            calculate the fraction of time remaining.
     */
    private void updateLabelColor(final int time) {
        double fraction = (double) time / maxTime;
        currentColor = interpolateColor(fraction);
        timerLabel.setTextFill(currentColor);
    }

    /**
     * Interpolates a color between red and green based on the given fraction.
     * A fraction of 1.0 results in green, and a fraction of 0.0 results in red.
     *
     * @param fraction The fraction used to interpolate between red and green.
     * @return The interpolated color.
     */
    private Color interpolateColor(final double fraction) {
        double r = Math.max(0.0, Math.min(1.0, Color.RED.getRed()
                                - (Color.RED.getRed()
                                - Color.GREEN.getRed())
                                * fraction));
        double g = Math.max(0.0, Math.min(1.0, Color.RED.getGreen()
                + (Color.GREEN.getGreen()
                - Color.RED.getGreen())
                * fraction));
        double b = Math.max(0.0, Math.min(1.0, Color.RED.getBlue()
                + (Color.GREEN.getBlue()
                - Color.RED.getBlue())
                * fraction));
        return new Color(r, g, b, 1.0);
    }

    /**
     * Gets the time remaining displayed on the timer.
     *
     * @return The time remaining, in seconds.
     */
    public int getTimeRemaining() {
        return timeRemaining;
    }

    /**
     * Gets the timer label.
     *
     * @return The timer label.
     */
    public Label getTimerLabel() {
        return timerLabel;
    }
}
