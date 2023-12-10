package com.group4.chipgame.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class TimerUI extends BaseUI {
    private Label timerLabel;
    private int timeRemaining;
    private final int maxTime;
    private Color currentColor;

    public TimerUI(Pane rootPane, int initialTime) {
        super(rootPane);
        this.timeRemaining = initialTime;
        this.maxTime = initialTime;
        this.currentColor = interpolateColor(1.0);
    }

    public Label getTimerLabel() {
        return timerLabel;
    }

    @Override
    protected void createUI() {
        timerLabel = new Label("Time: " + timeRemaining);
        timerLabel.setTextFill(currentColor);
        StackPane.setAlignment(timerLabel, Pos.TOP_RIGHT);
        StackPane.setMargin(timerLabel, new Insets(10, 10, 0, 0));
        getRootPane().getChildren().add(timerLabel);
    }

    @Override
    protected void adjustUI() {
        setFontSizeFor(timerLabel, 20);
    }

    public void updateTime(int time) {
        Platform.runLater(() -> {
            this.timeRemaining = time;
            timerLabel.setText("Time: " + time);
            updateLabelColor(time);
        });
    }

    private void updateLabelColor(int time) {
        double fraction = (double) time / maxTime;
        currentColor = interpolateColor(fraction);
        timerLabel.setTextFill(currentColor);
    }

    private Color interpolateColor(double fraction) {
        double r = Math.max(0.0, Math.min(1.0, Color.RED.getRed() - (Color.RED.getRed() - Color.GREEN.getRed()) * fraction));
        double g = Math.max(0.0, Math.min(1.0, Color.RED.getGreen() + (Color.GREEN.getGreen() - Color.RED.getGreen()) * fraction));
        double b = Math.max(0.0, Math.min(1.0, Color.RED.getBlue() + (Color.GREEN.getBlue() - Color.RED.getBlue()) * fraction));
        return new Color(r, g, b, 1.0);
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }
}
